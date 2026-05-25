package arnett.uIUniverse.uiTypes.slotTypes;

import org.bukkit.event.player.PlayerEvent;

public abstract class BaseSlot {

    /**
     * Called when content is changed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onSelect(PlayerEvent e) {}

    /**
     * Called when content is selected and is used to call the more general method onSelect
     */
    private void contentSelectedHandler(PlayerEvent e)
    {
        onContentSelected(e);
        onSelect(e);
    }

    /**
     * Called when content is selected and is used to call the more general method onSelect
     */
    private void contentPlacedHandler(PlayerEvent e)
    {
        onContentPlaced(e);
        onSelect(e);
    }

    /**
     * Called when content is removed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onContentSelected(PlayerEvent e) {}

    /**
     * Called when content is removed (i.e. when the player tries to move
     * the item in this slot or place a new item in)
     */
    public void onContentPlaced(PlayerEvent e) {}

    /**
     * @return Whether the content in this slot can be changed
     */
    public abstract boolean isMovable();
}
