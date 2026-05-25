package arnett.uIUniverse.uiTypes.ui.menus;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

public class MenuManager {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/

    /**
     * Preloaded menus which are stored for quick access in cases where the menu needs to be loaded often
     */
    public HashMap<NamespacedKey, Inventory> preloadedMenus = new HashMap<>();


    //endregion

    //region Registration

    /*=================================================================================================
                       -  Registration  -
    =================================================================================================*/

    /**
     * Saves the menu to a YML file if not existent
     * @param menu
     */
    public static void registerMenu(Menu menu)
    {
        registerMenu(menu, false);
    }

    /**
     *
     * @param menu
     * @param preload
     */
    public static void registerMenu(Menu menu, boolean preload)
    {
        //saves the menu to the yml

    }

    //endregion

    //region Opening Menus

    /*=================================================================================================
                       -  Opening Menus  -
    =================================================================================================*/

    /**
     * Finds the menu YAML and returns the built inventory
     * @return Built inventory
     */
    public static Inventory getMenu()
    {

    }

    //endregion

    //region Building Menus

    /*=================================================================================================
                       -  Building Menus  -
    =================================================================================================*/

    /**
     * Translates a Yaml section to an Inventory
     * @param yaml
     * @return
     */
    public static Inventory buildMenu(Yaml yaml)
    {

    }

    //endregion





}
