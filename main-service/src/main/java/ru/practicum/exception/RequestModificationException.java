package ru.practicum.exception;

public class RequestModificationException extends RuntimeException {
    public RequestModificationException(String message) {
        super(message);
    }
}
