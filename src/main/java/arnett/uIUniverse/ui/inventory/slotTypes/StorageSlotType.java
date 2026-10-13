package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;

public class StorageSlotType extends SlotType {

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
