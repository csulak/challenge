package meli.challenge.demo.repository;

import meli.challenge.demo.model.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRepository extends MongoRepository<Statistics, Integer> {

}
