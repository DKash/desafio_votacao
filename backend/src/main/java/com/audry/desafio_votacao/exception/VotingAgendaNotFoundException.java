package com.audry.desafio_votacao.exception;

import java.util.UUID;

public class VotingAgendaNotFoundException extends RuntimeException {
    public VotingAgendaNotFoundException(UUID id) {
        super("A pauta com o id: " + id + " não foi encontrada");
    }
}
