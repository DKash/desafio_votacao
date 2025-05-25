package com.audry.desafio_votacao.exception;

public class VotingSessionAlreadyExistsException extends RuntimeException {

    public VotingSessionAlreadyExistsException() {
        super("Já existe uma sessão de votação aberta para esta pauta");
    }
}
