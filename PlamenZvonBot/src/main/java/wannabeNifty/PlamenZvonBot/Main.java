package wannabeNifty.PlamenZvonBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wannabeNifty.PlamenZvonBot.config.Config;

public class Main {
    public final static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws InterruptedException {
        Config.Config();
        JDA bot = JDABuilder.createDefault(Config.Token)
                .setActivity(Activity.watching("Hasičské Výjezdy"))
                .addEventListeners(new CommandListener(), new ModalListener())
                .build().awaitReady();
        Guild guild = bot.getGuildById(Config.GuildId);
        if (guild != null) {
            guild.upsertCommand("callout", "Send Callout Info by the ID provided").queue();
            guild.upsertCommand("technika", "Pošle info o zasahující technice dle ID výjezdu")
                    .addOption(OptionType.STRING, "id", "ID Výjezdu od kterého chcete techniku", true)
                    .addOption(OptionType.BOOLEAN, "senddm", "Chcete-li poslat do DM", false)
                    .queue();
            guild.upsertCommand("statistika", "Pošle statistiku pro daný čas")
                    .addOption(OptionType.INTEGER, "days", "Počet dní ze které chtete statistiku", false).queue();
            guild.upsertCommand("vyjezdy", "Vypíše výjezdy pro daný den").queue();
            guild.upsertCommand("bugreport", "Zde se dá nahlásit chyba").queue();
            guild.upsertCommand("verze", "Vypíše verzi").queue();
        }
    }
}