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
                for (FireDepartmentData entry : data) {
                    String onPlace;
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
                //event.replyEmbeds(builder.build()).queue();
                if (SendDM) {
                    event.getUser().openPrivateChannel().flatMap(privateChannel ->
                            privateChannel.sendMessageEmbeds(builder.build())).queue();
                            event.getHook().sendMessage("Send into DM's").queue();
                }
                else {
                    event.getHook().sendMessageEmbeds(builder.build()).queue();
                }
            }
            else if (json.length() == 2) {
                event.getHook().sendMessage("There isn't a calloud with ID: " + ID).queue();
            }
            else {
                event.getHook().sendMessage("There isn't a calloud with ID: " + ID).queue();
            }
        }
        catch (Exception e) {
            event.getHook().sendMessage("There was an error " + e.getMessage()).queue();
        }
    }
}
