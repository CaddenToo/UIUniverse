package arnett.uIUniverse.ui.dialog.types.value.parameters.types;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput.MultilineOptions;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.dialog.input.TextInput;

@SuppressWarnings("UnstableApiUsage")
public class StringPromptInput extends PromptInput<String> {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/
    int width = 200;
    int maxLength = 200;
    boolean labelVisible = true;
    MultilineOptions multilineOptions = MultilineOptions.create(1, 40);

    //endregion



    //region Constructors

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public StringPromptInput(String name, String defaultValue) {
        super(name, defaultValue);
    }

    public StringPromptInput(String name, String defaultValue, int width) {
        super(name, defaultValue);
        this.width = width;
    }

    public StringPromptInput(String name, String defaultValue, int width, boolean labelVisible) {
        super(name, defaultValue);
        this.labelVisible = labelVisible;
        this.width = width;
    }

    public StringPromptInput(String name, String defaultValue, int width, boolean labelVisible, int maxLength) {
        super(name, defaultValue);
        this.maxLength = maxLength;
        this.labelVisible = labelVisible;
        this.width = width;
    }

    public StringPromptInput(String name, String defaultValue, int width, boolean labelVisible, int maxLength, MultilineOptions multilineOptions) {
        super(name, defaultValue);
        this.maxLength = maxLength;
        this.labelVisible = labelVisible;
        this.multilineOptions = multilineOptions;
        this.width = width;
    }

    //endregion



    //region Dialog Representation & Reading

    /*=================================================================================================
                       -  Dialog Representation & Reading  -
    =================================================================================================*/

    @Override
    public DialogInput inputFormat() {
        return DialogInput.text(
                getName(),
                width,
                MiniMessage.miniMessage().deserialize(displayName),
                labelVisible,
                getValue(),
                maxLength,
                multilineOptions
        );
    }

    @Override
    public void readFromDialog(DialogResponseView view) {
        setValue(view.getText(getName()));
    }

    //endregion

}
