package arnett.uIUniverse.commands.list;

import arnett.uIUniverse.ui.inventory.MenuManager;
import cd.arnett.caddamands.cattamands.arguments.ArgumentHelper;
import cd.arnett.caddamands.cattamands.arguments.Cattarameter;
import cd.arnett.caddamands.cattamands.cattamand.Cattamand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class ListActive extends Cattamand {
    @Override
    public int execute(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

        Player player = ArgumentHelper.getPlayerSender(commandContext);

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<bold>Registered Menus:</bold>"
        ));

        MenuManager.activeInventories.forEach((namespacedKey, holder) -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<italic><gray>" + namespacedKey.toString() + "<reset>"
            ));
        });

        return 1;
    }

    @Override
    public List<? extends Cattamand> getChildren() {
        return List.of();
    }

    @Override
    public List<? extends Cattarameter> getArguments() {
        return List.of(

        );
    }

    @Override
    public String getName() {
        return "active";
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
