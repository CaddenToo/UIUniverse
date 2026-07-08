package arnett.uIUniverse.ui.dialog;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PromptHelper {

    //region Helper Methods

    /*=================================================================================================
                       -  Helper Methods  -
    =================================================================================================*/

    public static Player getPlayerFromAudience(Audience audience)
    {
        var id = audience.get(Identity.UUID);
        return id.map(Bukkit::getPlayer).orElse(null);
    }

    public static  UUID getUUIDFromAudience(Audience audience)
    {
        var id = audience.get(Identity.UUID);
        return id.orElse(null);
    }

    //endregion

}
