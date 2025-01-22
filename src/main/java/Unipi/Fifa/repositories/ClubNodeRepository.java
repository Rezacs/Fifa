package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;

public interface ClubNodeRepository extends Neo4jRepository<ClubNode, Long> {

    List<ClubNode> findNodeByMongoId(String mongoId);
    List<ClubNode> findNodeById(Long Id);
    List<ClubNode> findNodeByTeamName(String name);
    List<ClubNode> findByGender(String gender);

    boolean existsByMongoId(String id);

    ;
}
