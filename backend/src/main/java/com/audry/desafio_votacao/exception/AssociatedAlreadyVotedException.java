package com.audry.desafio_votacao.exception;

public class AssociatedAlreadyVotedException extends RuntimeException {
    public AssociatedAlreadyVotedException() {
        super("Você já votou nesta pauta");
    }
}
