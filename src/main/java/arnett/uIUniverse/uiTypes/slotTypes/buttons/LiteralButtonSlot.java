package arnett.uIUniverse.uiTypes.slotTypes.buttons;

import arnett.uIUniverse.uiTypes.prompt.PromptInput;
import arnett.uIUniverse.uiTypes.prompt.inputs.BooleanPromptInput;
import arnett.uIUniverse.uiTypes.slotTypes.BaseSlot;
import org.bukkit.event.player.PlayerEvent;

public class LiteralButtonSlot extends BaseSlot {

    //region Parameters

    /*=================================================================================================
                       -  Parameters  -
    =================================================================================================*/

    public ButtonExecution execution;
    public PromptInput<?>[] parameters;

    //endregion


    //region Constructor

    /*=================================================================================================
                       -  Constructor  -
    =================================================================================================*/

    public LiteralButtonSlot(ButtonExecution execution, PromptInput<?>... parameters)
    {
        this.execution = execution;
        this.parameters = parameters;
    }

    //endregion




    @Override
    public void onSelect(PlayerEvent e) {

        //todo read parameters from yaml file or pdc of item

        execution.run(parameters);

    }

    @Override
    public final boolean isMovable() {
        return false;
    }


}

