package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;

import java.util.List;

public interface PlayerRepositoryCustom {
    List<Player> findByClubTeamIdAndGender(Integer clubTeamId, String gender);
}
