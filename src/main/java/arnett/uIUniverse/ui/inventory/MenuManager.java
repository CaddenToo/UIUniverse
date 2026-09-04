package arnett.uIUniverse.ui.inventory;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotManager;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class MenuManager {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/

    /**
     * Preloaded menus which are stored for quick access in cases where the menu needs to be loaded often
     */
    public static HashMap<MenuKey, Inventory> preloadedMenuInventories = new HashMap<>();

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
     * Registers a saved menu for preloading
     */
    public void preload(MenuKey key)
    {
        preloadedMenuInventories.put(key, getMenuInventory(key));
    }


    /**
     * Returns the stored Yaml Data for a given menu
     * @param key Key to find data for
     * @return Yaml file data, NULL if file was empty
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    public static YamlConfiguration getMenuYaml(@NotNull MenuKey key) throws IllegalArgumentException, IOException {
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
    public static File getMenuFile(MenuKey key) throws IOException {

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
            Files.createFile(menuFilePath);
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
     * Opens a menu for a player
     * @param player Player to open for
     * @param menu Menu to open
     * @return was the opening successful
     */
    public static InventoryView openMenu(Player player, UniverseInventoryHolder menu)
    {
        return openMenu(player, menu.getMenuKey());
    }

    /**
     * Opens a menu for a player
     * @param player Player to open for
     * @param key ID of the menu to open
     * @return was the opening successful
     */
    public static InventoryView openMenu(Player player, MenuKey key)
    {
        Inventory inventory = getMenuInventory(key);

        //failed to open
        if(inventory == null)
            return null;

        return player.openInventory(inventory);
    }

    public static ChestUIEditor getEditor(NamespacedKey key)
    {
        UniverseInventoryHolder holder = MenuManager.createMenuInstance(key);

        if(!(holder instanceof ChestUIHolder menu))
        {
            UIUniverse.logger.warning("At the moment the editor only works for chest UIs");
            return null;
        }

        return new ChestUIEditor(menu);
    }

    public static ChestUIEditor getEditor(ChestUIHolder menu)
    {
        return new ChestUIEditor(menu);
    }

    public static void openEditor(Player player, NamespacedKey key)
    {
        getEditor(key).openEditor(player);
    }

    public static void openEditor(Player player, ChestUIHolder menu)
    {
        getEditor(menu).openEditor(player);
    }

    public static UniverseInventoryHolder createMenuInstance(NamespacedKey menuId)
    {
        Inventory inv = getMenuInventory(new MenuKey(menuId, null));
        return (UniverseInventoryHolder) inv.getHolder();
    }

    //endregion



    //region Reading Menus

    /*=================================================================================================
                       -  Reading Menus  -
    =================================================================================================*/

    /**
     * Finds the menu YAML and returns the built inventory
     * @return Built inventory
     */
    public static Inventory getMenuInventory(MenuKey menuId)
    {
        try
        {
            //get the file to read from
            File menuFile = getMenuFile(menuId);

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(menuFile);

            System.out.println("Getting menu " + menuId.namespace() + " " + menuId.id());

            Class<? extends UniverseInventoryHolder> menuClass = registeredMenuClasses.get(menuId.namespace());

            UniverseInventoryHolder holder = menuClass.getConstructor(YamlConfiguration.class).newInstance(yaml);

            return holder.getInventory();

        }
        catch (Exception e)
        {
            UIUniverse.singleton.getLogger().warning("Unable to Load Inventory " + menuId.toString() + " : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //endregion





}
