package com.example.bettominator.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class GameAlreadyInShoppingCartException extends RuntimeException{

    public GameAlreadyInShoppingCartException(Long id, String username) {
        super(String.format("Game with id: %d already exists in shopping cart with selected bet type for user with username %s", id, username));
    }
}
