package arnett.uIUniverse.ui.inventory.helpers;

import org.bukkit.inventory.ItemStack;

public class ItemStackHelpers {

    /**
     * Stacks one similar item to another
     * @param from item to take from
     * @param to item to stack to
     * @return whether the to item is at max capacity
     */
    public static boolean stackSimilarItems(ItemStack from, ItemStack to)
    {
        int fromSize = from.getAmount();
        int toSize = to.getAmount();
        int combinedSize = fromSize + toSize;
        int maxSize = to.getMaxStackSize();

        to.setAmount(Math.min(maxSize, combinedSize));

        int leftover = Math.max(0, combinedSize - maxSize);
        from.setAmount(leftover);

        return to.getAmount() >= maxSize;
    }

}
