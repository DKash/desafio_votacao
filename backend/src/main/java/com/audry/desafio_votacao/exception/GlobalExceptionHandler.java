package com.audry.desafio_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.audry.desafio_votacao.entities.CpfStatusEnum;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    // Excecao de CPF invalido
    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCpf(InvalidCpfException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssociatedUnableToVoteException.class)
    public ResponseEntity<Map<String, Object>> handleUnableToVote(AssociatedUnableToVoteException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", CpfStatusEnum.UNABLE_TO_VOTE);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // O cooperado ja votou
    @ExceptionHandler(AssociatedAlreadyVotedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyVoted(AssociatedAlreadyVotedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // A sessao de votacao ja foi encerrada
    @ExceptionHandler(VotingSessionClosedException.class)
    public ResponseEntity<Map<String, Object>> handleSessionClosed(VotingSessionClosedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // A sessao de votacao ja foi encerrada
    @ExceptionHandler(VotingSessionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSessionNotFound(VotingSessionNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Ja existe uma sessao de votacao aberta
    @ExceptionHandler(VotingSessionAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleSessionAlreadyExists(VotingSessionAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // A pauta de votacao nao foi encontrada
    @ExceptionHandler(VotingAgendaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAgendaNotFound(VotingAgendaNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Trata as demais excecoes
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
