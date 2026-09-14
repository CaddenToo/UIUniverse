package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class DisplaySlot extends BaseSlot{

    @Override
    public final boolean isMovable() {
        return false;
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "display");
    }

    @Override
    public DyeColor getEditorColor()
    {
        return DyeColor.PURPLE;
    }
}
