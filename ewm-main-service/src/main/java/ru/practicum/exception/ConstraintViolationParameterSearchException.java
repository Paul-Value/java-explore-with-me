package ru.practicum.exception;

public class ConstraintViolationParameterSearchException extends BadRequestException {
    public ConstraintViolationParameterSearchException(String message) {
        super(message);
    }
}
