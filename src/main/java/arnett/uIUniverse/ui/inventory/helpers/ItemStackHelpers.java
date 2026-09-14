package arnett.uIUniverse.ui.inventory.helpers;

import org.bukkit.inventory.ItemStack;

public class ItemStackHelpers {

    public static boolean stackSimilarItems(ItemStack from, ItemStack to)
    {
        int fromSize = from.getAmount();
        int toSize = to.getAmount();
        int combinedSize = fromSize + toSize;
        int maxSize = to.getMaxStackSize();

        to.setAmount(Math.min(maxSize, combinedSize));

        int leftover = Math.max(0, combinedSize - maxSize);
        from.setAmount(leftover);

        return maxSize < to.getAmount();
    }

}
