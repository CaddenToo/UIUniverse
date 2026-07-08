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

import java.util.List;

public abstract class BaseSlot {

    protected ItemStack content;

    public BaseSlot()
    {
        setContent(ItemStack.empty());
    }

    public BaseSlot(ItemStack content)
    {
        setContent(content);
    }

    public BaseSlot(Material contentMat, int amount)
    {
        setContent(ItemStack.of(contentMat, amount));
    }

    public BaseSlot(ConfigurationSection yaml)
    {
        setContent(yaml.getItemStack(getIdentifierAsYamlKey() + ".item"));
    }

    /**
     * Called when content is changed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onSelect(Player player, Inventory inventory, int slot) {}

    /**
     * @return Whether the content in this slot can be changed
     */
    public abstract boolean isMovable();

    public YamlConfiguration writeToYaml()
    {
        YamlConfiguration yaml = new YamlConfiguration();

        String key = getIdentifierAsYamlKey() + '.';

        yaml.set(key + "item", content);

        return yaml;
    }

    public abstract NamespacedKey getIdentifier();

    public String getIdentifierAsYamlKey()
    {
        return getIdentifier().toString().replace(':', '-');
    }

    public ItemStack getContent()
    {
        return content;
    }

    public void setContent(ItemStack stack)
    {
        this.content = stack;
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
        content.setAmount(amount);
    }
}
