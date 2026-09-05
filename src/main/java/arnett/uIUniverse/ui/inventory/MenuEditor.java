package arnett.uIUniverse.ui.inventory;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import arnett.uIUniverse.ui.inventory.chestInvetory.editor.ChestUIEditor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class MenuEditor {

    public static ChestUIEditor getEditor(NamespacedKey key)
    {
        UniverseInventoryHolder holder = MenuManager.getMenuInventoryHolder(new MenuKey(key, null));

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
}
