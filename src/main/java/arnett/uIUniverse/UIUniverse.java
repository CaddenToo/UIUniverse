package arnett.uIUniverse;

import cd.arnett.caddamands.cattamands.interpretation.Catterpreter;
import cd.arnett.caddamands.cattamands.interpretation.annotations.Catterpret;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

public final class UIUniverse extends JavaPlugin {

    public static JavaPlugin singleton;

    @Override
    public void onEnable() {
        // Plugin startup logic
        singleton = this;


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
