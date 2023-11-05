package wannabeNifty.PlamenZvonBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import wannabeNifty.PlamenZvonBot.Helper.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static wannabeNifty.PlamenZvonBot.Helper.Helper.getEndOfCurrentDay;
import static wannabeNifty.PlamenZvonBot.Helper.Helper.getStartOfCurrentDay;

public class GetCallout {
    public static void GetHZSJMKCalloutByID(@NotNull SlashCommandInteractionEvent event, @NotNull String ID, Boolean SendDM) throws Exception {
        if (ID.isEmpty()) {
            event.reply("Error ID Cannot Be Empty");
            throw new Exception("ID Cannot be Empty");
        }
        OkHttpClient client = new OkHttpClient();
        String url = "https://udalosti.firebrno.cz/api/udalosti/" + ID.trim() + "/technika";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String json = responseBody.string();
            if (response.isSuccessful() && responseBody != null) {
                if (json == null || json.length() == 2) {
                    event.getHook().sendMessage("There isn't a callout with ID: " + ID).queue();
                    return;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                EmbedBuilder builder = new EmbedBuilder();
                String Title = "Technika Empty";
               // builder.setTitle("Technika");
                builder.setColor(0xFC2003);
               // builder.setDescription(":notepad_spiral:Info\n Zde jsou informace o zasahující technice k danému výjezdu");
                builder.addField("ID Výjezdu: " + ID, "", false);
                builder.addField(":fire_engine:Vozidla/Technika", "", false);
                FireDepartmentData[] data = objectMapper.readValue(json, FireDepartmentData[].class);
                String CalloutDate = "";
                for (FireDepartmentData entry : data) {
                    String onPlace;
                    CalloutDate = entry.getReportTime();
                    if (entry.getActualQuantity() == 1)
                        onPlace = ":white_check_mark:";
                    else if (entry.getActualQuantity() == 0)
                        onPlace = ":x:";
                    else
                        onPlace = ":x:";
                    builder.addField(":fire_engine:" + entry.getUnit(), ":calendar:Čas Ohlášení: " + entry.getReportTime() + "\n" + "Typ: " +
                                    entry.getType() + "\n" + "Na místě: " + onPlace, true);
                    //event.reply(entry.getUnit() + " " + entry.getType() + " " + entry.getReportTime() + "Aktuální počet: " + entry.getActualQuantity());
                }
                //FireIncident[] incident = GetCalloutByTime(CalloutDate);
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date start = getStartOfCurrentDay();
                Date end = getEndOfCurrentDay();
                String StartOfTheDay = GetCallout.ConvertDate(format.format(start));
                String EndOfTheDay = GetCallout.ConvertDate(format.format(end));
                FireIncident[] incident = GetCalloutsFromDay(StartOfTheDay, EndOfTheDay);
                if (incident != null && incident.length > 0) {
                    for (FireIncident entry : incident) {
                        if (entry.id.equals(ID.trim())) {
                            /*builder.setTitle("Technika" + FireIncident.GetCalloutTypeById(entry.typId) + " " + FireIncident.GetCalloutBySubId(entry.podtypId))
                                    .setDescription("Stav: " + FireIncident.GetCalloutStateById(entry.stavId) + "");*/
                            //builder.addField(":notepad_spiral:Info", entry.poznamkaProMedia, true);
                            builder.setDescription(":notepad_spiral:**Info** " + entry.poznamkaProMedia);
                            Title = "Technika Výjezdu\n" + "Typ: " + FireIncident.GetCalloutTypeById(entry.typId) + " " + FireIncident.GetCalloutBySubId(entry.podtypId) + " \n" +
                                    "Stav: " + FireIncident.GetCalloutStateById(entry.stavId);
                        }
                    }
                }
                else {
                    //builder.setTitle("Technika Null");
                    Title = "Technika Null";
                }
                builder.setTitle(Title);
                //event.replyEmbeds(builder.build()).queue();
                if (SendDM) {
                    event.getUser().openPrivateChannel().flatMap(privateChannel ->
                            privateChannel.sendMessageEmbeds(builder.build())).queue();
                            event.getHook().sendMessage("Sent into DM's").queue();
                }
                else {
                    event.getHook().sendMessageEmbeds(builder.build()).queue();
                }
            }
            else {
                event.getHook().sendMessage("There isn't a callout with ID: " + ID).queue();
            }
        }
        catch (Exception e) {
            event.getHook().sendMessage("There was an error " + e.getMessage()).queue();
        }
    }
    public static FireIncident[] GetCalloutByTime(String Date) {
        // Format Like yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String formattedDate = ConvertDate(Date);
        String formattedModifiedDate = SubstractHours(formattedDate , 3);
        String Url = "https://udalosti.firebrno.cz/api/?casOd=" + formattedModifiedDate + "&casDo=" + formattedDate +
                "&krajId=116&stavIds=210&stavIds=400&stavIds=410&stavIds=420&stavIds=430&stavIds=440&stavIds=500&stavIds=510" +
                "&stavIds=520&stavIds=600&stavIds=610&stavIds=620&stavIds=700&stavIds=710&stavIds=750&stavIds=760&stavIds=780&stavIds=800";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String json = responseBody.string();
            if (response.isSuccessful() && responseBody != null) {
                ObjectMapper mapper = new ObjectMapper();
                FireIncident[] fireIncidents = mapper.readValue(json , FireIncident[].class);
                return fireIncidents;
            }
            else return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    public static FireIncident[] GetCalloutsFromDay(String StartOfTheDay , String EndOfTheDay) {
        String Url = "https://udalosti.firebrno.cz/api/?casOd=" + StartOfTheDay + "&casDo=" + EndOfTheDay +
                "&krajId=116&stavIds=210&stavIds=400&stavIds=410&stavIds=420&stavIds=430&stavIds=440&stavIds=500&stavIds=510" +
                "&stavIds=520&stavIds=600&stavIds=610&stavIds=620&stavIds=700&stavIds=710&stavIds=750&stavIds=760&stavIds=780&stavIds=800";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String json = responseBody.string();
            if (response.isSuccessful() && responseBody != null) {
                ObjectMapper mapper = new ObjectMapper();
                FireIncident[] fireIncidents = mapper.readValue(json , FireIncident[].class);
                return fireIncidents;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            FireIncident incident = new FireIncident();
            incident.id = e.getLocalizedMessage();
            FireIncident[] incidents = new FireIncident[] { incident };
            return incidents;
        }
    }

    public static String ConvertDate(String inputDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = format.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return outputFormat.format(date);
        }
        catch (ParseException e) {
            return "";
        }
    }
    private static String SubstractHours(String formattedDate, int hoursToSubstract) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = format.parse(formattedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, - hoursToSubstract);
            Date newDate = calendar.getTime();
            return format.format(newDate);
        }
        catch (ParseException e) {
            return "";
        }
    }
}
