package arnett.uIUniverse.ui.inventory.slotTypes.buttons.editing;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.dialog.types.value.ValuePrompter;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.ButtonSlot;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditSlotParametersButton  extends ButtonSlot {

    volatile boolean actingAsStorage = false;

    public EditSlotParametersButton(ItemStack displayItem) {
        super(displayItem);
    }

    public EditSlotParametersButton(Material contentMat, int amount) {
        super(contentMat, amount);
    }

    public EditSlotParametersButton(ConfigurationSection yaml) {
        super(yaml);
    }

    public EditSlotParametersButton() {
    }

    @Override
    public void execute(Player player, InventoryView view, Inventory inventory, int slot, PromptInput<?>... parameters) {

        //when clicked checks if it's in an edit menu and if so, calls the save and write function
        if(!(view.getTopInventory().getHolder() instanceof ChestUIEditor))
        {
            return;
        }

        ItemStack cursorItem = view.getCursor();

        System.out.println(actingAsStorage);
        System.out.println("Check instance: " + System.identityHashCode(this));

        //are we taking out an edited slot?
        if(actingAsStorage && cursorItem.isEmpty())
        {
            actingAsStorage = false;
            view.setCursor(inventory.getItem(slot));
            inventory.setItem(slot, definedContent);
            return;
        }

        //are we clicking with a slot Representation?
        if(ChestUIEditor.isSlotRepresentation(cursorItem))
        {
            try {
                BaseSlot representedSlot = ChestUIEditor.readSlotRepresentation(cursorItem);

                if(!(representedSlot instanceof ButtonSlot button))
                {
                    return;
                }

                List<PromptInput<?>> currentRepresentationParameters = Arrays.stream(button.parameters).toList();

                currentRepresentationParameters.forEach(part -> {
                    System.out.println("value1 " + part.getValue());
                });

                // if so prompt for the parameters
                new ValuePrompter(
                        "Input Button Parameters",
                        currentRepresentationParameters,
                        (dialogView, audience) -> {
                            // then update the button slot representation
                            ItemMeta cursorMeta = cursorItem.getItemMeta();


                            cursorMeta.getPersistentDataContainer().set(
                                    ChestUIEditor.slotRepresentation,
                                    PersistentDataType.STRING,
                                    representedSlot.writeToYaml().saveToString()
                            );
                            currentRepresentationParameters.forEach(part -> {
                                System.out.println("value2 " + part.getValue());
                            });

                            //have to set it here because lambda means button is a copy so the original
                            // buttons parameters are different from this one
                            button.parameters = currentRepresentationParameters.toArray(new PromptInput[0]);

                            //reset the display lore
                            cursorMeta.lore(button.getDisplayLore());

                            cursorItem.setItemMeta(cursorMeta);

                            //clear the cursor holder and place it in this slot for storage
                            inventory.setItem(slot, cursorItem);

                            actingAsStorage = true;

                            System.out.println("Lambda instance: " + System.identityHashCode(this));
                        },
                        null
                ).prompt(player);

                //remove the old itemStack
                view.setCursor(ItemStack.empty());

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }

        }

    }


    @Override
    public PromptInput<?>[] getDefaultParameters() {
        return new PromptInput[0];
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "saveedit");
    }
}
