package arnett.uIUniverse.ui.inventory;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.helpers.ItemStackHelpers;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class UniverseInventoryHolder implements InventoryHolder {

    //region Properties

    /*=================================================================================================
                        -  Properties  -
    =================================================================================================*/

    private Inventory inventory;

    private MenuKey menuKey;

    public String[] layout = {
            "         ",
            "         ",
            "         ",
            "         ",
            "         ",
            "         "
    };

    public HashMap<Character, BaseSlot> definitions = new HashMap<>();

    /**
     * @return menu key for access in the MenuManager
     */
    public MenuKey getMenuKey()
    {
        if(menuKey == null)
        {
            menuKey = new MenuKey(getIdentifier(), null);
        }

        return menuKey;
    }

    public void setMenuKey(UUID instanceId) {
        menuKey = new MenuKey(getIdentifier(), instanceId);
    }

    //endregion


    //region Initialization

    /*=================================================================================================
                        -  Initialization  -
    =================================================================================================*/

    public UniverseInventoryHolder()
    {
        this.layout = getValidatedDefaultLayout().toArray(new String[0]);
        this.definitions = new HashMap<>(getDefaultDefinitions());
        definitions.put(' ', getDefaultSlot());
    }

    //endregion


    //region Abstract Properties

    /*=================================================================================================
                       -  Abstract Properties  -
    =================================================================================================*/

    /**
     * @return The name used to identify this menu as a menu of this type, used for things like onMenuClosed
     */
    public abstract NamespacedKey getIdentifier();

    /**
     * @return By default, is set to an empty display slot, so items can not be placed inside.
     */
    public abstract BaseSlot getDefaultSlot();

    /**
     * @return String layout of the inventory, similar to recipes,
     * uses getDefaultDefinitions() for which character represents which slot.<br>
     * spaces " " are default slots.
     */
    public abstract List<String> getDefaultLayout();

    /**
     * Fixes any layout given so that every slot is defined even for when layouts are sketchy
     * @return Fixed default layout
     */
    public abstract List<String> getValidatedDefaultLayout();

    /**
     * @return Defines the possible item slots available to pick from for the menu.
     * Similar to the Ingredients List with Recipes
     * The space key ' ' is reserved for default slots
     */
    public abstract Map<Character, BaseSlot> getDefaultDefinitions();

    //endregion


    //region Inventory

    /*=================================================================================================
                        -  Inventory  -
    =================================================================================================*/

    /**
     * Gets the slot data for a specific slot
     * @param slotNumber Slot number to check
     * @return The slot data
     * @throws IndexOutOfBoundsException Thrown if index is out of bounds
     */
    public abstract BaseSlot getBaseSlot(int slotNumber) throws IndexOutOfBoundsException;


    /**
     * @return gets the inventory or creates on if not already built
     */
    @Override
    public @NotNull Inventory getInventory() {
        if(inventory == null)
        {
            UIUniverse.logger.info("Building inventory for " + getIdentifier().toString());
            inventory = buildInventory();
            return inventory;
        }
        else {
            return inventory;
        }
    }

    /**
     * @return Built inventory object from data
     */
    public abstract Inventory buildInventory();

    //endregion


    //region Events

    /*=================================================================================================
                        -  Events  -
    =================================================================================================*/


    /**
     * Called when a menu with this ID is closed
     */
    @OverridingMethodsMustInvokeSuper
    public void onMenuClose(InventoryCloseEvent e) {
        //player is still counted as looking here
        if (getInventory().getViewers().size() <= 1)
        {
            MenuManager.activeInventories.remove(menuKey);
        }
    }

    /**
     * Called when a menu with this ID is opened
     */
    @OverridingMethodsMustInvokeSuper
    public void onMenuOpen(InventoryOpenEvent e) {
        //save to active inventories
        MenuManager.activeInventories.put(menuKey, this);
    }


    //endregion


    //region Yaml

    /*=================================================================================================
                        -  Yaml  -
    =================================================================================================*/


    /**
     * Converts the default layout to Yaml data so it can be stored
     * @return converted Yaml menu
     */
    public abstract YamlConfiguration writeToYaml() throws MatchException;


    /**
     * Reads in data from a yaml file for setup
     * @param yaml yaml config to read from
     */
    public abstract UniverseInventoryHolder readFromYaml(YamlConfiguration yaml);

    //endregion


    //region Helpers

    /*=================================================================================================
                        -  Helpers  -
    =================================================================================================*/


    /**
     * Deposits as much of provided item stack as possible to an open or stackable (moveable) item slot
     * @param stack Stack to deposit
     */
    public void deposit(ItemStack stack)
    {
        Inventory inventory = getInventory();

        for (int i = 0; i < inventory.getSize(); i++)
        {
            ItemStack itemInSlot = inventory.getItem(i);
            BaseSlot slotType = getBaseSlot(i);

            //is this a moveable slot
            if(!slotType.isMovable())
            {
                continue;
            }
            //is this an open slot?
            else if(itemInSlot == null || itemInSlot.isEmpty())
            {
                //clone the stack
                inventory.setItem(i, stack.clone());
                //remove the original
                stack.setAmount(0);
            }
            //is this slot stackable
            else if(itemInSlot.getMaxStackSize() > itemInSlot.getAmount() && itemInSlot.isSimilar(stack))
            {
                ItemStackHelpers.stackSimilarItems(stack, itemInSlot);

                //if we have deposited it all then return
                if(stack.getAmount() <= 0)
                {
                    return;
                }
            }
        }
    }

    /**
     * Collects all similar (moveable) item types to the target until the target has reached max stack size
     * @param target Stack to collect to
     */
    public void collectTo(ItemStack target)
    {
        Inventory inventory = getInventory();

        //don't bother if we can't stack regardless
        if(target.getAmount() >= target.getMaxStackSize())
        {
            return;
        }

        for (ItemStack stack : inventory)
        {
            if(stack.isSimilar(target))
            {
                if(ItemStackHelpers.stackSimilarItems(stack, target)) return;
            }
        }
    }


    /**
     * Returns a character not yet used in the slot definitions
     */
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

    //endregion


}
