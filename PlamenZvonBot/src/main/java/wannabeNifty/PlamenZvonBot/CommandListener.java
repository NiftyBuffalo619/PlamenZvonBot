package wannabeNifty.PlamenZvonBot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import wannabeNifty.PlamenZvonBot.Helper.Helper;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        TextInput InputCalloutID = TextInput.create("calloutid", "Callout ID", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Zadejte ID Výjezdu")
                .setMinLength(1)
                .setMaxLength(50)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("calloutmodal", "Výjezd")
                .addActionRows(
                        ActionRow.of(InputCalloutID)
                )
                .build();
        switch (event.getName()) {
            case "technika":
                event.deferReply().queue();
                String ID = event.getOption("id").getAsString();
                Boolean SendIntoDM = false;
                if (event.getOption("senddm") != null) {
                    SendIntoDM = event.getOption("senddm").getAsBoolean();
                }
                try {
                        GetCallout.GetHZSJMKCalloutByID(event, ID, SendIntoDM);
                }
                catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Error");
                    builder.addField("There was an error", e.getMessage(), false);
                    event.getHook().sendMessage("There was an error").setEphemeral(true).queue();
                }
                break;
            case "callout":
                event.replyModal(modal).queue();
                break;
            case "statistika":
                event.deferReply().queue();
                int NumberOfDays = 0;
                try {
                    NumberOfDays = event.getOption("days").getAsInt();
                }
                catch (NullPointerException e) {
                    NumberOfDays = 1;
                }
                if (NumberOfDays > 7 || NumberOfDays < 1) {
                    event.getHook().sendMessage("Tato hodnota není podporována prosím zadejte hodnotu od 1 do 7").queue();
                    break;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String formatedStartDate = dateFormat.format(Helper.getStartOfCurrentDay());
                String formatedEndDate = dateFormat.format(Helper.getEndOfCurrentDay());

                if (NumberOfDays == 1) {
                    Main.logger.info(event.getUser().getName() + " used command /statistika for \u001B[32m" + NumberOfDays + "\u001B[0m");
                    Helper.DoOneDayStatistics(event);
                }
                else if (NumberOfDays <= 7 && NumberOfDays > 1) {
                    Main.logger.info(event.getUser().getName() + " used command /statistika for \u001B[32m" + NumberOfDays + "\u001B[0m");
                    Helper.DoMoreDaysStatistics(event, NumberOfDays);
                }
                else {
                    Main.logger.info(event.getUser().getName() + " used command /statistika for \u001B[32m" + NumberOfDays + "\u001B[0m");
                    event.getHook().sendMessage("Více dní zatím není podporováno").queue();
                }
                break;
            case "vyjezdy":
                Main.logger.info(event.getUser().getName() + " used command /vyjezdy");
                event.deferReply().queue();
                try {
                    Member member = event.getMember();
                    if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                        event.getHook().sendMessage("Nemáš oprávnění k použití tohoto příkazu");
                        break;
                    }
                } catch (NullPointerException exception) {
                    event.getHook().sendMessage("Chyba").queue();
                }
                SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String formatedStartOfDate = DateFormat.format(Helper.getStartOfCurrentDay());
                String formatedEndOfDate = DateFormat.format(Helper.getEndOfCurrentDay());
                Helper.PrintAllDayCallouts(event);
                break;
            case "bugreport":
                TextInput reportText = TextInput.create("bugreporttext", "Popis", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Zde popiš chybu")
                        .setMinLength(1)
                        .setMaxLength(250)
                        .setRequired(true)
                        .build();
                Modal BugReportModal = Modal.create("bugreportmodal", "Nahlásit Chybu")
                        .addActionRows(
                                ActionRow.of(reportText)
                        ).build();
                event.replyModal(BugReportModal).queue();
                break;
            case "verze":
                event.reply("Verze 1.0").queue();
                break;
        }
    }
}
