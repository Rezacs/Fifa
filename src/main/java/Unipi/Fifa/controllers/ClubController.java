package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.services.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/c")
@RequiredArgsConstructor
public class ClubController {
    @Autowired
    private ClubService clubService;

    @GetMapping("/clubs")
    public List<Club> findByName(@RequestParam String name) {
        return clubService.getClubbyName(name);
    }

    @GetMapping("/Mongo/{ClubMongoId}")
    public Club findClubByMongoId(@PathVariable String ClubMongoId) {
        return ResponseEntity.ok(clubService.getClubbyId(ClubMongoId)).getBody();
    }

    @GetMapping("/overall/{clubOverall}")
    public List<Club> findClubOverall(@PathVariable Integer clubOverall) {
        return ResponseEntity.ok(clubService.getClubsByOverall(clubOverall)).getBody();
    }




}
