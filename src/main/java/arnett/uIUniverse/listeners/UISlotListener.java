package arnett.uIUniverse.listeners;

import arnett.uIUniverse.uiTypes.ui.UniverseInventoryHolder;
import arnett.uIUniverse.uiTypes.ui.menus.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UISlotListener extends Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        //only continue for custom inventories
        if (!(e.getInventory().getHolder() instanceof UniverseInventoryHolder))
        {
            return;
        }


    }
}
