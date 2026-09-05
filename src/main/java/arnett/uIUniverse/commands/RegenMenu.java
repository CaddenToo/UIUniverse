package arnett.uIUniverse.commands;

import arnett.uIUniverse.ui.inventory.MenuManager;
import arnett.uIUniverse.ui.inventory.UniverseInventoryHolder;
import cd.arnett.caddamands.cattamands.arguments.Cattarameter;
import cd.arnett.caddamands.cattamands.cattamand.Cattamand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.NamespacedKey;

import java.util.List;

public class RegenMenu extends Cattamand{

    @Override
    public int execute(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

        NamespacedKey key = commandContext.getArgument("menu", NamespacedKey.class);

        Class<? extends UniverseInventoryHolder> menuClass = MenuManager.registeredMenuClasses.get(key);

        boolean preloaded = MenuManager.preloadedMenuInventories.containsKey(key);

        MenuManager.registerMenu(menuClass, preloaded);

        return 1;
    }

    @Override
    public List<? extends Cattamand> getChildren() {
        return List.of();
    }

    @Override
    public List<? extends Cattarameter> getArguments() {
        return List.of(
                new Cattarameter(
                        "menu",
                        ArgumentTypes.namespacedKey(),
                        MenuManager.registeredMenuClasses.keySet().stream().map(NamespacedKey::toString).toList()
                )
        );
    }

    @Override
    public String getName() {
        return "regen";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

}
