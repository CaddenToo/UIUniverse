package arnett.uIUniverse.ui.dialog;


import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase.DialogAfterAction;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class Prompter {

    //region Abstract Properties

    /*=================================================================================================
                       -  Abstract Properties  -
    =================================================================================================*/

    /**
     * @return {@code boolean} Title text of this Dialog, displayed at the top along with a required
     * warning button by minecraft
     */
    public abstract @NotNull String title();

    /**
     * @return {@code boolean} Title text of this Dialog, displayed at the top along with a required
     * warning button by minecraft
     */
    public abstract List<DialogInput> inputs();

    /**
     * @return {@code List<ActionButton>} List of the footer buttons (often just the exit button),
     * this is just a formality it's not required for the Dialog but may be useful to track
     */
    public abstract List<ActionButton> footerButtons();

    //endregion



    //region Optional Properties

    /*=================================================================================================
                       -  Optional Properties  -
    =================================================================================================*/

    /**
     * @return {@code boolean} Can this dialog be closed by pressing the Esc key<br>
     * Default: true
     */
    public boolean canCloseWithEscape(){return true;}

    /**
     * @return {@code String} Fallback External title shown by buttons in ListDialog Types (BranchPrompters)<br>
     * Default: null
     */
    public String externalTitle(){return null;}

    /**
     * @return {@code DialogAfterAction} Action to be taken automatically after an input is received<br>
     * Default: DialogAfterAction.CLOSE
     */
    public DialogAfterAction afterAction() {return DialogAfterAction.CLOSE;}

    //endregion



    //region Building

    /*=================================================================================================
                       -  Building  -
    =================================================================================================*/

    /**
     * @return {@code DialogBase} The built dialog menu which can be shown to players
     */
    protected DialogBase buildBase()
    {
        String externalTitle = externalTitle();

        return DialogBase.builder(
                MiniMessage.miniMessage().deserialize(title())
        ).inputs(
                inputs()
        ).canCloseWithEscape(
                canCloseWithEscape()
        ).externalTitle(
                (externalTitle == null || externalTitle.isEmpty()) ?
                        null : MiniMessage.miniMessage().deserialize(externalTitle)
        ).afterAction(
                afterAction()
        ).build();
    }

    /**
     * @return {@code DialogType} Defined type of dialog which provides the buttons for the footer
     */
    protected abstract DialogType buildType();


    /**
     * @return {@code DialogType} Finished dialog object representing this prompter
     */
    public Dialog buildDialog()
    {
        return Dialog.create(builder -> builder.empty()
                .base(
                        buildBase()
                )
                .type(
                        buildType()
                )
        );
    }

    //endregion



    //region Prompting

    /*=================================================================================================
                       -  Prompting  -
    =================================================================================================*/

    /**
     * Shows the provided player the dialog built from this class
     * @param player Player to show dialog to
     */
    public void prompt(Player player)
    {
        player.showDialog(buildDialog());
    }


    public void prompt(List<Player> players)
    {
        Dialog builtDialog = buildDialog();
        players.forEach(player -> player.showDialog(builtDialog));
    }

    //endregion

}
