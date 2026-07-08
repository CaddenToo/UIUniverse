package arnett.uIUniverse.ui.dialog.types.value.parameters.types;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.minimessage.MiniMessage;

@SuppressWarnings("UnstableApiUsage")
public class IntegerPromptInput extends PromptInput<Integer> {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/

    int maxValue = 256;
    int minValue = 0;
    int width = 200;

    //endregion



    //region Constructors

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public IntegerPromptInput(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    public IntegerPromptInput(String name, Integer defaultValue, int width) {
        super(name, defaultValue);
        this.width = width;
    }

    public IntegerPromptInput(String name, Integer defaultValue, int width, int minValue, int maxValue) {
        super(name, defaultValue);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.width = width;
    }

    //endregion



    //region Dialog Representation & Reading

    /*=================================================================================================
                       -  Dialog Representation & Reading  -
    =================================================================================================*/

    @Override
    public DialogInput inputFormat() {

        return DialogInput.numberRange(
                getName(), //key
                width, //display size
                MiniMessage.miniMessage().deserialize(displayName), //display name
                "options.generic_value",
                minValue, //min
                maxValue, //max
                (float)getValue(), //initial
                1f //step
        );
    }

    @Override
    public void readFromDialog(DialogResponseView view) {
        setValue(view.getFloat(getName()).intValue());
    }

    //endregion

}
