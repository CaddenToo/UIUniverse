package arnett.uIUniverse.ui.dialog.types.test;

import arnett.uIUniverse.ui.dialog.PromptHelper;
import arnett.uIUniverse.ui.dialog.types.notice.NoticePrompt;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MeowPrompt extends NoticePrompt {
    @Override
    public void onAction(DialogResponseView view, Audience audience) {
        PromptHelper.getPlayerFromAudience(audience).sendMessage("meow");
    }

    @Override
    public @NotNull String actionButtonText() {
        return "meow";
    }

    @Override
    public @NotNull String actionButtonHoverText() {
        return "hoverer";
    }

    @Override
    public @NotNull String title() {
        return "mmoweowoe";
    }

    @Override
    public List<DialogInput> inputs() {
        return List.of();
    }
}
