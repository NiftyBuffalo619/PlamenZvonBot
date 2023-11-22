package wannabeNifty.PlamenZvonBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import wannabeNifty.PlamenZvonBot.config.Config;

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
                event.getJDA().retrieveUserById(Config.AdminUserId).queue((User) -> {
                    User.openPrivateChannel().queue((privateChannel -> {
                        String BugMessage = event.getValue("bugreporttext").getAsString();
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("\t Bug Report");
                        builder.setColor(0xFC2003);
                        builder.setAuthor(event.getInteraction().getUser().getName(), null , event.getInteraction().getUser().getAvatarUrl());
                        builder.addField(":notepad_spiral:Bug Message", BugMessage, false);
                        privateChannel.sendMessageEmbeds(builder.build()).queue();
                    }));
                });
                break;
        }
    }
}
