package com.audry.desafio_votacao.exception;

public class VotingSessionClosedException extends RuntimeException {
    public VotingSessionClosedException() {
        super("A sessão já está encerrada");
    }
}
