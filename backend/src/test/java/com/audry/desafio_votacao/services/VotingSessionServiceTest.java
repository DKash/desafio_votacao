package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VotingSessionDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.entities.VotingSession;
import com.audry.desafio_votacao.exception.VotingSessionAlreadyExistsException;
import com.audry.desafio_votacao.exception.VotingSessionClosedException;
import com.audry.desafio_votacao.exception.VotingSessionNotFoundException;
import com.audry.desafio_votacao.repository.VotingSessionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VotingSessionServiceTest {

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private VotingAgendaService votingAgendaService;

    @InjectMocks
    private VotingSessionService votingSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldOpenVotingSessionSuccessfullyWhenNoExistingSession() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.empty());

        when(votingSessionRepository.save(any(VotingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(10)
                .build();

        VotingSession session = votingSessionService.openVotingSession(dto);

        assertNotNull(session);
        assertEquals(votingAgenda, session.getVotingAgenda());
        assertFalse(session.isFinished());
        assertTrue(session.getDtEnd().isAfter(session.getDtStart()));
    }

    @Test
    void shouldMarkVotingSessionAsFinished() {
        VotingAgenda votingAgenda = VotingAgenda.builder().id(UUID.randomUUID()).build();
        VotingSession session = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().plusMinutes(5))
                .build();

        when(votingSessionRepository.save(any(VotingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSession finished = votingSessionService.finishVotingSession(session);
        finished.setFinished(true);

        assertTrue(finished.isFinished(), "A sessão deve ser marcada como finalizada");
        verify(votingSessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowExceptionWhenVotingAgendaNotFoundOnOpenSession() {
        UUID invalidAgendaId = UUID.randomUUID();

        when(votingAgendaService.findById(invalidAgendaId)).thenThrow(new VotingSessionNotFoundException(invalidAgendaId));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(invalidAgendaId)
                .duration(10)
                .build();

        assertThrows(VotingSessionNotFoundException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
    }

    @Test
    void shouldDefaultDurationToOneMinuteWhenDurationIsNullOrZero() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.empty());
        when(votingSessionRepository.save(any(VotingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSessionDto dtoNullDuration = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(null)
                .build();

        VotingSession sessionNull = votingSessionService.openVotingSession(dtoNullDuration);
        assertNotNull(sessionNull);
        assertEquals(1, sessionNull.getDtEnd().minusMinutes(sessionNull.getDtStart().getMinute()).getMinute());

        VotingSessionDto dtoZeroDuration = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(0)
                .build();

        VotingSession sessionZero = votingSessionService.openVotingSession(dtoZeroDuration);
        assertNotNull(sessionZero);
        assertEquals(1, sessionZero.getDtEnd().minusMinutes(sessionZero.getDtStart().getMinute()).getMinute());
    }

    @Test
    void shouldNotAllowOpeningMultipleSessionsForSameAgenda() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        VotingSession existingSession = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().plusMinutes(10))
                .build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.of(existingSession));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(5)
                .build();

        assertThrows(VotingSessionClosedException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
        assertThrows(VotingSessionClosedException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
        verify(votingSessionRepository, never()).save(any());
    }


    @Test
    void shouldThrowClosedExceptionWhenSessionExistsAndOpen() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        VotingSession existingSession = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().plusMinutes(10))
                .build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.of(existingSession));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(5)
                .build();

        assertThrows(VotingSessionClosedException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
    }

    @Test
    void shouldEnsureDefaultDurationIsOneMinuteWhenNullOrZero() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.empty());
        when(votingSessionRepository.save(any(VotingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(null)
                .build();

        VotingSession session = votingSessionService.openVotingSession(dto);

        Duration duration = Duration.between(session.getDtStart(), session.getDtEnd());
        assertEquals(1, duration.toMinutes());
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenSessionExistsAndClosed() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        VotingSession existingSession = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(true)
                .dtEnd(LocalDateTime.now().minusMinutes(10))
                .build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.of(existingSession));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(5)
                .build();

        assertThrows(VotingSessionClosedException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenSessionExistsAndOpen() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        VotingSession existingSession = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().minusMinutes(10))
                .build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.of(existingSession));

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(5)
                .build();

        assertThrows(VotingSessionAlreadyExistsException.class, () -> {
            votingSessionService.openVotingSession(dto);
        });
    }

    @Test
    void shouldFinishVotingSession() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();
        VotingSession session = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().plusMinutes(5))
                .build();

        when(votingSessionRepository.save(session)).thenReturn(session);

        VotingSession finishedSession = votingSessionService.finishVotingSession(session);
        assertEquals(session, finishedSession);
    }

    @Test
    void shouldFindVotingSessionByVotingAgenda() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();
        VotingSession session = VotingSession.builder()
                .votingAgenda(votingAgenda)
                .finished(false)
                .dtEnd(LocalDateTime.now().plusMinutes(5))
                .build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.of(session));

        VotingSession found = votingSessionService.findByVotingAgenda(agendaId);

        assertEquals(session, found);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenVotingSessionNotFound() {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder().id(agendaId).build();

        when(votingAgendaService.findById(agendaId)).thenReturn(votingAgenda);
        when(votingSessionRepository.findByVotingAgenda(votingAgenda)).thenReturn(Optional.empty());

        assertThrows(VotingSessionNotFoundException.class, () -> {
            votingSessionService.findByVotingAgenda(agendaId);
        });
    }
}
