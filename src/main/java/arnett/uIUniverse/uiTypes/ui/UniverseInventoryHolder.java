package arnett.uIUniverse.uiTypes.ui;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.InventoryHolder;

public interface UniverseInventoryHolder extends InventoryHolder {

    /**
     * @return The name used to identify this menu as a menu of this type, used for things like onMenuClosed
     */
    NamespacedKey getId();

}
