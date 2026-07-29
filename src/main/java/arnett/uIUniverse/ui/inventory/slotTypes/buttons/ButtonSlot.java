package arnett.uIUniverse.ui.inventory.slotTypes.buttons;

import arnett.uIUniverse.ui.dialog.types.value.parameters.PromptInput;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ButtonSlot extends DisplaySlot {

    public PromptInput<?>[] parameters;

    public ButtonSlot(ItemStack displayItem)
    {
        super(displayItem);
        parameters = getDefaultParameters();
    }

    public ButtonSlot(Material contentMat, int amount)
    {
        super(contentMat, amount);
        parameters = getDefaultParameters();
    }

    public ButtonSlot()
    {
        super();
    }

    protected ButtonSlot(ConfigurationSection yaml)
    {
        //read the content
        super(yaml);

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
    }


    public abstract void execute(Player player, InventoryView view, Inventory inventory, int slot, PromptInput<?>... parameters);

    public abstract PromptInput<?>[] getDefaultParameters();


    @Override
    public void onSelect(Player player, InventoryView view, Inventory inventory, int slot) {

        //todo read parameters from yaml file or pdc of item

        execute(player, view, inventory, slot, parameters);
    }



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

    @Override
    public abstract NamespacedKey getIdentifier();

    @Override
    public List<TextComponent> getDisplayLore() {

        ArrayList<TextComponent> out = new ArrayList<>();

        for(int i = 0; i < parameters.length; i++)
        {
            String value = parameters[i].getValue().toString();

            System.out.println(parameters[i].getValue().toString());

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
}

