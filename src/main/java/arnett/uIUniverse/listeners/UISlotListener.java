package arnett.uIUniverse.listeners;

import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.UniverseInventoryHolder;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UISlotListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(e.isCancelled())
            return;

        //only continue for custom inventories
        if (!(e.getInventory().getHolder() instanceof UniverseInventoryHolder holder))
        {
            return;
        }
        if(!(e.getWhoClicked() instanceof Player player))
        {
            return;
        }

        Inventory topInventory = e.getView().getTopInventory();
        Inventory bottomInventory = e.getView().getBottomInventory();
        int rawClickedSlot = e.getRawSlot();

        //only if we clicked in the top half of the inventory
        if(rawClickedSlot >= 0 && rawClickedSlot < topInventory.getSize()) {
            BaseSlot clickedSlot = holder.getBaseSlot(e.getSlot());

            if (clickedSlot == null)
            {
                e.setCancelled(true);
                return;
            }

            clickedSlot.onSelect(player, topInventory, rawClickedSlot);

            //stop if this slot isn't movable
            if(!clickedSlot.isMovable())
            {
                e.setCancelled(true);
                return;
            }
        }

        //clicking inside player inventory, not menu
        else
        {
            switch (e.getAction())
            {
                //check for shift click into to menu
                case MOVE_TO_OTHER_INVENTORY -> {
                    e.setCancelled(true);
                    holder.deposit(e.getCurrentItem());
                }

                //check for double click pickup
                case COLLECT_TO_CURSOR -> {
                    if(e.getCurrentItem() == null || e.getCurrentItem().isEmpty())
                    {
                        return;
                    }

                    //stop the event
                    e.setCancelled(true);

                    //make a copy so we don't lose it when moved
                    ItemStack currentItem = e.getCurrentItem().clone();

                    //move all of similar type
                    for(ItemStack stack : bottomInventory.getContents())
                    {
                        if(stack == null || stack.isEmpty())
                        {
                            continue;
                        }
                        else if(stack.isSimilar(currentItem))
                        {
                            holder.deposit(stack);
                        }
                    }
                }
            }
        }
    }

}
