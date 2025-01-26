package ru.practicum.exception;

public class UniqueEmailByUserException extends RuntimeException {
    public UniqueEmailByUserException(String message) {
        super(message);
    }
}
