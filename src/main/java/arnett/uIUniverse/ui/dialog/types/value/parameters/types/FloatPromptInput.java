package arnett.uIUniverse.ui.dialog.types.value.parameters.types;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class FloatPromptInput extends PromptInput<Float> {

    //region Properties

    /*=================================================================================================
                       -  Properties  -
    =================================================================================================*/

    float maxValue = 256;
    float minValue = 0;
    int width = 200;
    float step = .25f;

    //endregion



    //region Constructors

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public FloatPromptInput(String name, Float defaultValue) {
        super(name, defaultValue);
    }

    public FloatPromptInput(String name, Float defaultValue, float stepSize) {
        super(name, defaultValue);
        this.step = stepSize;
    }

    public FloatPromptInput(String name, Float defaultValue, float stepSize, int width) {
        super(name, defaultValue);
        this.width = width;
        this.step = stepSize;
    }

    public FloatPromptInput(String name, Float defaultValue, float minValue, float maxValue) {
        super(name, defaultValue);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public FloatPromptInput(Float defaultValue, String name, float stepSize, int width, float minValue, float maxValue) {
        super(name, defaultValue);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.width = width;
        this.step = stepSize;
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
                step //step
        );
    }

    @Override
    public void readFromDialog(DialogResponseView view) {
        setValue(view.getFloat(getName()).floatValue());
    }

    //endregion



    //region Getting & Setting

    /*=================================================================================================
                       -  Getting & Setting  -
    =================================================================================================*/

    @Override
    public boolean trySetValue(@Nullable Object value) {

        //expects a Double here which doesn't directly cast to Float
        try {
            this.value = (float)(double)value;
        }
        catch (Exception e)
        {
            return false;
        }

        //successful cast
        return true;
    }

    //endregion

}
