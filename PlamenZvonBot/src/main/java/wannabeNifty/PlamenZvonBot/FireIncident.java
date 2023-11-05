package wannabeNifty.PlamenZvonBot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class FireIncident {
    @JsonProperty("id")
    public String id;
    @JsonProperty("stavId")
    public String stavId;
    @JsonProperty("typId")
    public String typId;
    @JsonProperty("podtypId")
    public String podtypId;
    @JsonProperty("casVzniku")
    public String casVzniku;
    @JsonProperty("casOhlaseni")
    public String casOhlaseni;
    @JsonProperty("poznamkaProMedia")
    public String poznamkaProMedia;
    @JsonProperty("kraj")
    public Kraj kraj;
    @JsonProperty("okres")
    public Okres okres;
    @JsonProperty("obec")
    public String obec;
    @JsonProperty("castObce")
    public String castObce;
    @JsonProperty("ORP")
    public String ORP;
    @JsonProperty("ulice")
    public String ulice;
    @JsonProperty("gis1")
    public String gis1;
    @JsonProperty("gis2")
    public String gis2;
    @JsonProperty("zoc")
    public Boolean zoc;
    @JsonProperty("silnice")
    public String silnice;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public FireIncident() {

    }

    public static String GetCalloutTypeById(@NotNull String ID) {
        switch (ID) {
            case "3100":
                return ":fire:Požár";
            case "3200":
                return "Dopravní nehoda";
            case "3400":
                return "Únik nebezpečných látek";
            case "3500":
                return "Technická pomoc";
            case "3550":
                return "Záchrana osob a zvířat";
            case "3600":
                return "Formálně založená událost";
            case "3700":
                return "Mimořádná událost";
            case "3800":
                return "Planý poplach";
            case "3900":
                return "Jiné zatím neurčeno";
            case "5000":
                return "Událost na objekt";
            case "6000":
                return "Pohotovost";
            default:
                return "Jiné zatím neurčeno";
        }
    }
    public static String GetCalloutBySubId(@NotNull String SubId) {
        switch (SubId) {
            case "3401":
                return "Na pozemní komunikaci";
            case "10020":
                return "Na určené požární stanici";
            case "3101":
                return "Nízké budovy";
            case "3811":
                return "Planý poplach";
            case "3611":
                return ":radioactive:Radiační nehoda, havárie";
            case "3211":
                return "Vyproštění osob";
            case "3522":
                return "Z Výšky";
            case "3911":
                return "Zatím neurčeno";
            case "3602":
                return "Živelná pohroma";
            case "3402":
                return "Do půdy";
            case "3711":
                return "EVAKUACE A OCHRANA OBYVATEL PLOŠNÁ";
            case "3603":
                return "HUMANITÁRNÍ POMOC";
            case "10019":
                return "Na vlastní požární zbrojnici";
            case "3505":
                return ":evergreen_tree:Odstranění stromu";
            case "3214":
                return ":stethoscope:Se zraněním";
            case "3921":
                return "TECHNOLOGICKÝ TEST";
            case "3212":
                return "Uvolnění komunikace, odtažení";
            case "3102":
                return "VÝŠKOVÉ BUDOVY";
            case "3529":
                return "Z HLOUBKY";
            case "3712":
                return "JINÉ";
            case "3403":
                return "NA (DO) VODNÍ PLOCHU(Y)";
            case "3601":
                return "OSTATNÍ FORMÁLNĚ ZALOŽENÁ UDÁLOST";
            case "3103":
                return "PRŮMYSLOVÉ,ZEMĚDĚLSKÉ OBJEKTY,SKLADY";
            case "3213":
                return ":broom:ÚKLID VOZOVKY";
            case "3523":
                return "UZAVŘENÉ PROSTORY, VÝTAH";
            case "3931":
                return "ZLOMYSLNÉ VOLÁNÍ";
            case "3404":
                return "DO OVZDUŠÍ";
            case "10014":
                return ":fire:Požár";
            case "10001":
                return "Signalizace EPS";
            case "10015":
                return "Větrná smršť";
            case "3231":
                return "ŽELEZNIČNÍ";
            case "3530":
                return "AED";
            case "3241":
                return "LETECKÁ";
            case "10016":
                return "Povodeň";
            case "3104":
                return "SHROMAŽDIŠTĚ OSOB";
            case "3110":
                return "LESNÍ POROST";
            case "3105":
                return "PODZEMNÍ PROSTORY,TUNELY";
            case "3524":
                return "ZASYPANÉ,ZAVALENÉ";
            case "3521":
                return "Z VODY";
            case "3106":
                return "POLNÍ POROST, TRÁVA";
            case "3502":
                return "SPOLUPRÁCE SE SLOŽKAMI IZS";
            case "3542":
                return "LIKVIDACE OBTÍŽNÉHO HMYZU";
            case "3111":
                return "ODPAD, OSTATNÍ";
            case "3543":
                return "TRANSPORT PACIENTA";
            case "3107":
                return "TRAFOSTANICE, ROZVODNY";
            case "3108":
                return "DOPRAVNÍ PROSTŘEDKY";
            case "3109":
                return "POPELNICE, KONTEJNER";
            case "3503":
                return ":house_abandoned:Destrukce objektu";
            case "3504":
                return "NÁHRADA NEFUNKČNÍHO ZAŘÍZENÍ";
            case "3525":
                return "OTEVŘENÍ UZAVŘENÝCH PROSTOR";
            case "3501":
                return "ODSTRANĚNÍ NEBEZPEČNÝCH STAVŮ";
            case "10024":
                return "Činnost jednotky";
            case "3526":
                return "ODSTRAŇOVÁNÍ PŘEKÁŽEK";
            case "3527":
                return "ČERPÁNÍ VODY";
            case "3528":
                return "MĚŘENÍ KONCENTRACÍ";
            case "3541":
                return "MONITORING";
            default:
                return "";
        }
    }
    public static String GetCalloutStateById(@NotNull String StateId) {
        switch (StateId) {
            case "400":
                return "Bez SaP";
            case "110":
                return "Čekající - přečteno";
            case "100":
                return "Čekající na odbavení";
            case "200":
                return "Inicializovaná";
            case "440":
                return "Likvidovaná";
            case "430":
                return "Lokalizovaná";
            case "450":
                return "Odjezd poslední jednotky";
            case "300":
                return "Odložená";
            case "120":
                return "Odmítnuto";
            case "210":
                return "Převzatá";
            case "410":
                return "SaP na cestě";
            case "420":
                return "SaP na místě";
            case "700":
            case "710":
            case "800":
            case "520":
            case "500":
            case "750":
            case "760":
            case "780":
            case "620":
            case "510":
            case "600":
            case "610":
                return "Ukončená";
            case "130":
                return "Zapracováno k řešené události";
            default:
                return "";
        }
    }
    public static class Kraj {
        @JsonProperty("id")
        private int id;
        @JsonProperty("nazev")
        private String nazev;
    }
    public static class Okres {
        @JsonProperty("id")
        private int id;
        @JsonProperty("nazev")
        private String nazev;
    }
}