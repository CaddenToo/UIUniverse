package arnett.uIUniverse.listeners;

import arnett.uIUniverse.ui.inventory.UniverseInventoryHolder;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class UIMenuListener implements Listener {

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e)
    {
        if(e.getInventory().getHolder() instanceof UniverseInventoryHolder holder)
        {
            holder.onMenuClose(e);
        }
    }

    @EventHandler
    public void onItemThrow(PlayerDropItemEvent e)
    {
        if(ChestUIEditor.isSlotRepresentation(e.getItemDrop().getItemStack()))
        {
            e.getItemDrop().remove();
        }
    }

}
