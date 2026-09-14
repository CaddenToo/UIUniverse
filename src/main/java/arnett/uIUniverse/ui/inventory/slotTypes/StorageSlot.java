package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class StorageSlot extends BaseSlot {

    @Override
    public final boolean isMovable() {
        return true;
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "storage");
    }

    @Override
    public DyeColor getEditorColor()
    {
        return DyeColor.GRAY;
    }
}
