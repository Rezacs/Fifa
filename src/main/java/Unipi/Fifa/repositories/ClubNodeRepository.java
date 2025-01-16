package Unipi.Fifa.repositories;

import Unipi.Fifa.models.ClubNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;

public interface ClubNodeRepository extends Neo4jRepository<ClubNode, Long> {

    List<ClubNode> findNodeById(Long Id);
    List<ClubNode> findNodeByName(String name);
}
