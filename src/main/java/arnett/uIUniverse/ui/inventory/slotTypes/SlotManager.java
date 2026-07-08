package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class SlotManager {

    public static HashMap<NamespacedKey, Class<? extends BaseSlot>> registeredSlotClasses = new HashMap<>();

    public static void registerSlot(BaseSlot slot)
    {
        registeredSlotClasses.put(slot.getIdentifier(), slot.getClass());
    }

    public static void registerSlot(Class<? extends BaseSlot> slotClass) {

        try {
            BaseSlot shell = slotClass.getConstructor().newInstance();
            registeredSlotClasses.put(shell.getIdentifier(), slotClass);
        }
        catch (Exception e)
        {
            UIUniverse.logger.warning("Could not Register Slot, ensure you have a no param constructor");
            return;
        }
    }

    public static Class<? extends BaseSlot> getSlotClass(NamespacedKey identifier)
    {
        return registeredSlotClasses.get(identifier);
    }
}
