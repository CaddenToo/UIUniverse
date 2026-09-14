package arnett.uIUniverse.ui.inventory.slotTypes;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class BaseSlot {

    private ItemStack initialContent = ItemStack.empty();


    //region Initialization

    /*=================================================================================================
                        -  Initialization  -
    =================================================================================================*/

    /**
     * @return Returns the inital content provided for this slot, DOES NOT HOLD WHAT IS CURRENTLY
     * IN THIS SLOT IN THE INVENTORY
     */
    public ItemStack getInitialContent()
    {
        return initialContent;
    }

    /**
     * To be called along with initialization, sets the itemStack to be in this slot's location.
     * All overloads of this call this function.
     * @param stack Stack to set to
     * @return This object (for easier initialization)
     */
    public BaseSlot setInitialContent(ItemStack stack)
    {
        this.initialContent = stack;
        return this;
    }

    /**
     * To be called along with initialization, sets the itemStack to be in this slot's location
     * @param contentMat material of the stack
     * @param amount amount to set the stack to
     * @return This object (for easier initialization)
     */
    public BaseSlot setInitialContent(Material contentMat, int amount)
    {
        setInitialContent(ItemStack.of(contentMat, amount));
        return this;
    }


    public BaseSlot clone()
    {
        return clone(initialContent);
    }

    public BaseSlot clone(ItemStack otherContent)
    {
        try {
            return getClass().getConstructor().newInstance().setInitialContent(otherContent);
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


    public BaseSlot readFromYaml(YamlConfiguration yaml)
    {
        initialContent = yaml.getItemStack(getIdentifierAsYamlKey() + ".item");
        return this;
    }

    public YamlConfiguration writeToYaml()
    {
        YamlConfiguration yaml = new YamlConfiguration();

        String key = getIdentifierAsYamlKey() + '.';

        yaml.set(key + "item", initialContent);

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
