package ru.yandex.practicum.filmorate.handler;


public class ErrorResponse {
    private String message;

    public String stackTrace;

    public ErrorResponse(String message, String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }
}
