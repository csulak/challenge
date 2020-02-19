package meli.challenge.demo.model.country;

import java.io.Serializable;

public class Currency implements Serializable {
    private static final long serialVersionUID = -4987350379494151985L;

    private String code;
    private String name;
    private String Symbol;

    public Currency() {
    }

    public Currency(String code, String name, String symbol) {
        this.code = code;
        this.name = name;
        Symbol = symbol;
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

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }
}


