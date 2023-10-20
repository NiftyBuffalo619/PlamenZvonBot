package wannabeNifty.PlamenZvonBot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        TextInput InputCalloutID = TextInput.create("calloutid", "Callout ID", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Zadejte ID Výjezdu")
                .setMinLength(1)
                .setMaxLength(50)
                .setRequired(true)
                .build();
        SelectOption option = SelectOption.of("HZS Jihomoravského Kraje", "hzsjmk");
        SelectOption option2 = SelectOption.of("HZS Olomouckého Kraje", "hzsok");
        SelectOption option3 = SelectOption.of("HZS Pardubického Kraje", "hzspk");
        Modal modal = Modal.create("calloutmodal", "Výjezd")
                .addActionRows(ActionRow.of(InputCalloutID))
                .build();
        switch (event.getName()) {
            case "callout":
                event.replyModal(modal).queue();
                break;
        }
    }
}
