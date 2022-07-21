package meli.challenge.demo.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.mikael.urlbuilder.UrlBuilder;
import meli.challenge.demo.model.IpInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Component
public class IpInfoRestClient {

    @Value("${ipInfo.baseUrl}")
    private String baseUrl;

    @Value("${ipInfo.access_key}")
    private String access_key;

    @Value("${ipInfo.format}")
    private String format;

    private HttpClient client;
    private ObjectMapper mapper = new ObjectMapper();

    public IpInfoRestClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * Returns info related with the inserted Ip
     *
     * @return IpDTO
     */
    public IpInfoDTO ipInfo(String ip) {

        // TODO move this outside in order to avoid on each new call make a new instance of uri. SAME LOGIC AS countryInfoRestClient
        URI uri = UrlBuilder.empty()
                .fromString(baseUrl + ip)
                .addParameter("access_key", access_key)
                .addParameter("format", format)
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

            return mapper.readValue(response.body(), IpInfoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo info de la ip /" + ip , e);
        }
    }


}
