package arnett.uIUniverse;

import arnett.uIUniverse.commands.*;
import arnett.uIUniverse.commands.list.ListMenus;
import arnett.uIUniverse.commands.list.ListSlots;
import arnett.uIUniverse.listeners.UIMenuListener;
import arnett.uIUniverse.listeners.UISlotListener;
import arnett.uIUniverse.ui.inventory.slotTypes.DisplaySlot;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotManager;
import arnett.uIUniverse.ui.inventory.slotTypes.StorageSlot;
import arnett.uIUniverse.ui.inventory.MenuManager;
import arnett.uIUniverse.ui.inventory.chestInvetory.menus.TestMenu;
import cd.arnett.caddamands.cattamands.cattamand.LiteralCattamand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public final class UIUniverse extends JavaPlugin {

    public static JavaPlugin singleton;
    public static Logger logger;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Plugin startup logic
        singleton = this;

        logger = getLogger();

        getServer().getPluginManager().registerEvents(new UISlotListener(), this);
        getServer().getPluginManager().registerEvents(new UIMenuListener(), this);

        MenuManager.registerMenu(TestMenu.class);

        //register slot types
        SlotManager.registerSlot(DisplaySlot.class);
        SlotManager.registerSlot(StorageSlot.class);

        new LiteralCattamand("uiuniverse", List.of(
                new OpenMenu(),
                new RegenMenu(),
                new EditMenu(),
                new PromptTest(),
                new LiteralCattamand("list", List.of(
                        new ListSlots(),
                        new ListMenus())
                )
        )).registerAsRoot(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
