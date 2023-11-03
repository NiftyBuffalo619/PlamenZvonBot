package wannabeNifty.PlamenZvonBot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JDA bot = JDABuilder.createDefault("")
                .setActivity(Activity.watching("Hasičské Výjezdy"))
                .addEventListeners(new CommandListener(), new ModalListener())
                .build().awaitReady();
        Guild guild = bot.getGuildById("");
        if (guild != null) {
            guild.upsertCommand("callout", "Send Callout Info by the ID provided").queue();
            guild.upsertCommand("technika", "Pošle info o zasahující technice dle ID výjezdu")
                    .addOption(OptionType.STRING, "id", "ID Výjezdu od kterého chcete techniku", true)
                    .addOption(OptionType.BOOLEAN, "senddm", "Chcete-li poslat do DM", false)
                    .queue();
            guild.upsertCommand("statistika", "Pošle statistiku pro daný čas")
                    .addOption(OptionType.INTEGER, "days", "Počet dní ze které chtete statistiku", false).queue();
            guild.upsertCommand("vyjezdy", "Vypíše výjezdy pro daný den").queue();
        }
    }
}