package meli.challenge.demo.repository;

import meli.challenge.demo.model.Statistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, String>{

    @Query(nativeQuery = true, value = "SELECT AVG(distance_to_buenos_aires) AS average, MIN(distance_to_buenos_aires) AS min, MAX(distance_to_buenos_aires) AS max, count(*) AS quantity FROM statistics")
    List<Map<String, ?>> averageDistanceToBuenosAires();

}
