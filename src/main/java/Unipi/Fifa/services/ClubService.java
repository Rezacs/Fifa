package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.*;
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
        return clubRepository.findByOverall(overall);
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
            clubNode.setFifaVersion(club.getFifaVersion());
            clubNode.setFifaUpdate(club.getFifaUpdate());
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

    public Club deletePreviousEdges(String mongoId){
        Club club = clubRepository.findById(mongoId).orElse(null);
        List<CoachNode> coachNode = coachNodeRepository.findByCoachId(club.getCoachId());
        List<PlayerNode> playerNodes = playerNodeRepository.findByClubTeamId(club.getTeamId());
        for (CoachNode node : coachNode) {
            node.setClubNode(null);
            coachNodeRepository.save(node);
        }
        for (PlayerNode node : playerNodes) {
            node.setClubNode(null);
            playerNodeRepository.save(node);

        }
        return club;
    }

    public ClubNode TransferOneDataToNeo4j(String mongoId) {
        Club club = clubRepository.findById(mongoId).orElse(null);
        List<CoachNode> coachNode = coachNodeRepository.findByCoachId(club.getCoachId());
//        for (CoachNode node : coachNode) {
//            node.setClubNode(null);
//        }
        if (club == null) {
            throw new IllegalArgumentException("Club with the provided mongoId does not exist.");
        }
        ClubNode clubNode = clubNodeRepository.findNodeByMongoId(club.getId());
        if (clubNode == null){
            clubNode = new ClubNode();
        }
        clubNode.setMongoId(mongoId);
        clubNode.setTeamId(club.getTeamId());
        clubNode.setFifaVersion(club.getFifaVersion());
        clubNode.setFifaUpdate(club.getFifaUpdate());
        clubNode.setTeamName(club.getTeamName());
        clubNode.setNationalityName(club.getNationalityName());
        clubNode.setOverall(club.getOverall());
        clubNode.setCoachId(club.getCoachId());
        clubNode.setCaptain(club.getCaptain());
        clubNodeRepository.save(clubNode);
        return clubNode;
    }

    public List<Club> getClubbyNameAndFifaVersion(String clubName, Integer fifaVersion) {
        return clubRepository.findByTeamNameAndFifaVersion(clubName , fifaVersion);
    }
}
