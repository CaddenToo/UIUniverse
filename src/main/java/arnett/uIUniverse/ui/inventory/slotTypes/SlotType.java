package arnett.uIUniverse.ui.inventory.slotTypes;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class SlotType {

    private List<ItemStack> savedContent = List.of();


    //region Initialization

    /*=================================================================================================
                        -  Initialization  -
    =================================================================================================*/

    /**
     * @return Returns the saved content provided for this index of this slot type in the layout
     * For example providing and apple and a diamond will first create an apple stack at index 1
     * and then a diamond stack at index 2 regardless of where they are placed in the layout from
     * left-right top-bottom
     */
    public ItemStack getSavedStack(int index)
    {
        return savedContent.get(index);
    }

    /**
     * @return Returns the saved content provided for this slot type
     */
    public List<ItemStack> getSavedContent()
    {
        return List.copyOf(savedContent);
    }

    /**
     * To be called along with initialization, sets the item stacks to be used for this slot types layout pattern.
     * For example providing and apple and a diamond will first create an apple stack and then a diamond stack at the
     * layout's next instance of this slot type. layout order follows left-right top-bottom
     * @param content Stacks to set to
     * @return This object (for easier initialization)
     */
    public SlotType setSavedContent(List<ItemStack> content)
    {
        this.savedContent = content;
        return this;
    }

    //region Overloads

    //----------= Overloads =-----------------------------------------------------------------------------

    /**
     * To be called along with initialization, sets the item stacks to be used for this slot types layout pattern.
     * For example providing and apple and a diamond will first create an apple stack and then a diamond stack at the
     * layout's next instance of this slot type. layout order follows left-right top-bottom
     * @param content Stack to set to
     * @return This object (for easier initialization)
     */
    public SlotType setSavedContent(ItemStack content)
    {
        this.savedContent = List.of(content);
        return this;
    }

    /**
     * To be called along with initialization, sets the item stacks to be used for this slot types layout pattern.
     * For example providing and apple and a diamond will first create an apple stack and then a diamond stack at the
     * layout's next instance of this slot type. layout order follows left-right top-bottom
     * @param mat material of the one itemstack
     * @param amount numbebr of items in that stack
     * @return This object (for easier initialization)
     */
    public SlotType setSavedContent(Material mat, int amount)
    {
        this.savedContent = List.of(ItemStack.of(mat, amount));
        return this;
    }

    //endregion



    public SlotType clone()
    {
        return clone(savedContent);
    }

    public SlotType clone(List<ItemStack> otherContent)
    {
        try {
            return getClass().getConstructor().newInstance().setSavedContent(List.copyOf(otherContent));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion


    //region Yaml

    /*=================================================================================================
                        -  Yaml  -
    =================================================================================================*/


    public SlotType readFromYaml(ConfigurationSection yaml)
    {
        ArrayList<ItemStack> content = new ArrayList<>();

        ConfigurationSection itemsSection = yaml.getConfigurationSection(getIdentifierAsYamlKey() + ".items");

        if(itemsSection == null)
            return this;

        var keys = itemsSection.getKeys(false);

        for (int i = 0; i < keys.size(); i++) {
            content.add(itemsSection.getItemStack(Integer.toString(i)));
        }

        savedContent = List.copyOf(content);

        return this;
    }

    public YamlConfiguration writeToYaml()
    {
        YamlConfiguration yaml = new YamlConfiguration();

        String key = getIdentifierAsYamlKey() + '.';

        int i = 0;
        for (ItemStack stack : savedContent) {
            yaml.set(key + "items." + i, stack);
            i++;
        }

        return yaml;
    }


    //endregion


    //region Abstract Properties

    /*=================================================================================================
                        -  Abstract Properties  -
    =================================================================================================*/

    /**
     * @return Whether the content in this slot can be changed
     */
    public abstract boolean isMovable();

    /**
     * @return Namespace used to identify this slot type
     */
    public abstract NamespacedKey getIdentifier();


    //endregion


    //region Events

    /*=================================================================================================
                        -  Events  -
    =================================================================================================*/

    /**
     * Called when content is changed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onSelect(Player player, Inventory inventory, int slot) {}

    //endregion


    //region Helpers

    /*=================================================================================================
                        -  Helpers  -
    =================================================================================================*/


    /**
     * @return A Yaml safe version of the identifier namespaced key
     */
    public String getIdentifierAsYamlKey()
    {
        return getIdentifier().toString().replace(':', '-');
    }



    //endregion


    //region Editor Representation

    /*=================================================================================================
                        -  Editor Representation  -
    =================================================================================================*/

    /**
     * @return Color used to display in Editor slot if empty (purely for vanity)
     */
    public DyeColor getEditorColor()
    {
        return DyeColor.RED;
    }

    /**
     * @return Lore to be used in editor for slot representation. For example, for the button slot, this
     * returns a list of the parameters and their values.
     */
    public List<TextComponent> getEditorLore()
    {
        return List.of();
    }

    //endregion

}
