package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;

import java.util.Optional;

public interface ClubRepositoryCustom {
    Optional<Club> findByTeamNameAndFifaVersion(String teamName, Integer fifaVersion);
}
