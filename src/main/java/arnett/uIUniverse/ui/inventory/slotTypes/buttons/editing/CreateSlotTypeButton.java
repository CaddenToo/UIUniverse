package arnett.uIUniverse.ui.inventory.slotTypes.buttons.editing;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.ButtonSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateSlotTypeButton extends ButtonSlot {
    @Override
    public void execute(Player player, Inventory inventory, int slot, PromptInput<?>... parameters) {



    }

    @Override
    public PromptInput<?>[] getDefaultParameters() {
        return new PromptInput[0];
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "createrep");
    }
}
