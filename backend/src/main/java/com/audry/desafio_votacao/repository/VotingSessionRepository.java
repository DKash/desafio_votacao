package com.audry.desafio_votacao.repository;

import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.entities.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VotingSessionRepository extends JpaRepository<VotingSession, UUID> {
    Optional<VotingSession> findByVotingAgenda(VotingAgenda votingAgenda);
    
    Optional<VotingSession> findByIdAndVotingAgenda(UUID id, VotingAgenda votingAgenda);
}
