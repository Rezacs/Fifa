package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;

import org.bson.Document;

import java.util.List;
import java.util.Optional;

public interface ClubRepositoryCustom {
    Optional<Club> findByTeamNameAndFifaVersion(String teamName, Integer fifaVersion);
    List<Document> getTop10ClubsByAverageOverall();

}
