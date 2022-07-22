package com.example.bettominator.model.exceptions;

public class PasswordRequirementsException extends RuntimeException{

    public PasswordRequirementsException() {
        super("The password needs to be longer than 8 characters with a capital and lowercase letter as well as a number");
    }
}
