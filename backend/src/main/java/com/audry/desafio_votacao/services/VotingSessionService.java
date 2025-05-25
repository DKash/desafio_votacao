package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VotingSessionDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.entities.VotingSession;
import com.audry.desafio_votacao.repository.VotingSessionRepository;
import com.audry.desafio_votacao.exception.VotingSessionAlreadyExistsException;
import com.audry.desafio_votacao.exception.VotingSessionClosedException;
import com.audry.desafio_votacao.exception.VotingSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final VotingAgendaService votingAgendaService;

    /**
     * Abre uma sessao de votacao para a pauta informada.
     */
    public VotingSession openVotingSession(VotingSessionDto dto) {
        VotingAgenda votingAgenda = votingAgendaService.findById(dto.getVotingAgendaId());
        Optional<VotingSession> existingSession = votingSessionRepository.findByVotingAgenda(votingAgenda);
        
        LocalDateTime now = LocalDateTime.now();
        // Valida se ja existe uma sessao de votacao aberta para a pauta
        if (existingSession.isPresent()) {
            // A sessao ja existe, mas esta fechada
            if (existingSession.get().isFinished() || existingSession.get().getDtEnd().isAfter(now)) {
                throw new VotingSessionClosedException();
            }
            throw new VotingSessionAlreadyExistsException();
        }

        int duration = (dto.getDuration() == null || dto.getDuration() <= 0) ? 1 : dto.getDuration();

        VotingSession session = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .dtStart(now)
                .dtEnd(now.plusMinutes(duration))
                .finished(false)
                .build();

        return votingSessionRepository.save(session);
    }

    /**
     * Finaliza uma sessao de votacao
     */
    public VotingSession finishVotingSession(VotingSession session) {
        return votingSessionRepository.save(session);
    }

    public VotingSession findByVotingAgenda(UUID votingAgendaId) {
        VotingAgenda votingAgenda = votingAgendaService.findById(votingAgendaId);
        return votingSessionRepository.findByVotingAgenda(votingAgenda)
                .orElseThrow(() -> new VotingSessionNotFoundException(votingAgendaId));
    }
}
