package arnett.uIUniverse.ui.inventory.slotTypes.buttons;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.DisplaySlot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ButtonSlot extends DisplaySlot {

    protected PromptInput<?>[] parameters;
    public abstract PromptInput<?>[] getDefaultParameters();


    //region Initialization

    /*=================================================================================================
                        -  Initialization  -
    =================================================================================================*/

    @Override
    public BaseSlot readFromYaml(YamlConfiguration yaml) {

        //read the content
        super.readFromYaml(yaml);

        //get the default structure of parameters (specifically the types and names)
        parameters = getDefaultParameters();

        //read the parameters
        ConfigurationSection parametersSection = yaml.getConfigurationSection(getIdentifierAsYamlKey() + ".parameters");
        for (PromptInput<?> parameter : parameters)
        {
            Object value = parametersSection.get(parameter.getName());

            if(value == null)
            {
                continue;
            }

            //will try to set and if there is an error will do nothing
            parameter.trySetValue(value);
        }

        return this;
    }


    //endregion


    //region Events

    /*=================================================================================================
                        -  Events  -
    =================================================================================================*/

    /**
     * Execution to be called upon a click. <br>
     * Disclaimer - to read/set this slot's content you have to get it from the inventory
     * @param player Player causing the execution
     * @param inventory Inventory belonging to the clicked slot
     * @param slot Slot number clicked
     * @param parameters Parameters of the button
     */
    public abstract void execute(Player player, Inventory inventory, int slot, PromptInput<?>... parameters);

    @Override
    public void onSelect(Player player, Inventory inventory, int slot) {
        execute(player, inventory, slot, parameters);
    }



    //endregion


    //region Yaml

    /*=================================================================================================
                        -  Yaml  -
    =================================================================================================*/

    @Override
    public YamlConfiguration writeToYaml() {

        //writes the base item stack
        YamlConfiguration yaml = super.writeToYaml();

        //write the parameters under parameters (in their own section so we can loop through them after)
        String key = getIdentifierAsYamlKey() + ".parameters.";
        for (PromptInput<?> parameter : parameters) {
            yaml.set(key + parameter.getName(), parameter.getValue());
        }

        return yaml;
    }

    //endregion


    //region Editor

    /*=================================================================================================
                        -  Editor  -
    =================================================================================================*/


    /**
     * @return Parameter lore seperated into a list of text components
     */
    @Override
    public List<TextComponent> getEditorLore() {

        ArrayList<TextComponent> out = new ArrayList<>();

        for(int i = 0; i < parameters.length; i++)
        {
            String value = parameters[i].getValue().toString();

            if(value.length() > 10)
            {
                value = value.substring(0, 10) + "...";
            }

            out.add(
                    Component.text(
                            parameters[i].getName() + " - " + value,
                            NamedTextColor.DARK_PURPLE,
                            TextDecoration.ITALIC
                    )
            );
        }

        return out;

    }


    //endregion
}

