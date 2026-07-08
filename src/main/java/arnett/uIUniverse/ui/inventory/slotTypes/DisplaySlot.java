package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class DisplaySlot extends BaseSlot{

    public DisplaySlot()
    {
        super();
    }

    public DisplaySlot(ItemStack displayItem)
    {
        super(displayItem);
    }

    public DisplaySlot(Material contentMat, int amount)
    {
        super(contentMat, amount);
    }

    public DisplaySlot(ConfigurationSection yaml)
    {
        super(yaml);
    }

    @Override
    public final boolean isMovable() {
        return false;
    }


    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "display");
    }

    @Override
    public DyeColor getDisplayColor()
    {
        return DyeColor.PURPLE;
    }
}
