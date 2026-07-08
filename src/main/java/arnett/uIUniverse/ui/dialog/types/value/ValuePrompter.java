package arnett.uIUniverse.ui.dialog.types.value;

import arnett.uIUniverse.ui.dialog.types.confirmation.ConfirmationPrompter;
import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ValuePrompter extends ConfirmationPrompter {

    //region Fields

    /*=================================================================================================
                       -  Fields  -
    =================================================================================================*/

    String title;
    List<PromptInput<?>> inputs;
    Runnable yesCallback;
    Runnable noCallback;

    //endregion



    //region Constructor

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public ValuePrompter(String title, List<PromptInput<?>> inputs, Runnable yesCallback, Runnable noCallback) {
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
        yesCallback.run();
    }

    @Override
    public final void onNo(DialogResponseView view, Audience audience) {
        noCallback.run();
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
