package com.example.bettominator.web.rest;


import com.example.bettominator.model.SportsGame;
import com.example.bettominator.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Bet-Page")
public class GameRestController {

    private final GameService gameService;


    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    private List<SportsGame> findAll() {
        return this.gameService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportsGame> findById(@PathVariable Long id) {
        return this.gameService.findById(id)
                .map(game -> ResponseEntity.ok().body(game))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public SportsGame save(@Valid SportsGame game) throws IOException {
        return this.gameService.save(game);
    }

    @PostMapping("/edit/{id}")
    public Optional<SportsGame> edit(@PathVariable Long id, @Valid SportsGame game) throws IOException {
        return this.gameService.edit(id, game);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.gameService.deleteById(id);
        if (this.gameService.findById(id).isEmpty()) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
