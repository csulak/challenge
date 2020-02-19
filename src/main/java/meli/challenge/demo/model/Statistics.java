package meli.challenge.demo.model;

import meli.challenge.demo.utils.UUIDHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {
    private static final long serialVersionUID = -4987350379494141985L;

    //private static Statistics statistics = null;

    @Id
    private String statisticsId;

    private String ip;
    private Double distanceToBuenosAires;
    //private Double maxDistanceToBuenosAires;
    //private Double minDistanceToBuenosAires;


    /*
    @Autowired
    RedisTemplate<String, Statistics> redisTemplate;
*/



    public Statistics() {
    }

    /*
    //Singleton
    public static Statistics getInstance(){
        if( statistics == null ){
            statistics = new Statistics();
        }
        return statistics;
    }
*/

    public Statistics(String ip, Double distanceToBuenosAires) {
        this.statisticsId = UUIDHelper.getUuid();
        this.ip = ip;
        this.distanceToBuenosAires = distanceToBuenosAires;
    }

    public String getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getDistanceToBuenosAires() {
        return distanceToBuenosAires;
    }

    public void setDistanceToBuenosAires(Double distanceToBuenosAires) {
        this.distanceToBuenosAires = distanceToBuenosAires;
    }

    /*
    public Optional<Statistics> getSearchParams(String key) {
        try {
            ValueOperations<String, Statistics> opsForValue = redisTemplate.opsForValue();
            Optional<Statistics> params = Optional.ofNullable(opsForValue.get(key));
            if (params.isPresent()) {
                redisTemplate.expire(key, 120, TimeUnit.SECONDS);
            }
            return params;
        } catch (Exception e) {
            //log.error("Error al consultar SearchParams en Redis - Key {}", SEARCH_PARAMS_KEY + key, e);
            return Optional.empty();
        }
    }
    */

}
