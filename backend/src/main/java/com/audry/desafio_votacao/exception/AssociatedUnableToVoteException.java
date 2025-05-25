package com.audry.desafio_votacao.exception;

public class AssociatedUnableToVoteException extends RuntimeException {
    public AssociatedUnableToVoteException() {
        super("O associado não pode votar");
    }
}
