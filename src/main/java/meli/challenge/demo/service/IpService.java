package meli.challenge.demo.service;

import meli.challenge.demo.model.CountryInfoComplete;
import meli.challenge.demo.model.StatisticsDTO;
import meli.challenge.demo.model.country.CountryCurrency;
import meli.challenge.demo.model.country.Currency;
import meli.challenge.demo.model.Statistics;
import meli.challenge.demo.repository.StatisticsRepository;
import meli.challenge.demo.rest.CountryInfoRestClient;
import meli.challenge.demo.rest.CurrencyInfoRestClient;
import meli.challenge.demo.rest.IpInfoRestClient;
import meli.challenge.demo.utils.DistanceCalculator;
import meli.challenge.demo.utils.ValidateIpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static meli.challenge.demo.utils.Constants.COUNTRIES_INFO_MAP_CACHE;
import static meli.challenge.demo.utils.Constants.COUNTRY_CODES_INFO_CACHE;
import static meli.challenge.demo.utils.Constants.COUNTRY_EXCHANGE_RATE_CACHE;

@Service
public class IpService {

    @Autowired
    CountryInfoRestClient countryInfoRestClient;

    @Autowired
    IpInfoRestClient ipInfoRestClient;

    @Autowired
    CurrencyInfoRestClient currencyInfoRestClient;

    @Autowired
    RedisTemplate<String, StatisticsDTO> redisTemplate;

    @Autowired
    private StatisticsRepository repository;

    public CountryInfoComplete countryInfoComplete(String ip) {

        if(!ValidateIpAddress.validateIPAddress(ip)){
            throw new RuntimeException("La ip ingresada no posee el patron 'XXX.XXX.XXX.XXX' ");
        }

        var ipInfo = ipInfoRestClient.ipInfo(ip);
        var countriesInfoMap = countryInfoRestClient.getAllCountriesInfoMap();

        var countryInfo = countriesInfoMap.get(ipInfo.getCountryCode3());

        LocalDateTime localDateTimeInUTC = LocalDateTime.now();

        var currentTimes = this.setCurrentTimes(countryInfo.getTimezones(), localDateTimeInUTC);

        var distanceBetweenBuenosAiresToThisCountryInKm = DistanceCalculator.distance(countryInfo.getLatlng().get(0), countryInfo.getLatlng().get(1));

        var countryCurrencies = this.getCountryCurrencies(countryInfo.getCurrencies());

        CountryInfoComplete countryInfoComplete = new CountryInfoComplete(
                ip,
                countryInfo.getName(),
                countryInfo.getAlpha2Code(),
                countryInfo.getAlpha3Code(),
                countryInfo.getLanguages(),
                localDateTimeInUTC,
                currentTimes,
                distanceBetweenBuenosAiresToThisCountryInKm,
                countryCurrencies);


        var statisticsMongo = new Statistics(ip, distanceBetweenBuenosAiresToThisCountryInKm);


        repository.save(statisticsMongo);

        // obtengo el objeto de redis y si no existe lo trae de mongo
        var statisticsToRedis = this.getStatisticsObject(countryInfoComplete.getDistanceBetweenBuenosAiresToThisCountryInKm());

        //guardo el objeto en redis
        this.addStatisticsObjectToRedis(statisticsToRedis.get());

        return countryInfoComplete;
    }


    private List<CountryCurrency> getCountryCurrencies(List<Currency> countryInfoCurrency) {

        var exchangesRateInEuroComplete = currencyInfoRestClient.getCountryCodeExchangeRates().getRates();
        //List<CountryCurrency> countryCurrencies = new ArrayList<>();


        var countryCurrencies = countryInfoCurrency.stream().map(currency -> new CountryCurrency(
                currency.getCode(),
                currency.getName(),
                exchangesRateInEuroComplete.getOrDefault(currency.getCode(), -1D))).collect(Collectors.toList());

        /*
        countryInfoCurrency.forEach(currency ->
                countryCurrencies.add(new CountryCurrency(
                        currency.getCode(),
                        currency.getName(),
                        exchangesRateInEuroComplete.getOrDefault(currency.getCode(), -1D))
                ));
*/

        return countryCurrencies;
    }

    private List<LocalDateTime> setCurrentTimes(List<String> timeZones, LocalDateTime localDateTimeInUTC) {
        List<LocalDateTime> currentTimes = new ArrayList<>();

        var timeZonesWithouthUTC = timeZones.stream().map(s -> s.replace("UTC", "").replace(":00", "")).collect(Collectors.toList());

        var timeZonesParsedToLong = timeZonesWithouthUTC.stream().map(Long::parseLong).collect(Collectors.toList());

        timeZonesParsedToLong.forEach(timeZone -> currentTimes.add(localDateTimeInUTC.plusHours(timeZone)));
        return currentTimes;
    }


    private void addStatisticsObjectToRedis(StatisticsDTO params) {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            opsForValue.set("STATISTICS_REDIS", params, 900, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }


    private Optional<StatisticsDTO> getStatisticsObject(Double distanceBetweenBuenosAiresToThisCountryInKm) {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            Optional<StatisticsDTO> params = Optional.ofNullable(opsForValue.get("STATISTICS_REDIS"));
            if (params.isEmpty() || params.get().getMaxDistanceToBuenosAires() == null) {

                // traigo de mongo todos los documentos que haya TODO esto se debe trabjar por query
                var listBooks = repository.findAll();

                // trabajo toda la info en memoria TODO esto se debe trabjar por query
                var listNums = listBooks.stream().map( l -> l.getDistanceToBuenosAires()).collect(Collectors.toList());;

                var minimo = listNums.stream().min(Double::compare);
                var maximo = listNums.stream().max(Double::compare);
                var quantity = listNums.size();
                var avg = listNums.stream().mapToDouble(Double::doubleValue).sum() / quantity;

                var satisticsDTO = new StatisticsDTO(avg, maximo.orElse(null), minimo.orElse(null), quantity);

                params = Optional.of(satisticsDTO);

                this.addStatisticsObjectToRedis(satisticsDTO);

            }
            else{

                // aca actualizo los valores del objeto de redis. esto lo hago en el caso de que ya exista en redis
                // porque los valores van a ser "viejos" por estar cacheados y no "nuevos" por ser traidos de sql
                var statisticsUpdated = this.updateStatistics(params.get(), distanceBetweenBuenosAiresToThisCountryInKm);
                params = Optional.of(statisticsUpdated);

            }

            return params;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private StatisticsDTO updateStatistics(StatisticsDTO statisticsToRedis, Double distanceBetweenBuenosAiresToThisCountryInKm) {

        var maxDistance = statisticsToRedis.getMaxDistanceToBuenosAires() > distanceBetweenBuenosAiresToThisCountryInKm ? statisticsToRedis.getMaxDistanceToBuenosAires() : distanceBetweenBuenosAiresToThisCountryInKm;
        var minDistance = statisticsToRedis.getMinDistanceToBuenosAires() < distanceBetweenBuenosAiresToThisCountryInKm ? statisticsToRedis.getMinDistanceToBuenosAires() : distanceBetweenBuenosAiresToThisCountryInKm;
        var sumDistances = statisticsToRedis.getAverage() * statisticsToRedis.getQuantity() + distanceBetweenBuenosAiresToThisCountryInKm;
        var quantity = statisticsToRedis.getQuantity() + 1;

        var average = sumDistances / quantity;
        return new StatisticsDTO(average, maxDistance, minDistance, quantity);

    }


    public Optional<StatisticsDTO> getStatisticsObjectByEndpoint() {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            Optional<StatisticsDTO> params = Optional.ofNullable(opsForValue.get("STATISTICS_REDIS"));
            if (params.isEmpty()) {

                // traigo de mongo todos los documentos que haya TODO esto se debe trabjar por query
                var listBooks = repository.findAll();

                // trabajo toda la info en memoria TODO esto se debe trabjar por query
                var listNums = listBooks.stream().map( l -> l.getDistanceToBuenosAires()).collect(Collectors.toList());;

                var minimo = listNums.stream().min(Double::compare);
                var maximo = listNums.stream().max(Double::compare);
                var quantity = listNums.size();
                var avg = listNums.stream().mapToDouble(Double::doubleValue).sum() / quantity;

                var satisticsDTO = new StatisticsDTO(avg, maximo.orElse(null), minimo.orElse(null), quantity);

                params = Optional.of(satisticsDTO);

                this.addStatisticsObjectToRedis(satisticsDTO);

            }
            return params;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo statistics /", e);
        }
    }

    /**
     * Metodo para limpiar todas las caches
     */
    @CacheEvict(allEntries = true, value = {COUNTRY_CODES_INFO_CACHE, COUNTRY_EXCHANGE_RATE_CACHE, COUNTRIES_INFO_MAP_CACHE})
    public void clearCache() {
        System.out.println("Flush Memory Cache by endpoint " + new Date());
    }

}
