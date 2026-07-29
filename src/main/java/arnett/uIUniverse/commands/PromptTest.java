package arnett.uIUniverse.commands;

import arnett.uIUniverse.ui.dialog.PromptHelper;
import arnett.uIUniverse.ui.dialog.types.test.BranchingPrompt;
import arnett.uIUniverse.ui.dialog.types.test.DiePrompt;
import arnett.uIUniverse.ui.dialog.types.test.MeowPrompt;
import arnett.uIUniverse.ui.dialog.types.test.ServerPrompt;
import arnett.uIUniverse.ui.dialog.types.value.ValuePrompter;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.dialog.types.value.parameters.types.BooleanPromptInput;
import arnett.uIUniverse.ui.dialog.types.value.parameters.types.FloatPromptInput;
import arnett.uIUniverse.ui.dialog.types.value.parameters.types.IntegerPromptInput;
import arnett.uIUniverse.ui.dialog.types.value.parameters.types.StringPromptInput;
import cd.arnett.caddamands.cattamands.arguments.ArgumentHelper;
import cd.arnett.caddamands.cattamands.arguments.Cattarameter;
import cd.arnett.caddamands.cattamands.cattamand.Cattamand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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
        else if(type.equals("meow"))
        {
            new MeowPrompt().prompt(targets);
        }
        else if(type.equals("value"))
        {
            List<PromptInput<?>> parameters = List.of(
                    new BooleanPromptInput("red", false),
                    new StringPromptInput("string", "text"),
                    new FloatPromptInput("teleport", 1f),
                    new IntegerPromptInput("ints", 4)
            );

            new ValuePrompter(
                    "Value test",
                    parameters,
                    (view, audience) -> {
                        boolean red = view.getBoolean("red").booleanValue();
                        String string = view.getText("string").toString();
                        float teleport = view.getFloat("teleport").floatValue();
                        int ints = view.getFloat("ints").intValue();

                        Player player = PromptHelper.getPlayerFromAudience(audience);

                        for (int i = 0; i < ints; i++)
                        {
                            player.sendMessage(
                                    MiniMessage.miniMessage().deserialize((red ? "<red>" : "") + string)
                            );
                        }

                        player.teleport(player.getLocation().clone().add(new Vector(0, teleport, 0)));
                    },
                    (view, audience) -> {
                        PromptHelper.getPlayerFromAudience(audience).sendMessage("yeee");
                    }
            ).prompt(targets);
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
            Cattarameter.of(
                    "players",
                    ArgumentTypes.players()
            ),
            Cattarameter.of(
                    "type",
                    StringArgumentType.string(),
                    List.of("die", "server", "branch", "value", "meow")
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
