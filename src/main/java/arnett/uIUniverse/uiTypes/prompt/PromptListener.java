package arnett.uIUniverse.uiTypes.prompt;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PromptListener implements Listener {

    @EventHandler
    public void onCustomButtonClick(PlayerCustomClickEvent e)
    {
        if(!(e.getCommonConnection() instanceof PlayerGameConnection connection))
        {
            return;
        }

        Player player = connection.getPlayer();

        if(e.getIdentifier().equals(Prompter.confirmationKey))
        {
            //confirmation
        }
        else if(e.getIdentifier().equals(Prompter.declinationKey))
        {
            //declination
        }
    }
}
