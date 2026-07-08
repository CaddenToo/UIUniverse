package arnett.uIUniverse.ui.dialog.types.value.parameters.types;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.minimessage.MiniMessage;

@SuppressWarnings("UnstableApiUsage")
public class BooleanPromptInput extends PromptInput<Boolean> {

    //region Constructors

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public BooleanPromptInput(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    //endregion



    //region Dialog Representation & Reading

    /*=================================================================================================
                       -  Dialog Representation & Reading  -
    =================================================================================================*/

    @Override
    public DialogInput inputFormat() {
        return DialogInput.bool(
                getName(),
                MiniMessage.miniMessage().deserialize(displayName)
        ).build();
    }

    @Override
    public void readFromDialog(DialogResponseView view) {
        setValue(view.getBoolean(getName()).booleanValue());
    }

    //endregion

}
