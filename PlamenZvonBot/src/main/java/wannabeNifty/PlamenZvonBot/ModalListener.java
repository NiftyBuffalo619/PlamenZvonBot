package wannabeNifty.PlamenZvonBot;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        switch (event.getModalId()) {
            case "calloutmodal":
                    String CalloutID = event.getValue("calloutid").getAsString();
                try {
                   // GetCallout.GetHZSJMKCalloutByID(event, CalloutID);
                } catch (Exception e) {
                    event.getHook().sendMessage("ERROR");
                    throw new RuntimeException(e);
                }
                event.deferReply().queue();
                //event.reply("Vyhledávání výjezdu s ID: " + CalloutID).queue();
                break;
            case "bugreportmodal":
                event.reply("Děkuji za zaslání bug reportu").queue();
                break;
        }
    }
}
