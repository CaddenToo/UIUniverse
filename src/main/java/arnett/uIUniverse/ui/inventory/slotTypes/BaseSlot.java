package arnett.uIUniverse.ui.inventory.slotTypes;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class BaseSlot {

    protected ItemStack definedContent;

    public BaseSlot()
    {
        setDefinedContent(ItemStack.empty());
    }

    public BaseSlot(ItemStack content)
    {
        setDefinedContent(content);
    }

    public BaseSlot(Material contentMat, int amount)
    {
        setDefinedContent(ItemStack.of(contentMat, amount));
    }

    public BaseSlot(ConfigurationSection yaml)
    {
        setDefinedContent(yaml.getItemStack(getIdentifierAsYamlKey() + ".item"));
    }

    /**
     * Called when content is changed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onSelect(Player player, InventoryView view, Inventory inventory, int slot) {}

    /**
     * @return Whether the content in this slot can be changed
     */
    public abstract boolean isMovable();

    public YamlConfiguration writeToYaml()
    {
        YamlConfiguration yaml = new YamlConfiguration();

        String key = getIdentifierAsYamlKey() + '.';

        yaml.set(key + "item", definedContent);

        return yaml;
    }

    public abstract NamespacedKey getIdentifier();

    public String getIdentifierAsYamlKey()
    {
        return getIdentifier().toString().replace(':', '-');
    }

    public ItemStack getDefinedContent()
    {
        return definedContent;
    }

    public void setDefinedContent(ItemStack stack)
    {
        this.definedContent = stack;
    }

    public DyeColor getDisplayColor()
    {
        return DyeColor.RED;
    }

    public List<TextComponent> getDisplayLore()
    {
        return List.of();
    }

    public void setContentAmount(int amount)
    {
        definedContent.setAmount(amount);
    }
}
