package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    private final ClubRepository clubRepository;

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

    @Autowired
    private ClubNodeRepository clubnodeRepository;

    public List<ClubNode> findNodeByName(String name) {
        return clubnodeRepository.findNodeByTeamName(name);
    }

    public List<ClubNode> getClubNodebyId(String id) {
        return clubnodeRepository.findNodeById(id);
    }


    public void transferDataToNeo4j(){
        List<ClubNode> clubNodes = clubnodeRepository.findAll();
        List<Club> clubs = clubRepository.findAll();
//
//        for (ClubNode club : clubs) {
//            // Check if the node already exists in Neo4j
//            List<ClubNode> existingNode = clubnodeRepository.findNodeByName(club.getName()) ;
//
//            if (existingNode.isEmpty()) {
//                // If the node does not exist, create a new one
//                ClubNode clubNode = new ClubNode();
//                clubNode.setMongoId(club.getMongoId());
//                clubNode.setName(club.getName());
//                clubNode.setOverall(club.getOverall());
//
//                // Save the node in Neo4j
//                clubnodeRepository.save(clubNode);
//            }
//        }

        for (Club club : clubs) {
            List<ClubNode> existingNode = clubnodeRepository.findNodeByMongoId(club.getId()) ;
            ClubNode clubNode = new ClubNode();
            clubNode.setMongoId(club.getId());
            clubNode.setFifaVersion(club.getFifaVersion());
            clubNode.setTeamName(club.getTeamName());
            clubNode.setNationalityName(club.getNationalityName());
            clubNode.setOverall(club.getOverall());
            clubNode.setCoachId(club.getCoachId());
            clubNode.setCaptain(club.getCaptain());
            // Save the node in Neo4j
            clubnodeRepository.save(clubNode);
        }

    }

    public List<Club> getClubbyNameAndFifaVersion(String clubName, Integer fifaVersion) {
        return clubRepository.findByTeamNameAndFifaVersion(clubName , fifaVersion);
    }
}
