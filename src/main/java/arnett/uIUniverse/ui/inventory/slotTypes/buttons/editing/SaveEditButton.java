package arnett.uIUniverse.ui.inventory.slotTypes.buttons.editing;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.ButtonSlotType;
import arnett.uIUniverse.ui.inventory.MenuManager;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SaveEditButton extends ButtonSlotType {

    @Override
    public void execute(Player player, Inventory inventory, int slot, PromptInput<?>... parameters) {

        System.out.println("SAVING!");

        //when clicked checks if it's in an edit menu and if so, calls the save and write function
        if(inventory.getHolder() instanceof ChestUIEditor editor)
        {
            editor.saveWork();
            try {
                MenuManager.overwriteMenuSave(editor);
            } catch (Exception e) {
                UIUniverse.logger.warning("Can not save edited data for menu");
                e.printStackTrace();
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
