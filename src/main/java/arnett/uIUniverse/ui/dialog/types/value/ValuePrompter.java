package arnett.uIUniverse.ui.dialog.types.value;

import arnett.uIUniverse.ui.dialog.types.confirmation.ConfirmationPrompter;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function3;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class ValuePrompter extends ConfirmationPrompter {

    //region Fields

    /*=================================================================================================
                       -  Fields  -
    =================================================================================================*/

    String title;
    List<PromptInput<?>> inputs;

    @FunctionalInterface
    public interface DialogCallback {
        void callback(DialogResponseView view, Audience audience);
    };

    DialogCallback yesCallback;
    DialogCallback noCallback;

    //endregion



    //region Constructor

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public ValuePrompter(String title, List<PromptInput<?>> inputs, DialogCallback yesCallback, DialogCallback noCallback) {
        this.title = title;
        this.inputs = inputs;
        this.yesCallback = yesCallback;
        this.noCallback = noCallback;
    }

    //endregion



    //region Defined Properties

    /*=================================================================================================
                       -  Defined Properties  -
    =================================================================================================*/

    @Override
    public @NotNull String title() {
        return title;
    }

    @Override
    public List<DialogInput> inputs() {
        return inputs.stream().map(PromptInput::inputFormat).toList();
    }

    @Override
    public final void onYes(DialogResponseView view, Audience audience) {

        inputs.forEach(input -> {
            input.readFromDialog(view);
        });

        if(yesCallback != null)
            yesCallback.callback(view, audience);
    }

    @Override
    public final void onNo(DialogResponseView view, Audience audience) {
        if(noCallback != null)
            noCallback.callback(view, audience);
    }

    @Override
    public @NotNull String yesButtonText() {
        return "<bold><green>Confirm";
    }

    @Override
    public @NotNull String noButtonText() {
        return "<bold><red>Discard";
    }

    @Override
    public @NotNull String yesButtonHoverText() {
        return "click to confirm inputs";
    }

    @Override
    public @NotNull String noButtonHoverText() {
        return "click to discard inputs";
    }

    //endregion

}
