package meli.challenge.demo.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.mikael.urlbuilder.UrlBuilder;
import meli.challenge.demo.model.country.CountryCodeExchangeRates;
import meli.challenge.demo.model.country.CountryCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Component
public class CurrencyInfoRestClient {

    @Value("${fixer.baseUrl}")
    private String baseUrl;

    @Value("${fixer.key}")
    private String key;

    @Autowired
    private CurrencyInfoRestClient ipInfoRestClient;

    private HttpClient client;
    private ObjectMapper mapper = new ObjectMapper();

    public CurrencyInfoRestClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * return the country codes list
     *
     * @return List<Country>
     */
    @Cacheable("country-codes-info-cache")
    private CountryCodes AllcountryCodes() {

        String path = "/api/symbols";

        URI uri = UrlBuilder.empty()
                .fromString(baseUrl + path)
                .addParameter("access_key", key)
                .toUri();

        try {
            var request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.of(10, ChronoUnit.SECONDS))
                    .GET()
                    .build();

            var response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Respuesta invalida - response code " + response.statusCode());
            }

            return new ObjectMapper().readValue(response.body(), CountryCodes.class);
        } catch (Exception e) {
            //log.error("Error obteniendo Bumex token /{}");
            throw new RuntimeException("Error obteniendo la informacion de tods los paises /", e);
        }
    }

    /**
     * return the country codes list
     *
     * @return List<Country>
     */
    @Cacheable("country-exchange-rate-cache")
    public CountryCodeExchangeRates getCountryCodeExchangeRates() {

        String path = "/api/latest";

        var allCountryCodesInString = this.getAllCountryCodes();


        URI uri = UrlBuilder.empty()
                .fromString(baseUrl + path)
                .addParameter("access_key", key)
                .addParameter("symbols", allCountryCodesInString)
                .toUri();

        try {
            var request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.of(10, ChronoUnit.SECONDS))
                    .GET()
                    .build();

            var response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Respuesta invalida - response code " + response.statusCode());
            }

            return new ObjectMapper().readValue(response.body(), CountryCodeExchangeRates.class);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo la informacion de todos los paises /", e);
        }
    }

    private String getAllCountryCodes() {

        var countryCodesMap = this.AllcountryCodes().getSymbols();

        StringBuilder allCountryCodes = new StringBuilder();

        for (String key : countryCodesMap.keySet()){
            allCountryCodes.append(key).append(", ");
        }

        return allCountryCodes.toString();
    }


}
