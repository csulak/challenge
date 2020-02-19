package meli.challenge.demo.controller;


import io.swagger.annotations.ApiOperation;
import meli.challenge.demo.model.CountryInfoComplete;
import meli.challenge.demo.model.Statistics;
import meli.challenge.demo.model.StatisticsDTO;
import meli.challenge.demo.rest.CountryInfoRestClient;
import meli.challenge.demo.rest.CurrencyInfoRestClient;
import meli.challenge.demo.rest.IpInfoRestClient;
import meli.challenge.demo.service.IpService;
import meli.challenge.demo.utils.DistanceCalculator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IpController {

    private IpInfoRestClient ipInfoRestClient;
    private CountryInfoRestClient countryInfoRestClient;
    private DistanceCalculator distanceCalculator;
    private IpService ipService;
    private CurrencyInfoRestClient currencyInfoRestClient;

    public IpController(IpInfoRestClient ipInfoRestClient, CountryInfoRestClient countryInfoRestClient, DistanceCalculator distanceCalculator, IpService ipService, CurrencyInfoRestClient currencyInfoRestClient) {
        this.ipInfoRestClient = ipInfoRestClient;
        this.countryInfoRestClient = countryInfoRestClient;
        this.distanceCalculator = distanceCalculator;
        this.ipService = ipService;
        this.currencyInfoRestClient = currencyInfoRestClient;
    }

    @ApiOperation(value = "Limpia todas las caches")
    @RequestMapping(value = "/internal/clearCache", method = RequestMethod.GET, produces = "application/json")
    public Boolean clearCache() {
        return true;
    }


    @ApiOperation(value = "Returns the country info complete for an specific IP")
    @RequestMapping(value = "/country/info", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CountryInfoComplete countryInfoComplete(@RequestParam String ip) {
        var asd = ipService.countryInfoComplete(ip);
        return asd;
    }

    @ApiOperation(value = "Retorna la info de redis")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
    public StatisticsDTO getStatistics() {
        return ipService.getStatisticsObjectByEndpoint().get();
    }



/*
    @ApiOperation(value = "Returns info related with the inserted Ip")
    @RequestMapping(value = "/info/ip", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public IpInfoDTO ipInfo(@RequestParam String ip) {
        return ipInfoRestClient.ipInfo(ip);

    }

    */

/*
    @ApiOperation(value = "Returns the whole info countries")
    @RequestMapping(value = "/info/countries", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Country> infoCountries() {
        return countryInfoRestClient.getAllCountriesInfo();
    }
*/
/*
    @ApiOperation(value = "Returns the distance between Buenos Aires and the lat and lon inserted")
    @RequestMapping(value = "/compare/distance", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Double distance(@RequestParam double lat, @RequestParam double lon) {
        return distanceCalculator.distance(lat, lon);
    }
*/
    /*
    @ApiOperation(value = "Returns the whole info countries")
    @RequestMapping(value = "/meduelelacabeza", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AAAAAAAA asdasd() {
        return currencyInfoRestClient.countryCodess();
    }
*/

}
