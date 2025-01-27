package Unipi.Fifa.highQueryServices;// MongoDB Query: Group players by FIFA version and count them
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlayerAggregationService {

    private final MongoTemplate mongoTemplate;

    public PlayerAggregationService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void aggregatePlayersByFifaVersion() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("fifaVersion").count().as("playerCount"),
                Aggregation.project("_id", "playerCount")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "Players", Map.class);

        results.getMappedResults().forEach(result -> {
            System.out.println("FIFA Version: " + result.get("_id") + ", Count: " + result.get("playerCount"));
        });
    }
}