package ru.practicum.exception;

public class EventModificationException extends BadRequestException {
    public EventModificationException(String message) {
        super(message);
    }
}
