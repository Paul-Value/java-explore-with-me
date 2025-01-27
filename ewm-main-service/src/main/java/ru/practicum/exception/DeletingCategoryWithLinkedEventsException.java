package ru.practicum.exception;

public class DeletingCategoryWithLinkedEventsException extends RuntimeException {
    public DeletingCategoryWithLinkedEventsException(String message) {
        super(message);
    }

    public DeletingCategoryWithLinkedEventsException(String message, Throwable cause) {
        super(message, cause);
    }
}
