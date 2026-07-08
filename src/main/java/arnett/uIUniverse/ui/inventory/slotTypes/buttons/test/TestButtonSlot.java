package arnett.uIUniverse.ui.inventory.slotTypes.buttons.test;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.dialog.types.value.parameters.types.FloatPromptInput;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.ButtonSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TestButtonSlot extends ButtonSlot {

    public TestButtonSlot(ItemStack displayItem) {
        super(displayItem);
    }

    public TestButtonSlot() {
    }

    public TestButtonSlot(ConfigurationSection yaml) {
        super(yaml);
    }

    @Override
    public void execute(Player player, Inventory inventory, int slot, PromptInput<?>... parameters) {
        player.teleport(player.getLocation().add(new Vector(0f, (float)parameters[0].getValue(), 0f)));
    }

    @Override
    public PromptInput<?>[] getDefaultParameters() {
        return new PromptInput[]{
            new FloatPromptInput("test", 1f)
        };
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "test");
    }

}
