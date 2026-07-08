package arnett.uIUniverse.ui.inventory;

import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class UniverseInventoryHolder implements InventoryHolder {

    public UniverseInventoryHolder(String[] layout, HashMap<Character, BaseSlot> definitions, BaseSlot defaultSlot)
    {
        this.layout = layout;
        this.definitions = definitions;
        definitions.put(' ', defaultSlot);
    }

    public UniverseInventoryHolder(YamlConfiguration yaml) {

    }

    public UniverseInventoryHolder()
    {
        this.layout = getDefaultLayout();
        this.definitions = new HashMap<>(getDefaultDefinitions());
        definitions.put(' ', getDefaultSlot());
    }


    public String[] layout = {
            "",
            "",
            "",
            "",
            "",
            "",
    };

    public HashMap<Character, BaseSlot> definitions = new HashMap<>();


    //region Default Properties

    /*=================================================================================================
                       -  Default Properties  -
    =================================================================================================*/

    /**
     * @return By default, is set to an empty display slot, so items can not be placed inside.
     */
    public abstract BaseSlot getDefaultSlot();

    /**
     * @return String layout of the inventory, similar to recipes,
     * uses getDefaultDefinitions() for which character represents which slot.<br>
     * spaces " " are default slots.
     */
    public abstract String[] getDefaultLayout();

    /**
     * @return Defines the possible item slots available to pick from for the menu.
     * Similar to the Ingredients List with Recipes
     * The space key ' ' is reserved for default slots
     */
    public abstract Map<Character, BaseSlot> getDefaultDefinitions();

    //endregion


    /**
     * Gets the slot data for a specific slot
     * @param slotNumber Slot number to check
     * @return The slot data
     * @throws IndexOutOfBoundsException Thrown if index is out of bounds
     */
    public abstract BaseSlot getBaseSlot(int slotNumber) throws IndexOutOfBoundsException;


    /**
     * Called when a menu with this ID is closed
     */
    public void onMenuClose(InventoryCloseEvent e) {}


    /**
     * Converts the default layout to Yaml data so it can be stored
     * @return converted Yaml menu
     */
    public abstract YamlConfiguration writeToYaml() throws MatchException;

    /**
     * @return The name used to identify this menu as a menu of this type, used for things like onMenuClosed
     */
    public abstract NamespacedKey getIdentifier();

    public abstract Inventory buildInventory();


    public void deposit(Inventory inventory, ItemStack stack)
    {
        for (int i = 0; i < inventory.getSize(); i++)
        {
            ItemStack itemInSlot = inventory.getItem(i);
            BaseSlot slotType = getBaseSlot(i);

            //is this a depsoitable slot
            if(!slotType.isMovable())
            {
                continue;
            }
            //is this an open slot?
            else if(itemInSlot == null || itemInSlot.isEmpty())
            {
                //clone the
                inventory.setItem(i, stack.clone());
                //remove the original
                stack.setAmount(0);
            }
            //is this slot stackable
            else if(itemInSlot.getMaxStackSize() > itemInSlot.getAmount() && itemInSlot.isSimilar(stack))
            {
                int oldSlotAmount = itemInSlot.getAmount();
                int incomingAmount = itemInSlot.getAmount();
                int combinedAmount = incomingAmount + oldSlotAmount;
                //cap the total at the max stack size
                int newSlotAmount = Math.max(combinedAmount, itemInSlot.getMaxStackSize());

                //update itemStacks sizes
                itemInSlot.setAmount(newSlotAmount);
                stack.setAmount(combinedAmount - newSlotAmount);

                //if we have deposited it all then return
                if(stack.getAmount() <= 0)
                {
                    return;
                }
            }
        }
    }

    protected char getAvailableCharacter()
    {
        for(char i = 'A'; i < 'Z'; i++)
        {
            if(!definitions.containsKey(i))
            {
                return i;
            }
        }
        for(char i = 'a'; i < 'z'; i++)
        {
            if(!definitions.containsKey(i))
            {
                return i;
            }
        }
        for(char i = '0'; i < '9'; i++)
        {
            if(!definitions.containsKey(i))
            {
                return i;
            }
        }
        for(char i = '!'; i < 'ÿ'; i++)
        {
            if(!definitions.containsKey(i))
            {
                return i;
            }
        }
        return '0';
    }
}
