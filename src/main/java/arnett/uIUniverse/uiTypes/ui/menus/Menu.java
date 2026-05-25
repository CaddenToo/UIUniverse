package arnett.uIUniverse.uiTypes.ui.menus;

import arnett.uIUniverse.uiTypes.slotTypes.BaseSlot;
import arnett.uIUniverse.uiTypes.slotTypes.DisplaySlot;
import arnett.uIUniverse.uiTypes.ui.UniverseInventoryHolder;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

public abstract class Menu implements UniverseInventoryHolder {

    /**
     * @return String layout of the inventory similar to recipes.<br>
     * spaces " " are default slots.
     */
    public abstract List<String> getLayout();

    /**
     * @return Defines the possible item slots available to pick from for the menu.
     */
    public abstract Map<Character, BaseSlot> getDefinitions();

    /**
     * @return By default, is set to an empty display slot, so items can not be placed inside.
     */
    public BaseSlot getDefaultSlot()
    {
        return new DisplaySlot();
    }

    /**
     * @return The name used to identify this menu
     */
    public abstract String getId();

    /**
     * Called when the menu is closed
     */
    public abstract void onMenuClose(InventoryCloseEvent e);

    private Yaml saveToYaml()
    {

    }
}
