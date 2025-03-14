package Unipi.Fifa.services;

import Unipi.Fifa.models.Coach;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.CoachNodeRepository;
import Unipi.Fifa.repositories.CoachRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoachService {
    private final CoachNodeRepository coachNodeRepository;
    private final CoachRepository coachRepository;


    public CoachService(CoachRepository coachRepository, CoachNodeRepository coachNodeRepository) {
        this.coachRepository = coachRepository;
        this.coachNodeRepository = coachNodeRepository;
    }

    public Coach getCoachById(String id){
        return coachRepository.findById(id).orElse(null);
    }

    public CoachNode getCoachNodeByMongoId(String mongoId){
        return coachNodeRepository.findByMongoId(mongoId);
    }

    public Optional<Coach> getCoachByMongoId(String mongoId){
        return coachRepository.findById(mongoId);
    }

    public Coach getCoachByCoachId(Integer coachId) {
        return coachRepository.findByCoachId(coachId);
    }

    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    public List<Coach> getCouchesByGender(PlayerNode.Gender gender) {
        return coachRepository.findByGender(gender);
    }

    public String transferDataToNeo4j(PlayerNode.Gender gender) {
        List<CoachNode> coachNodes = coachNodeRepository.findByGender(gender);
        List<Coach> coaches = coachRepository.findByGender(gender);
        int number = 0;

        for (Coach coach : coaches) {
            if (coachNodeRepository.existsByMongoId(coach.getId())){
                continue;
            }
            CoachNode coachNode = new CoachNode();
            number += 1;
            coachNode.setMongoId(String.valueOf(coach.getId()));
            coachNode.setCoachId(coach.getCoachId());
            coachNode.setLongName(coach.getLongName());
            coachNode.setNationalityName(coach.getNationalityName());
            coachNode.setGender(coach.getGender());
            coachNodeRepository.save(coachNode);
        }

        return "The amount of " + coaches.size() + " was checked and " + number + " was changed";
    }

    public CoachNode TransferOneDataToNeo4j(String mongoId){
        Coach coach = coachRepository.findById(mongoId).orElse(null);
        CoachNode coachNode = coachNodeRepository.findByMongoId(mongoId);
        if (coachNode == null){
            coachNode = new CoachNode();
        }
        coachNode.setMongoId(String.valueOf(coach.getId()));
        coachNode.setCoachId(coach.getCoachId());
        coachNode.setLongName(coach.getLongName());
        coachNode.setNationalityName(coach.getNationalityName());
        coachNode.setGender(coach.getGender());
        coachNodeRepository.save(coachNode);
        return coachNode;
    }

    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    public void deletePreviousEdges(Integer coachId) {
        CoachNode coachNode = coachNodeRepository.findByCoachId(coachId);
        coachNode.setClubNode(null);
        coachNodeRepository.save(coachNode);
    }

    public void deleteCoachNodeById(Integer coachId) {
        CoachNode coachNode = coachNodeRepository.findByCoachId(coachId);
        Long id = coachNode.getId();
        coachNodeRepository.deleteCoachNodeById(id);
    }

    public void deleteCoachById(Integer coachId) {
        Coach coach = coachRepository.findById(coachId).orElse(null);
        if (coach != null){
            coachRepository.delete(coach);
        }
    }
}
