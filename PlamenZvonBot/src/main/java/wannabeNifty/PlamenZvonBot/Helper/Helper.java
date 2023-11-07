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
        int VehicleIncidents = 0;
        int TechnicalHelps = 0;
        int Fires = 0;
        int LeakageOfDangerousSubstances = 0;
        int Rescues = 0;
        int Others = 0;
        int ExtraordinaryEvents = 0;
        int HighAlerts = 0;

        if (incidents == null) {
            builder.addField("Chyba při načítání výjezdů", "  ", true);
            builder.addField("Time " + StartOfTheDay + "  " + EndOfTheDay, "  ", true);
            event.getHook().sendMessage("# Výjezdy pro den " + StartOfTheDay + Helper.getDecoration() + " #").addEmbeds(builder.build()).queue();
            return;
        }

        for (FireIncident incident : incidents) {
            switch (incident.typId) {
                case "3100":
                    Fires++;
                    break;
                case "3200":
                    VehicleIncidents++;
                    break;
                case "3400":
                    LeakageOfDangerousSubstances++;
                    break;
                case "3500":
                    TechnicalHelps++;
                    break;
                case "3550":
                    Rescues++;
                    break;
                case "3700":
                    ExtraordinaryEvents++;
                    break;
                case "3600", "3800", "3900", "5000":
                    Others++;
                    break;
                case "6000":
                    HighAlerts++;
                    break;
                default:
                    Others++;
                    break;
            }
        }
        builder.addField("Celkový počet událostí: " + incidents.length, "  ", true);
        builder.addField("Mimořádné události: " + HighAlerts, "  ", true);
        builder.addField(":fire:Požáry: " + Fires, "  ", true);
        builder.addField("Dopravní Nehody: " + VehicleIncidents, "  ", true);
        builder.addField("Záchrana osob a zvířat: " + Rescues, "  ", true);
        builder.addField("Únik nebezpečných látek: " + LeakageOfDangerousSubstances, "  ", true);
        builder.addField(":wrench:Technické pomoci: " + TechnicalHelps, "  ", true);
        builder.addField("Mimořádné události: " + ExtraordinaryEvents, "  ", true);
        builder.addField("Ostatní: " + Others, "  ", true);
        /*for (FireIncident incident : incidents) {
            builder.addField(FireIncident.GetCalloutTypeById(incident.typId) + " " + FireIncident.GetCalloutBySubId(incident.podtypId)
                    , "ID: " + incident.id + " " + incident.poznamkaProMedia, true);
        }*/
        event.getHook().sendMessage("# Statistiky " + Helper.getDecoration() + " #").addEmbeds(builder.build()).queue();
    }
    public static void PrintAllDayCallouts(@NotNull SlashCommandInteractionEvent event) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String StartOfTheDay = GetCallout.ConvertDate(format.format(getStartOfCurrentDay()));
        String EndOfTheDay = GetCallout.ConvertDate(format.format(getEndOfCurrentDay()));
        FireIncident[] incidents = GetCallout.GetCalloutsFromDay(StartOfTheDay , EndOfTheDay);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDateTime.format(formatter);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Všechny výjezdy pro den " + formattedDate);
        builder.setColor(0xFC2003);
        for (FireIncident incident : incidents) {
            builder.addField(FireIncident.GetCalloutTypeById(incident.typId) + " " + FireIncident.GetCalloutBySubId(incident.podtypId),
                    ":calendar:Čas ohlášení: " + incident.casOhlaseni + "\n" + "ORP:" + incident.ORP + "\n" +
                            "ID: " + incident.id + "\n" + ":notepad_spiral:Poznámka: " + incident.poznamkaProMedia, true);
        }
        event.getHook().sendMessage("# Výjezdy " + Helper.getDecoration() + " #").addEmbeds(builder.build())
                .setEphemeral(true).queue();
    }
}
