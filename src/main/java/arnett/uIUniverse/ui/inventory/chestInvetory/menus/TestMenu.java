package arnett.uIUniverse.ui.inventory.chestInvetory.menus;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotType;
import arnett.uIUniverse.ui.inventory.slotTypes.DisplaySlotType;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.test.TestButtonSlotType;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class TestMenu extends ChestUIHolder {

    @Override
    public SlotType getDefaultSlot() {
        return new DisplaySlotType();
    }

    @Override
    public List<String> getDefaultLayout() {
        return List.of(
                "         ",
                " 1  2  1 ",
                "         "
        );
    }

    @Override
    public Map<Character, SlotType> getDefaultDefinitions() {
        return Map.of(
                '1', new DisplaySlotType().setSavedContent(ItemStack.of(Material.ACACIA_BOAT)),
                '2', new TestButtonSlotType().setSavedContent(ItemStack.of(Material.PALE_OAK_BOAT))
        );
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "test");
    }
}
