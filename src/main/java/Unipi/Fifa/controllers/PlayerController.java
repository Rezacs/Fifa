package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/p")
@RequiredArgsConstructor
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getPlayersByClub(@RequestParam String clubName) {
        return playerService.getPlayersByClub(clubName);
    }
}
