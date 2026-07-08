package arnett.uIUniverse.ui.dialog.types.test;

import arnett.uIUniverse.ui.dialog.PromptHelper;
import arnett.uIUniverse.ui.dialog.types.confirmation.ConfirmationPrompter;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class DiePrompt extends ConfirmationPrompter {
    @Override
    public void onYes(DialogResponseView view, Audience audience) {
        Player player  = PromptHelper.getPlayerFromAudience(audience);

        if(!(
                view.getBoolean("First") &&
                view.getBoolean("Second") &&
                view.getBoolean("Third") &&
                view.getBoolean("Fourth")
                ))
        {
            player.sendMessage("You did not agree to the terms");
            return;
        }

        if(player == null)
            return;

        player.setHealth(0);
    }

    @Override
    public void onNo(DialogResponseView view, Audience audience) {
        Player player  = PromptHelper.getPlayerFromAudience(audience);

        if(player == null)
            return;

        player.sendMessage("lucky...");
    }

    @Override
    public @NotNull String yesButtonText() {
        return "<bold><gradient:#851400:#ff00aa>yes.";
    }

    @Override
    public @NotNull String noButtonText() {
        return "<bold><gradient:#1cffc2:#1f93ff>uhhh, no?";
    }

    @Override
    public @NotNull String yesButtonHoverText() {
        return "<#59c8ff>are you sure???";
    }

    @Override
    public @NotNull String noButtonHoverText() {
        return "<#00ff2f>you should probably click this";
    }

    @Override
    public @NotNull String title() {
        return "<bold><red>Die?";
    }

    @Override
    public List<DialogInput> inputs() {
        return List.of(
                DialogInput.bool("First", MiniMessage.miniMessage().deserialize("<i>Sign away <gradient:#0f8500:#1eff00>life savings</i>...")).build(),
                DialogInput.bool("Second", MiniMessage.miniMessage().deserialize("<i>Forgo last meal</i>...")).build(),
                DialogInput.bool("Third", MiniMessage.miniMessage().deserialize("<i>Breath your <red>last</red> breath</i>...")).build(),
                DialogInput.bool("Fourth", MiniMessage.miniMessage().deserialize("<i>Admit cats are <bold>better</bold> than dogs</i>...")).build()
        );
    }
}
