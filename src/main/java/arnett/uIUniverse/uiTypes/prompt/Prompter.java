package arnett.uIUniverse.uiTypes.prompt;


import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Prompter {

    public static Key confirmationKey = Key.key("uiuniverse:prompter/confirm");
    public static Key declinationKey = Key.key("uiuniverse:prompter/exit");

    public static CompletableFuture<PromptInput<?>[]> prompt(PromptInput<?>... values)
    {


        Dialog prompt = Dialog.create(builder ->
                builder.empty()
                .base(
                        DialogBase.builder(
                                Component.text("Prompt")
                        )
                        .inputs(
                                List.of(

                                )
                        )
                        .build()
                )
                .type(
                        DialogType.confirmation(
                                ActionButton.builder(Component.text("Confirm", TextColor.color(0xEDC7FF)))
                                        .tooltip(Component.text("Click to use these values"))
                                        .action(DialogAction.customClick(confirmationKey, null))
                                        .build(),
                                ActionButton.builder(Component.text("Exit", TextColor.color(0xFF8B8E)))
                                        .tooltip(Component.text("Click to use the default values"))
                                        .action(DialogAction.customClick(declinationKey, null))
                                        .build()
                        )
                )
        );

        for (Object object : values)
        {
            if(object instanceof Boolean bool)
            {

            }
        }


    }
}
