package arnett.uIUniverse.uiTypes.prompt;

import io.papermc.paper.registry.data.dialog.input.DialogInput;

import java.lang.reflect.ParameterizedType;

public abstract class PromptInput<T> {

    T value;

    public PromptInput(T defaultValue)
    {
        value = defaultValue;
    }

    /**
     * Resolves an instance of this instance's generic type
     * @param generic A generic object to resolve
     * @throws IllegalArgumentException Thrown if object is not resolvable
     */
    public T resolve(Object generic) throws ClassCastException
    {
        return (T)generic;
    }

    public abstract DialogInput inputFormat();
}
