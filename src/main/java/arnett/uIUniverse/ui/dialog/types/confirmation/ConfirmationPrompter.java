package arnett.uIUniverse.ui.dialog.types.confirmation;

import arnett.uIUniverse.ui.dialog.Prompter;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.TemporalAmount;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class ConfirmationPrompter extends Prompter {

    //region Buttons

    /*=================================================================================================
                       -  Buttons  -
    =================================================================================================*/

    /**
     * @return {@code ActionButton} A build of the button which serves as the 'yes' option at the footer
     */
    public ActionButton buildYesButton()
    {
        return ActionButton.builder(
                        MiniMessage.miniMessage().deserialize(yesButtonText())
                ).tooltip(
                        MiniMessage.miniMessage().deserialize(yesButtonHoverText())
                ).action(
                        DialogAction.customClick(
                                this::onYes,
                                ClickCallback.Options.builder()
                                        //copied from papermc docs
                                        .uses(uses()) // Set the number of uses for this callback. Defaults to 1
                                        .lifetime(lifetime()) // Set the lifetime of the callback. Defaults to 12 hours
                                        .build()
                        )
                )
                .build();
    }

    /**
     * @return {@code ActionButton} A build of the button which serves as the 'no' option at the footer
     */
    public ActionButton buildNoButton()
    {
        return ActionButton.builder(
                        MiniMessage.miniMessage().deserialize(noButtonText())
                ).tooltip(
                        MiniMessage.miniMessage().deserialize(noButtonHoverText())
                ).action(
                        DialogAction.customClick(
                                this::onNo,
                                ClickCallback.Options.builder()
                                        //copied from papermc docs
                                        .uses(uses()) // Set the number of uses for this callback. Defaults to 1
                                        .lifetime(lifetime()) // Set the lifetime of the callback. Defaults to 12 hours
                                        .build()
                        )
                )
                .build();
    }

    //endregion



    //region Abstract properties

    /*=================================================================================================
                       -  Abstract properties  -
    =================================================================================================*/

    /**
     * Called upon selection of the yes button
     * @param view view of the dialog state when 'yes' was clicked
     * @param audience audience of the dialog
     */
    public abstract void onYes(DialogResponseView view, Audience audience);


    /**
     * Called upon selection of the no button
     * @param view view of the dialog state when 'no' was clicked
     * @param audience audience of the dialog
     */
    public abstract void onNo(DialogResponseView view, Audience audience);

    /**
     * @return {@code String} Text which appears on the button when dialog is open
     */
    public abstract @NotNull String yesButtonText();

    /**
     * @return {@code String} Text which appears on the button when dialog is open
     */
    public abstract @NotNull String noButtonText();


    /**
     * @return {@code String} Text which appears next to the cursor while hovering over the button
     */
    public abstract @NotNull String yesButtonHoverText();

    /**
     * @return {@code String} Text which appears next to the cursor while hovering over the button
     */
    public abstract @NotNull String noButtonHoverText();

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
     * @return {@code List<ActionButton>} List of all buttons present at the footer (mostly for formality)
     */
    @Override
    public final List<ActionButton> footerButtons() {
        return List.of(buildYesButton(), buildNoButton());
    }

    @Override
    protected final DialogType buildType() {
        return DialogType.confirmation(
                buildYesButton(),
                buildNoButton()
        );
    }

    //endregion


}
