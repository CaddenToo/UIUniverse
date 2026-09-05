package arnett.uIUniverse.ui.inventory;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotManager;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class MenuManager {

    //region Variables

    /*=================================================================================================
                       -  Variables  -
    =================================================================================================*/

    /**
     * Preloaded menus which are stored for quick access in cases where the menu needs to be loaded often
     */
    public static HashMap<MenuKey, Inventory> preloadedMenuInventories = new HashMap<>();

    /**
     * Tracks open inventories for multiplayer syncing for instanced menus
     */
    public static HashMap<MenuKey, UniverseInventoryHolder> activeInventories = new HashMap<>();

    /**
     * Tracks registered menu classes so that we can call methods of theirs later, like onMenuClose
     */
    public static HashMap<NamespacedKey, Class<? extends UniverseInventoryHolder>> registeredMenuClasses = new HashMap<>();
    //endregion



    //region Registration

    /*=================================================================================================
                       -  Registration  -
    =================================================================================================*/

    /**
     * Registers the menu type with the manager
     * @param menuClass Class of menu to register
     */
    public static void registerMenu(Class<? extends UniverseInventoryHolder> menuClass) {
        registerMenu(menuClass, false);
    }

    /**
     * Registers the menu type with the manager
     * @param menuClass Class of menu to register
     * @param preload Should this inventory be preloaded (good for if this menu is used a lot so we don't have
     *                  to load it from storage every time)
     */
    public static void registerMenu(Class<? extends UniverseInventoryHolder> menuClass, boolean preload) {

        //create a default version of this menu so we can use its properties for setup
        UniverseInventoryHolder menu;
        try
        {
            menu = menuClass.getConstructor(YamlConfiguration.class).newInstance(new YamlConfiguration());
        }
        catch (Exception e)
        {
            UIUniverse.logger.warning("Unable to Register menu of class" + menuClass.getName()
                    + " Can't call constructor class(YamlConfiguration null)");
            e.printStackTrace();
            return;
        }

        NamespacedKey namespacedKey = menu.getIdentifier();

        UIUniverse.logger.info("Registering menu: " + namespacedKey.toString());

        //register the default slots of the menu
        try
        {
            menu.getDefaultDefinitions().forEach((character, baseSlot) -> {

                SlotManager.registerSlot(baseSlot);

            });
        }
        catch (Exception e)
        {
            UIUniverse.logger.info("Unable to Slot(s) for menu " + namespacedKey.toString());
        }

        //save static menu to data folder if not present already
        //Menu from the yaml file present in the data folder (or from the default menu provided)
        YamlConfiguration existingMenuConfig;
        File menuFile;

        try
        {
            menuFile = getMenuFile(new MenuKey(namespacedKey, null));

            if(menuFile.length() == 0)
                existingMenuConfig = null;
            else
                existingMenuConfig = YamlConfiguration.loadConfiguration(menuFile);
        }
        catch (Exception e)
        {
            UIUniverse.logger.info("Unable to Register menu " + namespacedKey.toString());
            e.printStackTrace();
            return;
        }

        //saves the menu to a yaml in the data folder if no valid yaml file is present for this menu
        if(existingMenuConfig == null)
        {
            //there is no menu in this file and it is not empty so return
            try
            {
                overwriteMenuSave(menu);
                UIUniverse.logger.info("Saved menu " + namespacedKey.toString() + " File now Exists");
            }
            catch (Exception e)
            {
                UIUniverse.logger.warning("Unable to save menu file " + namespacedKey.toString());
                e.printStackTrace();
                return;
            }
        }
        else
        {
            if(existingMenuConfig.getKeys(false).isEmpty())
            {
                UIUniverse.logger.info("Didn't Save menu " + namespacedKey.toString() + " File already Exists");
                return;
            }
        }

        //track this menu class in the registry
        registeredMenuClasses.put(namespacedKey, menuClass);

        //if this should be preloaded save it
        if(preload)
        {

        }
    }

    //endregion



    //region Saving Menus

    /*=================================================================================================
                       -  Saving Menus  -
    =================================================================================================*/


    /**
     * Returns the stored Yaml Data for a given menu  instance
     * @param key Key to find data for
     * @return Yaml file data, NULL if file was empty
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    static YamlConfiguration getMenuYaml(@NotNull MenuKey key) throws IllegalArgumentException, IOException {
        File menuFile = getMenuFile(key);

        if(menuFile.length() == 0)
            return null;

        return new YamlConfiguration().loadConfiguration(menuFile);
    }


    /**
     * Retruns the file at the location pointed to by the menuID
     * @param key Menu key which points to the file location
     * @return The yaml file for this menuID
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    static File getMenuFile(MenuKey key) throws IOException {

        //get the path of the data folder
        File dataFolder = UIUniverse.singleton.getDataFolder();
        Path dataFolderPath = dataFolder.toPath();

        NamespacedKey namespace = key.namespace();
        UUID id = key.id();

        //get the path to the Menus directory
        Path menuFolderPath = dataFolderPath.resolve("Menus");

        //make sure that menus directory exists
        if(!Files.exists(menuFolderPath))
        {
            Files.createDirectories(menuFolderPath);
        }

        //now find the file of the namespaced key
        String[] paths = (namespace.getKey() + (id == null ? "" : ("/" + id)) + ".yaml").split("/");

        //find this namespaces folder
        Path menuFilePath = menuFolderPath.resolve(namespace.getNamespace());

        //snake down the tree
        for (String path : paths) {
            menuFilePath = menuFilePath.resolve(path);
        }

        //make sure that menus directory exists
        if(!Files.exists(menuFilePath.getParent()))
        {
            Files.createDirectories(menuFilePath.getParent());
        }

        //make sure the Yaml file exists
        if(!Files.exists(menuFilePath))
        {
            //if this is an instance menu load what is in the default location
            if(key.id() != null)
            {
                //copies the default (sample) file to a new one
                File sample = getMenuFile(new MenuKey(key.namespace(), null));
                Files.copy(sample.toPath(), menuFilePath);
            }
            else
            {
                //create the file
                Files.createFile(menuFilePath);
            }
        }

        //return the file
        return menuFilePath.toFile();
    }

    public static void overwriteMenuSave(UniverseInventoryHolder ui) throws IOException {
        ui.writeToYaml().save(getMenuFile(ui.getMenuKey()));
    }

    //endregion



    //region Opening Menus

    /*=================================================================================================
                       -  Opening Menus  -
    =================================================================================================*/

    /**
     * Opens a static menu for a player
     * @param player Player to open for
     * @param menu Menu to open
     * @return was the opening successful
     */
    public static InventoryView openMenu(Player player, UniverseInventoryHolder menu)
    {
        return openMenu(player, menu.getMenuKey());
    }

    /**
     * Opens a menu instance for a player
     * @param player Player to open for
     * @param key ID of the menu to open
     * @return was the opening successful
     */
    public static InventoryView openMenu(Player player, MenuKey key)
    {
        UniverseInventoryHolder holder = getMenuInventoryHolder(key);

        //failed to open
        if(holder == null)
            return null;

        return player.openInventory(holder.getInventory());
    }

    public static UniverseInventoryHolder createMenuInstance(NamespacedKey baseTypeIdentifier)
    {
        return getMenuInventoryHolder(new MenuKey(baseTypeIdentifier, UUID.randomUUID()));
    }

    /**
     * Finds the menu YAML and returns the built inventory
     * @return Built inventory
     */
    public static UniverseInventoryHolder getMenuInventoryHolder(MenuKey menuKey)
    {
        //first see if this inventory is opened
        if(activeInventories.containsKey(menuKey))
        {
            return activeInventories.get(menuKey);
        }

        try
        {
            //get the file to read from
            File menuFile = getMenuFile(menuKey);

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(menuFile);
            Class<? extends UniverseInventoryHolder> menuClass = registeredMenuClasses.get(menuKey.namespace());
            UniverseInventoryHolder holder = menuClass.getConstructor(YamlConfiguration.class).newInstance(yaml);

            //set the instance id
            holder.setMenuKey(menuKey.id());

            return holder;

        }
        catch (Exception e)
        {
            UIUniverse.singleton.getLogger().warning("Unable to Load Inventory " + menuKey.toString() + " : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //endregion


    //region Save to / Read from PDC

    /*=================================================================================================
                        -  Save to / Read from PDC  -
    =================================================================================================*/

    public static void openFrom(PersistentDataContainerView pdcv, Player opener)
    {
        openFrom(pdcv, opener, "storage");
    }

    public static void openFrom(PersistentDataContainerView pdcv, Player opener, String tag)
    {
        String[] keyString = pdcv.get(
                new NamespacedKey(UIUniverse.singleton, tag),
                PersistentDataType.STRING
        ).split("/");

        MenuKey key = new MenuKey(
                NamespacedKey.fromString(keyString[0]),
                UUID.fromString(keyString[1])
        );

        openMenu(opener, key);
    }

    public static void saveToPDC(PersistentDataContainer pdc, UniverseInventoryHolder holder)
    {
        saveToPDC(pdc, holder, "storage");
    }

    public static void saveToPDC(PersistentDataContainer pdc, UniverseInventoryHolder holder, String tag)
    {
        MenuKey key = holder.getMenuKey();

        pdc.set(
                new NamespacedKey(UIUniverse.singleton, tag),
                PersistentDataType.STRING,
                key.namespace().toString() + "/" + key.id().toString()
        );
    }


    static void untagPDC(PersistentDataContainer pdc, String tag)
    {
        pdc.remove(new NamespacedKey(UIUniverse.singleton, tag));
    }


    //endregion

}
