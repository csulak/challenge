package meli.challenge.demo.model.country;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Language implements Serializable {
    private static final long serialVersionUID = -4987350379494181985L;

    @JsonProperty("iso639_1")
    private String iso639_1;

    @JsonProperty("iso639_2")
    private String iso639_2;
    private String name;

    @JsonProperty("nativeName")
    private String nativeName;

    public Language() {
    }

    public Language(String iso639_1, String iso639_2, String name, String nativeName) {
        this.iso639_1 = iso639_1;
        this.iso639_2 = iso639_2;
        this.name = name;
        this.nativeName = nativeName;
    }

    public String getIso639_1() {
        return iso639_1;
    }

    public void setIso639_1(String iso639_1) {
        this.iso639_1 = iso639_1;
    }

    public String getIso639_2() {
        return iso639_2;
    }

    public void setIso639_2(String iso639_2) {
        this.iso639_2 = iso639_2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
}


