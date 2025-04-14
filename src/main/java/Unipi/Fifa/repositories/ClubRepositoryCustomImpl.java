package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClubRepositoryCustomImpl implements ClubRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Club> findByTeamNameAndFifaVersion(String teamName, Integer fifaVersion) {
        // Dynamically construct the key like "merged_versions.fifa_stats_23"
        String mergedKey = "merged_versions.fifa_stats_" + fifaVersion;

        // Build the query to match the team name and the nested FIFA version
        Query query = new Query();
        query.addCriteria(Criteria.where("team_name").is(teamName));
        query.addCriteria(Criteria.where(mergedKey + ".fifa_version").is(fifaVersion));

        // Execute the query and return the result
        Club club = mongoTemplate.findOne(query, Club.class);
        return Optional.ofNullable(club);
    }
}
