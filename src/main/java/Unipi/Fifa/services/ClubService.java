package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    @Autowired
    private final ClubRepository clubRepository;
    @Autowired
    private ClubNodeRepository clubNodeRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }



    public String saveClub(Club club) {
        return clubRepository.save(club).getId();
    }

    public Club getClubbyId(String id) {
        return clubRepository.findById(id).orElse(null);
    }

    public List<Club> getClubbyName(String name) {
        return clubRepository.findByTeamName(name);
    }

    public List<Club> getClubs() {
        return clubRepository.findAll();
    }
    public void deleteClub(String id) {
        clubRepository.deleteById(id);
    }
    public List<Club> getClubsByOverall(Integer overall) {
        return clubRepository.findByOverall(overall);
    }



    public List<ClubNode> findNodeByName(String name) {
        return clubNodeRepository.findNodeByTeamName(name);
    }

    public List<ClubNode> getClubNodebyId(Long id) {
        return clubNodeRepository.findNodeById(id);
    }

    public List<ClubNode> getClubNodeByMongoId(String mongoId) {
        return clubNodeRepository.findNodeByMongoId(mongoId) ;
    }


    public String transferDataToNeo4j(PlayerNode.Gender gender){
        List<ClubNode> clubNodes = clubNodeRepository.findAll();
        List<Club> clubs = clubRepository.findByGender(String.valueOf(gender));
        int number = 0;

        for (Club club : clubs) {
            if (clubNodeRepository.existsByMongoId(club.getId())){
                continue;
            }

            ClubNode clubNode = new ClubNode();
            number +=1 ;
            clubNode.setMongoId(club.getId());
            clubNode.setTeamId(club.getTeamId());
            clubNode.setFifaVersion(Double.valueOf(club.getFifaVersion()));
            clubNode.setTeamName(club.getTeamName());
            clubNode.setNationalityName(club.getNationalityName());
            clubNode.setOverall(club.getOverall());
            clubNode.setCoachId(club.getCoachId());
            clubNode.setCaptain(club.getCaptain());
            clubNode.setGender(gender);
            // Save the node in Neo4j
            clubNodeRepository.save(clubNode);
        }
        return "The amount of " + clubs.stream().count() + " was checked and " + number + " was changed";
    }

    public List<Club> getClubbyNameAndFifaVersion(String clubName, Integer fifaVersion) {
        return clubRepository.findByTeamNameAndFifaVersion(clubName , fifaVersion);
    }
}
