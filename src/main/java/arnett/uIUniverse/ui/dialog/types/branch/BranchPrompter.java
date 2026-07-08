package arnett.uIUniverse.ui.dialog.types.branch;

import arnett.uIUniverse.ui.dialog.Prompter;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.TemporalAmount;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class BranchPrompter extends Prompter {

    //region Abstract Properties

    /*=================================================================================================
                       -  Abstract Properties  -
    =================================================================================================*/

    /**
     * Called upon selection of the action button
     * @param view view of the dialog state when the action button was clicked
     * @param audience audience of the dialog
     */
    public abstract void onExit(DialogResponseView view, Audience audience);

    /**
     * @return {@code String} Text which appears on the action button when dialog is open
     */
    public abstract @NotNull String exitButtonText();

    /**
     * @return {@code String} Text which appears next to the cursor while hovering over the button
     */
    public abstract @NotNull String exitButtonHoverText();

    /**
     * @return {@code RegistryKeySet<Dialog>} the set of dialogs to display
     */
    public abstract RegistryKeySet<Dialog> dialogs();
    /**
     * @return {@code int} the number of columns to display in the dialog
     */
    public abstract int columns();
    /**
     * @return {@code int} the width of each button in the dialog
     */
    public abstract int buttonWidth();

    //endregion



    //region Defined Properties

    /*=================================================================================================
                       -  Defined Properties  -
    =================================================================================================*/

    /**
     * @return {@code int} number of uses the button's callback has before it is discarded<br>
     * default: 1
     */
    public int uses(){return 1;}

    /**
     * @return {@code TemporalAmount} if uses are not consumed, this defines the time the callback stays registerd<br>
     * default: ClickCallback.DEFAULT_LIFETIME (12 hours)
     */
    public TemporalAmount lifetime(){return ClickCallback.DEFAULT_LIFETIME;}

    /**
     * @return {@code ActionButton} the action button to exit the dialog
     */
    public ActionButton exitButton()
    {
        return ActionButton.builder(
                        MiniMessage.miniMessage().deserialize(exitButtonText())
                ).tooltip(
                        MiniMessage.miniMessage().deserialize(exitButtonHoverText())
                ).action(
                        DialogAction.customClick(
                                this::onExit,
                                ClickCallback.Options.builder()
                                        //copied from papermc docs
                                        .uses(uses()) // Set the number of uses for this callback. Defaults to 1
                                        .lifetime(lifetime()) // Set the lifetime of the callback. Defaults to 12 hours
                                        .build()
                        )
                )
                .build();
    }

    @Override
    public List<ActionButton> footerButtons() {
        return List.of(exitButton());
    }

    @Override
    protected DialogType buildType() {
        return DialogType.dialogList(
                dialogs(),
                exitButton(),
                columns(),
                buttonWidth()
        );
    }

    //endregion

}
