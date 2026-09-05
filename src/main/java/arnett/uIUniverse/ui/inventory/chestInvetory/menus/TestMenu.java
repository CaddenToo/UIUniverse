package arnett.uIUniverse.ui.inventory.chestInvetory.menus;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.DisplaySlot;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.test.TestButtonSlot;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class TestMenu extends ChestUIHolder {

    public TestMenu(YamlConfiguration yaml) {
        super(yaml);
    }

    @Override
    public BaseSlot getDefaultSlot() {
        return new DisplaySlot();
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
    public Map<Character, BaseSlot> getDefaultDefinitions() {
        return Map.of(
                '1', new DisplaySlot(ItemStack.of(Material.ACACIA_BOAT)),
                '2', new TestButtonSlot(ItemStack.of(Material.PALE_OAK_BOAT))
        );
    }

    @Override
    public NamespacedKey getIdentifier() {
        return new NamespacedKey(UIUniverse.singleton, "test");
    }
}
