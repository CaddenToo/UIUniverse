package arnett.uIUniverse.listeners;

import arnett.uIUniverse.ui.inventory.slotTypes.SlotType;
import arnett.uIUniverse.ui.inventory.UniverseInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class UISlotListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.isCancelled()) return;

        Inventory topInventory = e.getInventory();

        //only continue for custom inventories
        if (!(topInventory.getHolder() instanceof UniverseInventoryHolder holder)) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory clickedInventory = e.getClickedInventory();
        SlotType clickedSlot = holder.getBaseSlotSafe(e.getRawSlot());

        // if we clicked an immovable slot, cancel the event and call its click function
        if(!clickedSlot.isMovable())
        {
            e.setCancelled(true);
            clickedSlot.onSelect(player, clickedInventory, e.getSlot());
            return;
        }

        //other & bottom inventory useful for clicks that affect both
        Inventory bottomInventory = e.getView().getBottomInventory();
        Inventory otherInventory = clickedInventory == topInventory ? bottomInventory : topInventory;

        //the slot clicked is moveable but the click type may affect others which are immovable
        switch (e.getAction())
        {
            case MOVE_TO_OTHER_INVENTORY -> {
                if(otherInventory instanceof UniverseInventoryHolder)
                {
                    e.setCancelled(true);
                    holder.deposit(e.getCurrentItem());
                }
            }
            case COLLECT_TO_CURSOR -> {
                e.setCancelled(true);
                holder.collectTo(e.getCursor(), bottomInventory);
            }
        }
    }


    public void onDrag(InventoryDragEvent e)
    {
        if(e.isCancelled()) return;

        if(!(e.getInventory() instanceof UniverseInventoryHolder holder))
        {
            return;
        }
        if(!(e.getWhoClicked() instanceof Player player))
        {
            return;
        }

        //tracks whether any custom logic needs to be done
        AtomicBoolean inUniverseInventory = new AtomicBoolean(false);

        var deposits = e.getNewItems();
        int oldSlotCount = deposits.size();

        deposits.entrySet().removeIf((entry) -> {

            SlotType clickedSlot = holder.getBaseSlotSafe(entry.getKey());

            if(!clickedSlot.isMovable())
            {
                inUniverseInventory.set(true);
                return true;
            }

            return false;
        });

        //redistribute items ourselves if in custom inventory
        if(inUniverseInventory.get())
        {
            e.setCancelled(true);

            ItemStack draggedStack = e.getOldCursor();

            int oldCount = draggedStack.getAmount();
            int newCount = e.getCursor() == null ? 0 : e.getCursor().getAmount();

            //amount deposited in each stack
            int perSlotAmount = (oldCount - newCount) / oldSlotCount;

            //amount that can't be deposited because it's slots are immovable
            int refund = (deposits.size() - oldSlotCount) * perSlotAmount;

            //deposit the rest of the times
            deposits.forEach((index, stack) -> {
                e.getView().setItem(index, stack);
            });

            ItemStack cursor = e.getCursor();
            cursor.setAmount(newCount + refund);

            player.setItemOnCursor(cursor);
        }
    }
}
