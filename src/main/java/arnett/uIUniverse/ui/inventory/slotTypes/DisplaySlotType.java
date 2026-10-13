package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;

public class DisplaySlotType extends SlotType {

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
