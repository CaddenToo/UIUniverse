package arnett.uIUniverse.uiTypes.prompt.inputs;

import arnett.uIUniverse.uiTypes.prompt.PromptInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;

public class BooleanPromptInput extends PromptInput<Boolean> {
    public BooleanPromptInput(Boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public DialogInput inputFormat() {
        return null;
    }


}
