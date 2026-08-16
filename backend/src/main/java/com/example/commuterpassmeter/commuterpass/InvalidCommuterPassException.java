package com.example.commuterpassmeter.commuterpass;

public class InvalidCommuterPassException extends RuntimeException {
    public InvalidCommuterPassException(String message) {
        super(message);
    }
}
