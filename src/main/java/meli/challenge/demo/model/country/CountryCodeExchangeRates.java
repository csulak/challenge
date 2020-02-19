package meli.challenge.demo.model.country;

import java.io.Serializable;
import java.util.Map;

public class CountryCodeExchangeRates implements Serializable {
    private static final long serialVersionUID = -4981310379494151985L;

    private Boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;


    public CountryCodeExchangeRates() {
    }

    public CountryCodeExchangeRates(Boolean success, Long timestamp, String base, String date, Map<String, Double> rates) {
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}


