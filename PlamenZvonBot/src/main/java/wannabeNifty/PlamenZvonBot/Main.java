package wannabeNifty.PlamenZvonBot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JDA bot = JDABuilder.createDefault("")
                .setActivity(Activity.watching("Hasičské Výjezdy"))
                .addEventListeners(new CommandListener(), new ModalListener())
                .build().awaitReady();
        Guild guild = bot.getGuildById("");
        if (guild != null) {
            guild.upsertCommand("callout", "Send Callout Info by the ID provided").queue();
        }
    }
}