package meli.challenge.demo.model;

import java.io.Serializable;

public class IpInfoDTO implements Serializable {
    private static final long serialVersionUID = -4987350379494151985L;


    private String countryCode;
    private String countryName;

    public IpInfoDTO() {
    }

    public IpInfoDTO(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
