package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Player> findByClubTeamIdAndGender(Integer clubTeamId, String gender) {
        // Create a query object
        Query query = new Query();

        // Add criteria for clubTeamId in mergedVersions
        query.addCriteria(Criteria.where("merged_versions")
                .elemMatch(Criteria.where("stats.club_team_id").is(clubTeamId))
        );

        // Add criteria for gender
        query.addCriteria(Criteria.where("gender").is(gender));

        // Execute the query and return the result
        return mongoTemplate.find(query, Player.class);
    }
}
