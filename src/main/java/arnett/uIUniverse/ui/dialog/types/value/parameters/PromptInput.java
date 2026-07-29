package arnett.uIUniverse.ui.dialog.types.value.parameters;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public abstract class PromptInput<T> {

    //region Fields

    /*=================================================================================================
                       -  Fields  -
    =================================================================================================*/

    /**
     * The value tracked by this class, ex// if you want to prompt a float from the player,
     * this would track that float value
     */
    protected volatile T value;

    /**
     * Name used to identify this input in Dialogs
     */
    protected String name;

    /**
     * Usually the fancier name displayed as text to the player when this is being represented
     * by a DialogInput in a Dialog
     */
    protected String displayName;

    //endregion



    //region Constructors

    /*=================================================================================================
                       -  Constructors  -
    =================================================================================================*/

    public PromptInput(String name, T defaultValue)
    {
        value = defaultValue;
        this.name = name;
        displayName = name;
    }

    //endregion



    //region Abstract Properties

    /*=================================================================================================
                       -  Abstract Properties  -
    =================================================================================================*/

    /**
     * @return {@code DialogInput} provides the DialogInput representing this PromptInput
     */
    public abstract DialogInput inputFormat();

    /**
     * Reads the DialogResponseView provided to set the value of this input to whatever
     * a DialogInput with this name as key has
     * @param view
     */
    public abstract void readFromDialog(DialogResponseView view);

    //endregion



    //region Getters

    /*=================================================================================================
                       -  Getters  -
    =================================================================================================*/

    /**
     * @return {@code T} gets the value of this input
     */
    public T getValue() {
        return value;
    }

    /**
     * @return {@code String} gets the name of this input
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@code String} gets the display name, used for the display text when shown in dialog
     */
    public String getDisplayName() {
        return displayName;
    }

    //endregion



    //region Setters

    /*=================================================================================================
                       -  Setters  -
    =================================================================================================*/

    /**
     * Sets the value
     * @param value value to set to
     */
    public void setValue(T value)
    {
        this.value = value;
    }

    /**
     * Sets the display name for any DialogInput created from this PromptInput
     * @param displayName display name, which is deserialized though MiniMessage
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Attemps to set the value of this instance from a generic object class
     * @param value generic object class, castable to these classes template type
     * @return {@code boolean} Success? True = yes, False = No
     */
    public boolean trySetValue(@Nullable Object value)
    {
        try {
            this.value = (T) value;
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
