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

    public ClubNode deletePreviousEdges(String mongoId) {
        // Retrieve the ClubNode by its mongoId
        ClubNode clubNode = clubNodeRepository.findNodeByMongoId(mongoId);

        if (clubNode != null) {
            // Remove outgoing relationships (coach relationships, player relationships, etc.)

            // Nullify the coach relationship (if exists)
            CoachNode coachNode = coachNodeRepository.findByCoachId(clubNode.getTeamId());
            if (coachNode != null) {
                coachNode.setManagingRelationships(null);
                coachNodeRepository.save(coachNode); // Save changes to coachNode
            }

            // Remove all player relationships associated with the ClubNode
            List<PlayerNode> playerNodes = playerNodeRepository.findByClubTeamId(clubNode.getTeamId());
            for (PlayerNode playerNode : playerNodes) {
                // Remove the relationship from player to club (assuming it's part of ClubRelationship)
                playerNode.getClubRelationships().removeIf(relationship ->
                        relationship.getClubNode().equals(clubNode)
                );
                playerNodeRepository.save(playerNode); // Save changes to playerNode
            }

            // Remove any incoming relationships (any other nodes connected to the clubNode)
            // Assuming you have a similar approach for other types of nodes (e.g., admin, event, etc.)
            List<OtherRelatedNode> relatedNodes = relatedNodeRepository.findByConnectedClubId(clubNode.getTeamId());
            for (OtherRelatedNode relatedNode : relatedNodes) {
                // Remove relationship from related node to clubNode (if applicable)
                relatedNode.removeClubNodeRelationship(clubNode); // Method to nullify or remove the relationship
                relatedNodeRepository.save(relatedNode); // Save changes to the relatedNode
            }

            // Finally, delete the clubNode itself or return it (depending on your use case)
            clubNodeRepository.delete(clubNode); // Optionally delete the ClubNode if needed

            // Return the ClubNode after deleting relationships
            return clubNode;
        } else {
            return null; // Return null if the clubNode was not found
        }
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
