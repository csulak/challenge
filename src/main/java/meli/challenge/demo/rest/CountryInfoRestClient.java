package meli.challenge.demo.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.mikael.urlbuilder.UrlBuilder;
import meli.challenge.demo.model.country.Country;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component
public class CountryInfoRestClient {

    @Value("${countryInfo.baseUrl}")
    private String baseUrl;

    @Autowired
    private CountryInfoRestClient ipInfoRestClient;

    private HttpClient client;
    private ObjectMapper mapper = new ObjectMapper();

    public CountryInfoRestClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * return the countries information list
     *
     * @return List<Country>
     */

    /*
    @Cacheable("countries-info-cache")
    public List<Country> getAllCountriesInfo() {
        var baseURL = "https://restcountries.eu/rest/v2/all";
        URI uri = UrlBuilder.empty()
                .fromString(baseURL)
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

            return Arrays.asList(mapper.readValue(response.body(), Country[].class));
        } catch (Exception e) {
            //log.error("Error obteniendo Bumex token /{}");
            throw new RuntimeException("Error obteniendo la informacion de tods los paises /", e);
        }
    }
*/

    /**
     * return the countries information in a map
     *
     * @return Map<String,Country>
     */
    @Cacheable("countries-info-map-cache")
    public Map<String, Country> getAllCountriesInfoMap() {

        Map<String, Country> countriesInfoMap = new HashMap<>();

        String path = "/rest/v2/all";
        URI uri = UrlBuilder.empty()
                .fromString(baseUrl + path)
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

            var list = Arrays.asList(mapper.readValue(response.body(), Country[].class));

            list.forEach(country -> countriesInfoMap.put(country.getAlpha3Code(), country));


            return countriesInfoMap;
        } catch (Exception e) {
            //log.error("Error obteniendo Bumex token /{}");
            throw new RuntimeException("Error obteniendo la informacion de tods los paises /", e);
        }
    }


}
