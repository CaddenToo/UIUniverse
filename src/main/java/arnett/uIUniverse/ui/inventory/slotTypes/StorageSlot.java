package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class StorageSlot extends BaseSlot {

    public StorageSlot()
    {
        super();
    }

    public StorageSlot(ItemStack content)
    {
        super(content);
    }

    public StorageSlot(Material contentMat, int amount)
    {
        super(contentMat, amount);
    }

    public StorageSlot(ConfigurationSection yaml) {
        super(yaml);
    }

    @Override
    public final boolean isMovable() {
        return true;
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "storage");
    }

    @Override
    public DyeColor getDisplayColor()
    {
        return DyeColor.GRAY;
    }
}
