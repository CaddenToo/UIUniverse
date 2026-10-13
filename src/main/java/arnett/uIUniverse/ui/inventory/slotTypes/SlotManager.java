package arnett.uIUniverse.ui.inventory.slotTypes;

import arnett.uIUniverse.UIUniverse;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class SlotManager {

    public static HashMap<NamespacedKey, Class<? extends SlotType>> registeredSlotClasses = new HashMap<>();

    public static void registerSlot(SlotType slot)
    {
        registeredSlotClasses.put(slot.getIdentifier(), slot.getClass());
    }

    public static void registerSlot(Class<? extends SlotType> slotClass) {

        try {
            SlotType shell = slotClass.getConstructor().newInstance();
            registeredSlotClasses.put(shell.getIdentifier(), slotClass);
        }
        catch (Exception e)
        {
            UIUniverse.logger.warning("Could not Register Slot, ensure you have a no param constructor");
            return;
        }
    }

    public static Class<? extends SlotType> getSlotClass(NamespacedKey identifier)
    {
        return registeredSlotClasses.get(identifier);
    }
}
