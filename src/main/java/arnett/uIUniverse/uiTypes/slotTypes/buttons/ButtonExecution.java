package arnett.uIUniverse.uiTypes.slotTypes.buttons;

import arnett.uIUniverse.uiTypes.prompt.PromptInput;

@FunctionalInterface
interface ButtonExecution {
    void run(PromptInput<?>... inputs);
}
