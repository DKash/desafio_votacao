package com.audry.desafio_votacao.repository;

import com.audry.desafio_votacao.entities.Associated;
import com.audry.desafio_votacao.entities.VotingSession;
import com.audry.desafio_votacao.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    Optional<Vote> findByAssociatedAndSession(Associated associated, VotingSession votingSession);
    List<Vote> findAllBySession(VotingSession votingSession);
}
