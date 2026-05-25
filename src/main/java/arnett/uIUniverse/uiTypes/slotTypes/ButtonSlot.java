package arnett.uIUniverse.uiTypes.slotTypes;

import arnett.uIUniverse.uiTypes.prompt.PromptInput;
import arnett.uIUniverse.uiTypes.prompt.inputs.BooleanPromptInput;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public abstract class ButtonSlot extends BaseSlot {

    public PromptInput<?>[] parameters()
    {
        return new PromptInput[]{
                new BooleanPromptInput(false)
        };
    }

    protected abstract void execution(PromptInput<?>... parameters);

    @Override
    public void onSelect(PlayerEvent e) {

        //todo read parameters from yaml file

        execution(parameters());

    }

    @Override
    public final boolean isMovable() {
        return false;
    }


}
