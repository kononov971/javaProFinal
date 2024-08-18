package ru.vtb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vtb.dto.ErrorResponseDTO;
import ru.vtb.exception.LimitException;
import ru.vtb.exception.TransactionException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(LimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleLimitException(LimitException exception) {
        return new ErrorResponseDTO(exception.getMessage());
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDTO handleTransactionException(TransactionException exception) {
        return new ErrorResponseDTO(exception.getMessage());
    }
}
