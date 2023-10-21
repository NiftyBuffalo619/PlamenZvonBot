package wannabeNifty.PlamenZvonBot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FireDepartmentData {
    @JsonProperty("typ")
    private String typ;
    @JsonProperty("jednotka")
    private String jednotka;
    @JsonProperty("pocet")
    private int pocet;
    @JsonProperty("aktualniPocet")
    private int aktualniPocet;
    @JsonProperty("casOhlaseni")
    private String casOhlaseni;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public FireDepartmentData() {

    }

    public String getType() {
        return this.typ;
    }
    public String getUnit() {
        return this.jednotka;
    }
    public int getQuantity() {
        return this.pocet;
    }
    public int getActualQuantity() {
        return this.aktualniPocet;
    }
    public String getReportTime() {
        return this.casOhlaseni;
    }
}
