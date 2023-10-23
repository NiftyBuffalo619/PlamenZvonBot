package wannabeNifty.PlamenZvonBot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import netscape.javascript.JSObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                ObjectMapper objectMapper = new ObjectMapper();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Technika");
                builder.setColor(0xFC2003);
                builder.setDescription("Zde jsou informace o zasahující technice k danému výjezdu");
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
                                    entry.getType() + "\n" + "Na místě: " + onPlace, false);
                    //event.reply(entry.getUnit() + " " + entry.getType() + " " + entry.getReportTime() + "Aktuální počet: " + entry.getActualQuantity());
                }
                FireIncident[] incident = GetCalloutByTime(CalloutDate);
                if (incident != null) {
                    for (FireIncident entry : incident) {
                        if (entry.id == ID) {
                            builder.setTitle("## Technika ##" + FireIncident.GetCalloutTypeById(entry.typId) + " " + FireIncident.GetCalloutBySubId(entry.podtypId))
                                    .setDescription("## Stav " + FireIncident.GetCalloutStateById(entry.stavId) + " ##");
                        }
                        else {
                            builder.setTitle("## Technika ## " + "NOID");
                        }
                    }
                }

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
            else if (json.length() == 2) {
                event.getHook().sendMessage("There isn't a callout with ID: " + ID).queue();
            }
            else if (responseBody.contentLength() == 2) {
                event.getHook().sendMessage("There isn't a callout with ID: " + ID).queue();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime(); // Before addition
        String formatedDate = dateFormat.format(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date modifiedDate = calendar.getTime();
        String formatedModifiedDate = dateFormat.format(modifiedDate);
        String Url = "https://udalosti.firebrno.cz/api/?casOd=" + formatedDate + "&casDo=" + formatedModifiedDate +
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
}
