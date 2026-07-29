package arnett.uIUniverse.ui.inventory.chestInvetory;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotManager;
import arnett.uIUniverse.ui.inventory.UniverseInventoryHolder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ChestUIHolder extends UniverseInventoryHolder {

    Inventory builtInventory;

    protected ChestUIHolder(ChestUIHolder other)
    {}

    public ChestUIHolder(YamlConfiguration yaml)
    {
        if(yaml == null || yaml.getValues(false).isEmpty())
        {
            return;
        }

        int lineOn = 1;

        //load the layout
        for (; yaml.contains("line-" + (lineOn)); lineOn++)
        {
            StringBuilder line = new StringBuilder();
            ConfigurationSection lineSection = yaml.getConfigurationSection("line-" + (lineOn));

            if(lineSection == null)
                continue;

            lineSection.getValues(false).forEach((key, value) ->
            {
                line.append(value);
            });

            layout[lineOn-1] = line.toString();
        }

        //load the definitions
        ConfigurationSection definitionSection = yaml.getConfigurationSection("definitions");

        definitionSection.getValues(false).forEach((key, value) -> {
            if(!definitionSection.isConfigurationSection(key))
                return;

            ConfigurationSection slotSection = definitionSection.getConfigurationSection(key);

            String[] identifier = slotSection.getKeys(false).iterator().next().split("-");

            Class<? extends BaseSlot> slotClass = SlotManager.getSlotClass(new NamespacedKey(identifier[0], identifier[1]));

            try
            {
                BaseSlot instantiatedSlot = slotClass.getConstructor(ConfigurationSection.class).newInstance(slotSection);
                definitions.put(key.charAt(0), instantiatedSlot);
            }
            catch (Exception e)
            {
                UIUniverse.logger.warning("Unable to add slot type to definition: " + key.charAt(0));
                e.printStackTrace();
            }
        });
    }

    /**
     * Gets the slot data for a specific slot
     * @param slotNumber Slot number to check
     * @return The slot data
     * @throws IndexOutOfBoundsException Thrown if index is out of bounds
     */
    @Override
    public BaseSlot getBaseSlot(int slotNumber) throws IndexOutOfBoundsException {
        return definitions.get(layout[slotNumber / 9].charAt(slotNumber % 9));
    }

    @Override
    public @NotNull Inventory getInventory() {
        if(builtInventory == null)
        {
            UIUniverse.logger.info("Building inventory for " + getIdentifier().toString());
            builtInventory = buildInventory();
            return builtInventory;
        }
        else {
            return builtInventory;
        }
    }

    @Override
    public Inventory buildInventory() {
        //create the inventory
        Inventory inv = Bukkit.createInventory(this, layout.length * 9, MiniMessage.miniMessage().deserialize(""));

        //set content in inventory
        for (int i = 0; i < layout.length; i++)
        {
            int k;
            int offset = 9 * i;
            String line = layout[i];

            for (k = 0; k < line.length(); k++)
            {
                //we are within what was defined for this line, so get the defined slot's content
                inv.setItem(k + offset, definitions.getOrDefault(line.charAt(k), getDefaultSlot()).getDefinedContent());
            }

            //we are over what was defined for this line, so the rest gets filled with default slots
            for (; k < 9; k++)
            {
                inv.setItem(k + offset, definitions.get(' ').getDefinedContent());
            }
        }

        //return the now filled inventory
        return inv;
    }

    /**
     * Converts the default layout to Yaml data so it can be stored
     * @return converted Yaml menu
     */
    public YamlConfiguration writeToYaml() throws MatchException
    {
        YamlConfiguration yaml = new YamlConfiguration();

        StringBuilder rootKey = new StringBuilder();
        int rootKeyLength = rootKey.length();

        //write the layout
        for (int i = 0; i < layout.length; i++) {

            //append this line's key
            rootKey.append("line-").append(i+1).append('.');
            int lineKeyLength = rootKey.length();

            //process the characters in the line
            for (int k = 0; k < layout[i].length(); k++) {
                //set the slot's yaml
                yaml.set(rootKey.append(k).toString(), layout[i].charAt(k));
                //cut off that slot key
                rootKey.setLength(lineKeyLength);
            }

            //cut off the line key
            rootKey.setLength(rootKeyLength);
        }


        rootKey.append("definitions.");
        int definitionsKeyLength = rootKey.length();
        //write the definitions
        definitions.forEach((character, baseSlot) ->
        {
            //attach the slots yaml file to this section
            yaml.set(rootKey.append(character).toString(), baseSlot.writeToYaml().getValues(false));

            //cut off this character's key
            rootKey.setLength(definitionsKeyLength);
        });


        return yaml;
    }


    public char getItemKey(ItemStack comparator)
    {
        for (var entry : definitions.entrySet())
        {
            if(entry.getValue().getDefinedContent().equals(comparator))
            {
                return entry.getKey();
            }
        }

        return getAvailableCharacter();
    }
}
