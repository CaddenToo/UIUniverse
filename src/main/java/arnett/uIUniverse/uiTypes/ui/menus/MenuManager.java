package arnett.uIUniverse.uiTypes.ui.menus;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class MenuManager {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/

    /**
     * Preloaded menus which are stored for quick access in cases where the menu needs to be loaded often
     */
    public static HashMap<NamespacedKey, Inventory> preloadedMenuInventories = new HashMap<>();

    /**
     * Tracks registered menu classes so that we can call methods of theirs later, like onMenuClose
     */
    public static HashMap<NamespacedKey, Menu> registeredMenuClasses = new HashMap<>();

    //endregion



    //region Registration

    /*=================================================================================================
                       -  Registration  -
    =================================================================================================*/

    /**
     * Saves the menu to a YML file if not existent
     * @param menu Menu to register
     */
    public static void registerMenu(Menu menu)
    {
        registerMenu(menu, false);
    }

    /**
     * Saves the menu to a YML file if not existent
     * @param menu Menu to register
     * @param preload Should this inventory be preloaded (good for if this menu is used a lot so we don't have
     *                  to load it from storage every time)
     */
    public static void registerMenu(Menu menu, boolean preload)
    {
        //track this menu class in the registry
        registeredMenuClasses.put(menu.getId(), menu);

        //saves the menu to a yaml in the data folder if no yaml file is present for this menu


        //inventory from the yaml file present in the data folder
        Inventory editedInventory;

        if(preload)
        {
            preloadedMenuInventories.put(menu.getId(), editedInventory);
        }
    }

    //endregion



    //region Saving Menus

    /*=================================================================================================
                       -  Saving Menus  -
    =================================================================================================*/

    /**
     * Returns the stored Yaml Data for a given menu
     * @param menu Menu to find data for
     * @return Yaml file data
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    public static YamlConfiguration getMenuYaml(@NotNull Menu menu) throws IOException {
        File menuFile = getMenuFile(menu.getId());
        return generateDefaultData(menu);
    }


    /**
     * Returns the stored Yaml Data for a given menu
     * @param menuID MenuID to find data for
     * @return Yaml file data
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    public static YamlConfiguration getMenuYaml(@NotNull NamespacedKey menuID) throws IllegalArgumentException, IOException {
        File menuFile = getMenuFile(menuID);

        /*todo
            get menu as yaml, if it does not have data but exists the the
            registry then use that menu to create it, otherwise if it does
            not have data and does not exist in the registry, throw an Exception
            because there is no menu class to create default data off of.
        * */

        return generateDefaultData(registeredMenuClasses.get(menuID));
    }


    /**
     * Decides whether this yaml is valid for creating a menu UI
     * @param yaml yaml to check
     * @return whether it is valid
     */
    public static boolean isValidUIYaml(YamlConfiguration yaml)
    {
        //todo
    }


    /**
     * Decides whether this file is valid yaml for creating a menu UI
     * @param file file to check for yaml
     * @return whether it is valid
     */
    public static boolean isValidUIYaml(File file)
    {
        return isValidUIYaml(YamlConfiguration.loadConfiguration(file));
    }

    /**
     * Creates the default Yaml file data for this menu
     * @param menu Menu to create default data for
     * @return Yaml data for this menu
     */
    public static YamlConfiguration generateDefaultData(@NotNull Menu menu)
    {
        //create the default data from menu object
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set();
    }


    /**
     * Retruns the file at the location pointed to by the menuID
     * @param menuID Menu ID which points to the file location
     * @return The yaml file for this menuID
     * @throws IOException Throws Error if unable to locate (or create if unable to locate) file
     */
    public static File getMenuFile(NamespacedKey menuID) throws IOException {

        //get the path of the data folder
        File dataFolder = UIUniverse.singleton.getDataFolder();
        Path dataFolderPath = dataFolder.toPath();

        //get the path to the Menus directory
        Path menuFolderPath = dataFolderPath.resolve("Menus");

        //make sure that menus directory exists
        if(!Files.exists(menuFolderPath))
        {
            Files.createDirectories(menuFolderPath);
        }

        //now find the file of the namespaced key
        String[] paths = menuID.getKey().split("/");

        //find this namespaces folder
        Path menuFilePath = menuFolderPath.resolve(menuID.getNamespace());

        //snake down the tree
        for(int i = 0; i < paths.length; i++)
        {
            menuFilePath = menuFilePath.resolve(paths[i] + (i == paths.length-1 ? ".yaml" : ""));
        }

        //make sure that menus directory exists
        if(!Files.exists(menuFilePath.getParent()))
        {
            Files.createDirectories(menuFolderPath.getParent());
        }

        //make sure the Yaml file exists
        if(!Files.exists(menuFilePath))
        {
            Files.createFile(menuFilePath);
        }

        //return the file
        return menuFilePath.toFile();
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
    public static boolean openMenu(Player player, Menu menu)
    {
        return openMenu(player, menu.getId());
    }

    /**
     * Opens a menu for a player
     * @param player Player to open for
     * @param menuId ID of the menu to open
     * @return was the opening successful
     */
    public static boolean openMenu(Player player, NamespacedKey menuId)
    {
        Inventory inventory = getMenuInventory(menuId);

        //failed to open
        if(inventory == null)
            return false;

        if(player.openInventory(inventory) == null)
        {
            //failed to open
            return false;
        }

        //successfully opened
        return true;
    }

    //endregion



    //region Reading Menus

    /*=================================================================================================
                       -  Reading Menus  -
    =================================================================================================*/

    /**
     * Translates a Yaml section to an Inventory
     * @param yaml
     * @return
     */
    public static Inventory buildMenu(YamlConfiguration yaml)
    {

    }

    /**
     * Finds the menu YAML and returns the built inventory
     * @return Built inventory
     */
    public static Inventory getMenuInventory(NamespacedKey menuId)
    {

    }

    //endregion





}
