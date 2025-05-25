package com.audry.desafio_votacao.repository;

import com.audry.desafio_votacao.entities.VotingAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VotingAgendaRepository extends JpaRepository<VotingAgenda, UUID> {
}
