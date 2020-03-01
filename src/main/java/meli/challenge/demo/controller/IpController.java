package meli.challenge.demo.controller;


import io.swagger.annotations.ApiOperation;
import meli.challenge.demo.model.CountryInfoComplete;
import meli.challenge.demo.model.StatisticsDTO;
import meli.challenge.demo.service.IpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IpController {


    private IpService ipService;

    public IpController(IpService ipService) {
        this.ipService = ipService;
    }

    @ApiOperation(value = "Limpia todas las caches de memoria")
    @RequestMapping(value = "/internal/clearMemoryCache", method = RequestMethod.POST, produces = "application/json")
    public void clearCache() {
        ipService.clearCache();
    }


    @ApiOperation(value = "Retorna las estadisticas")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
    public StatisticsDTO getStatistics() {
        return ipService.getStatisticsObjectByEndpoint().orElse(null);
    }

    @ApiOperation(value = "Returns the country info complete for an specific IP")
    @RequestMapping(value = "/country/info", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CountryInfoComplete countryInfoComplete(@RequestParam String ip) {
        return ipService.countryInfoComplete(ip);
    }


}
