package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    @Autowired
    private final ClubRepository clubRepository;
    @Autowired
    private ClubNodeRepository clubNodeRepository;
    @Autowired
    private CoachNodeRepository coachNodeRepository;
    @Autowired
    private PlayerNodeRepository playerNodeRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }



    public Club saveClub(Club club) {
        return clubRepository.save(club);
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
        return clubRepository.findByMergedVersionsOverall(overall);
    }



    public List<ClubNode> findNodeByName(String name) {
        return clubNodeRepository.findNodeByTeamName(name);
    }

    public List<ClubNode> getClubNodebyId(Long id) {
        return clubNodeRepository.findNodeById(id);
    }

    public ClubNode getClubNodeByMongoId(String mongoId) {
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
            clubNode.setTeamName(club.getTeamName());
            clubNode.setNationalityName(club.getNationalityName());
            clubNode.setGender(PlayerNode.Gender.valueOf(club.getGender().toUpperCase()));
            clubNode.setHomeStadium(club.getHomeStadium());
            // Save the node in Neo4j
            clubNodeRepository.save(clubNode);
        }
        return "The amount of " + clubs.stream().count() + " was checked and " + number + " was changed";
    }

    @Transactional
    public ClubNode deletePreviousEdges(String mongoId) {
        // Retrieve the ClubNode by its mongoId
        ClubNode clubNode = clubNodeRepository.findNodeByMongoId(mongoId);
        if (clubNode == null) {
            return null; // If no club found, return null
        }

        // Step 1: Remove MANAGES relationships between coaches and the club
        coachNodeRepository.deleteCoachClubRelationships(mongoId);

        // Step 2: Remove BELONGS_TO relationships between players and the club
        playerNodeRepository.deleteClubRelationships(mongoId);

        // Step 3: Delete the ClubNode itself
        clubNodeRepository.delete(clubNode);

        // Return the deleted ClubNode (optional, depends on use case)
        return clubNode;
    }






    public ClubNode TransferOneDataToNeo4j(String mongoId) {
        Club club = clubRepository.findById(mongoId).orElse(null);
        if (club == null) {
            throw new IllegalArgumentException("Club with the provided mongoId does not exist.");
        }
        ClubNode clubNode = clubNodeRepository.findNodeByMongoId(club.getId());
        if (clubNode == null){
            clubNode = new ClubNode();
        }
        clubNode.setMongoId(mongoId);
        clubNode.setTeamId(club.getTeamId());
        clubNode.setTeamName(club.getTeamName());
        clubNode.setNationalityName(club.getNationalityName());
        clubNode.setGender(PlayerNode.Gender.valueOf(club.getGender()));
        clubNodeRepository.save(clubNode);
        return clubNode;
    }

    public Club getClubbyNameAndFifaVersion(String clubName, Integer fifaVersion) {
        return clubRepository.findByTeamNameAndMergedVersionsContaining(clubName , fifaVersion).orElse(null);
    }

    public void deleClubNodeByMongoId(String mongoId) {
        ClubNode target = clubNodeRepository.findNodeByMongoId(mongoId);
        clubNodeRepository.deleteClubNodeById(target.getId());
    }
}
