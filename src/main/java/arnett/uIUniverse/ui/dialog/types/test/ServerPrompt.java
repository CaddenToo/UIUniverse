package arnett.uIUniverse.ui.dialog.types.test;

import arnett.uIUniverse.ui.dialog.PromptHelper;
import arnett.uIUniverse.ui.dialog.types.link.LinkPrompter;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerPrompt extends LinkPrompter {
    @Override
    public void onExit(DialogResponseView view, Audience audience) {
        Player player = PromptHelper.getPlayerFromAudience(audience);

        if(player != null)
        {
            player.sendMessage("oggabooga");
        }
    }

    @Override
    public @NotNull String exitButtonText() {
        return "ok";
    }

    @Override
    public @NotNull String exitButtonHoverText() {
        return "";
    }

    @Override
    public int columns() {
        return 2;
    }

    @Override
    public int buttonWidth() {
        return 150;
    }

    @Override
    public @NotNull String title() {
        return "links gere u go";
    }

    @Override
    public List<DialogInput> inputs() {
        return List.of(DialogInput.bool("text", Component.text("ag")).build());
    }
}
