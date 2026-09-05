package arnett.uIUniverse.commands;

import arnett.uIUniverse.ui.dialog.types.test.BranchingPrompt;
import arnett.uIUniverse.ui.dialog.types.test.DiePrompt;
import arnett.uIUniverse.ui.dialog.types.test.ServerPrompt;
import cd.arnett.caddamands.cattamands.arguments.ArgumentHelper;
import cd.arnett.caddamands.cattamands.arguments.Cattarameter;
import cd.arnett.caddamands.cattamands.cattamand.Cattamand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.entity.Player;

import java.util.List;

public class PromptTest extends Cattamand {
    @Override
    public int execute(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

        String type = commandContext.getArgument("type", String.class);
        List<Player> targets = ArgumentHelper.getPlayersFromArgs("players", commandContext);

        if(type.equals("die"))
        {
            new DiePrompt().prompt(targets);
        }
        else if(type.equals("server"))
        {
            new ServerPrompt().prompt(targets);
        }
        else if(type.equals("branch"))
        {
            new BranchingPrompt().prompt(targets);
        }



        return 0;
    }

    @Override
    public List<? extends Cattamand> getChildren() {
        return List.of();
    }

    @Override
    public List<? extends Cattarameter> getArguments() {
        return List.of(
            new Cattarameter(
                    "players",
                    ArgumentTypes.players()
            ),
            new Cattarameter(
                    "type",
                    StringArgumentType.string(),
                    List.of("die", "server", "branch")
            )
        );
    }

    @Override
    public String getName() {
        return "prompt";
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
