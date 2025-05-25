package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VotingAgendaDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.repository.VotingAgendaRepository;
import com.audry.desafio_votacao.exception.VotingAgendaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VotingAgendaService {

    /**
     * Repositorio da pauta
     */
    private final VotingAgendaRepository VotingAgendaRepository;

    /**
     * Adciiona uma nova pauta
     */
    public VotingAgenda addVotingAgenda(VotingAgendaDto dto) {
        VotingAgenda votingAgenda = VotingAgenda.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        return VotingAgendaRepository.save(votingAgenda);
    }

    /**
     * Pesquisa uma pauta pelo id
     */
    public VotingAgenda findById(UUID id) {
        return VotingAgendaRepository.findById(id)
                .orElseThrow(() -> new VotingAgendaNotFoundException(id));
    }
}
