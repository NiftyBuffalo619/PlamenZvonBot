package wannabeNifty.PlamenZvonBot.Helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import wannabeNifty.PlamenZvonBot.FireIncident;
import wannabeNifty.PlamenZvonBot.GetCallout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Helper {
    public static Date getStartOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public static Date getStartOfDay(int daysBefore) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, - daysBefore);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfDay(int daysBefore) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, - daysBefore);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }
    // This method returns some emoji value for different holidays
    public static String getDecoration() {
        Date actualDate = new Date();
        int month = actualDate.getMonth();
        int day = actualDate.getDate();
        String Decoration = ":fire_engine:";
        if (month == 9) { // Starting from 0
            if (day >= 20) {
                Decoration = ":jack_o_lantern:";
            }
        } else if (month == 10) {
            if (day <= 3) {
                Decoration = ":jack_o_lantern:";
            }
        } else {
            Decoration = "";
        }
        return Decoration;
    }

    public static void DoOneDayStatistics(@NotNull SlashCommandInteractionEvent event) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String StartOfTheDay = GetCallout.ConvertDate(format.format(getStartOfCurrentDay()));
        String EndOfTheDay = GetCallout.ConvertDate(format.format(getEndOfCurrentDay()));
        FireIncident[] incidents = GetCallout.GetCalloutsFromDay(StartOfTheDay , EndOfTheDay);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDateTime.format(formatter);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Statistika pro den " + formattedDate);
        builder.setColor(0xFC2003);
        if (incidents == null) {
            builder.addField("Chyba při načítání výjezdů", "  ", true);
            builder.addField("Time " + StartOfTheDay + "  " + EndOfTheDay, "  ", true);
            event.getHook().sendMessage("# Statistiky " + Helper.getDecoration() + " #").addEmbeds(builder.build()).queue();
            return;
        }
        builder.addField("Počet událostí: " + incidents.length, "  ", true);
        for (FireIncident incident : incidents) {
            builder.addField(FireIncident.GetCalloutTypeById(incident.typId) + " " + FireIncident.GetCalloutBySubId(incident.podtypId)
                    , "ID: " + incident.id + " " + incident.poznamkaProMedia, true);
        }
        event.getHook().sendMessage("# Statistiky " + Helper.getDecoration() + " #").addEmbeds(builder.build()).queue();
    }
}
