package meli.challenge.demo.model.country;

import java.io.Serializable;

public class CountryCurrency implements Serializable {
    private static final long serialVersionUID = -4987350379494151985L;

    private String code;
    private String name;
    private Double exchangeRateInEuro;

    public CountryCurrency() {
    }

    public CountryCurrency(String code, String name, Double exchangeRateInEuro) {
        this.code = code;
        this.name = name;
        this.exchangeRateInEuro = exchangeRateInEuro;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getExchangeRateInEuro() {
        return exchangeRateInEuro;
    }

    public void setExchangeRateInEuro(Double exchangeRateInEuro) {
        this.exchangeRateInEuro = exchangeRateInEuro;
    }
}


