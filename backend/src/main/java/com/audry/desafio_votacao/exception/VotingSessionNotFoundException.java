package com.audry.desafio_votacao.exception;

import java.util.UUID;

public class VotingSessionNotFoundException extends RuntimeException {
    public VotingSessionNotFoundException(UUID id) {
        super("A sessão da pauta com id: " + id + " não foi encontrada");
    }
}
