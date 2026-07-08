package arnett.uIUniverse.ui.dialog.types.test;

import arnett.uIUniverse.ui.dialog.PromptHelper;
import arnett.uIUniverse.ui.dialog.types.branch.BranchPrompter;
import arnett.uIUniverse.ui.dialog.types.link.LinkPrompter;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.keys.DialogKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BranchingPrompt extends BranchPrompter {
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
    public RegistryKeySet<Dialog> dialogs() {
        return RegistrySet.keySet(RegistryKey.DIALOG, DialogKeys.SERVER_LINKS, DialogKeys.QUICK_ACTIONS, DialogKeys.CUSTOM_OPTIONS);
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
