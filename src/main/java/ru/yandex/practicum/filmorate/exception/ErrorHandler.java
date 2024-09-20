package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(NotFoundException exp) {
        log.error("Объект не найден.");
        return new ErrorResponse("Объект не найден.", exp.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidationException(ValidationException exp) {
        log.error("Ошибка валидации.");
        return new ErrorResponse("Ошибка валидации.", exp.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        log.error("Ошибка валидации.");
        return new ErrorResponse("Ошибка валидации.", exp.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleException(Exception exp) {
        log.error("Произошла непредвиденная ошибка!");
        return new ErrorResponse("Произошла непредвиденная ошибка.", exp.getMessage());
    }
}
