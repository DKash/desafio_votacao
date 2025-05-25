package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VoteDto;
import com.audry.desafio_votacao.dto.VotingResultDto;
import com.audry.desafio_votacao.dto.CpfStatusDto;
import com.audry.desafio_votacao.entities.*;
import com.audry.desafio_votacao.exception.AssociatedAlreadyVotedException;
import com.audry.desafio_votacao.exception.AssociatedUnableToVoteException;
import com.audry.desafio_votacao.exception.VotingSessionClosedException;
import com.audry.desafio_votacao.repository.AssociatedRepository;
import com.audry.desafio_votacao.repository.VoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock private AssociatedRepository associatedRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VotingSessionService votingSessionService;
    @Mock private CpfValidationService cpfValidationService;

    @InjectMocks private VoteService voteService;

    private VoteDto voteDto;
    private Associated associated;
    private VotingSession session;
    private UUID agendaId;

    @BeforeEach
    void setUp() {
        agendaId = UUID.randomUUID();

        voteDto = VoteDto.builder()
                .cpf("12345678900")
                .votingAgendaId(agendaId)
                .vote(VoteEnum.Sim)
                .build();

        associated = Associated.builder()
                .id(UUID.randomUUID())
                .cpf("12345678900")
                .build();

        session = VotingSession.builder()
                .id(UUID.randomUUID())
                .votingAgenda(VotingAgenda.builder().id(agendaId).build())
                .dtEnd(LocalDateTime.now().plusMinutes(10))
                .finished(false)
                .build();
    }

    private void mockValidVoteScenario() {
        when(cpfValidationService.validateCpf(voteDto.getCpf()))
                .thenReturn(CpfStatusDto.builder().status(CpfStatusEnum.ABLE_TO_VOTE).build());
        when(associatedRepository.findByCpf(voteDto.getCpf()))
                .thenReturn(Optional.of(associated));
        when(votingSessionService.findByVotingAgenda(agendaId))
                .thenReturn(session);
    }

    private List<Vote> createVotes(VoteEnum... votes) {
        return Arrays.stream(votes)
                .map(v -> Vote.builder().vote(v).build())
                .toList();
    }


    @Test
    void shouldAllowValidVote() {
        mockValidVoteScenario();
        when(voteRepository.findByAssociatedAndSession(associated, session)).thenReturn(Optional.empty());

        voteService.castVote(voteDto);
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    void shouldRejectInvalidCpf() {
        when(cpfValidationService.validateCpf(voteDto.getCpf()))
                .thenReturn(CpfStatusDto.builder().status(CpfStatusEnum.UNABLE_TO_VOTE).build());

        assertThrows(AssociatedUnableToVoteException.class, () -> voteService.castVote(voteDto));
        verifyNoInteractions(voteRepository);
    }

    @Test
    void shouldRejectVoteAfterSessionEnds() {
        session.setDtEnd(LocalDateTime.now().minusMinutes(1));
        mockValidVoteScenario();

        assertThrows(VotingSessionClosedException.class, () -> voteService.castVote(voteDto));
        verify(votingSessionService).finishVotingSession(session);
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateVote() {
        mockValidVoteScenario();
        when(voteRepository.findByAssociatedAndSession(associated, session))
                .thenReturn(Optional.of(Vote.builder().build()));

        assertThrows(AssociatedAlreadyVotedException.class, () -> voteService.castVote(voteDto));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldReturnApprovedResult() {
        when(votingSessionService.findByVotingAgenda(agendaId)).thenReturn(session);
        when(voteRepository.findAllBySession(session)).thenReturn(createVotes(VoteEnum.Sim, VoteEnum.Sim, VoteEnum.Não));

        VotingResultDto result = voteService.getVotingResult(agendaId);

        assertEquals("APROVADA", result.getResult());
        assertEquals(2, result.getYesCount());
        assertEquals(1, result.getNoCount());
    }

    @Test
    void shouldReturnRejectedResult() {
        when(votingSessionService.findByVotingAgenda(agendaId)).thenReturn(session);
        when(voteRepository.findAllBySession(session)).thenReturn(createVotes(VoteEnum.Não, VoteEnum.Não, VoteEnum.Sim));

        VotingResultDto result = voteService.getVotingResult(agendaId);
        assertEquals("REPROVADA", result.getResult());
    }

    @Test
    void shouldReturnTieResult() {
        when(votingSessionService.findByVotingAgenda(agendaId)).thenReturn(session);
        when(voteRepository.findAllBySession(session)).thenReturn(createVotes(VoteEnum.Sim, VoteEnum.Não));

        VotingResultDto result = voteService.getVotingResult(agendaId);
        assertEquals("EMPATE", result.getResult());
    }

    @Test
    void shouldReturnNoVotesResult() {
        when(votingSessionService.findByVotingAgenda(agendaId)).thenReturn(session);
        when(voteRepository.findAllBySession(session)).thenReturn(Collections.emptyList());

        VotingResultDto result = voteService.getVotingResult(agendaId);
        assertEquals("NAO REALIZADA", result.getResult());
    }

    @Test
    void shouldSaveVoteWithCorrectData() {
        mockValidVoteScenario();
        when(voteRepository.findByAssociatedAndSession(associated, session)).thenReturn(Optional.empty());

        voteService.castVote(voteDto);

        ArgumentCaptor<Vote> voteCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository).save(voteCaptor.capture());

        Vote savedVote = voteCaptor.getValue();
        assertEquals(VoteEnum.Sim, savedVote.getVote());
        assertEquals(associated, savedVote.getAssociated());
        assertEquals(session, savedVote.getSession());
    }

    @Test
    void shouldThrowException_whenAssociatedNotFound() {
        when(cpfValidationService.validateCpf(voteDto.getCpf()))
                .thenReturn(CpfStatusDto.builder().status(CpfStatusEnum.ABLE_TO_VOTE).build());
        when(associatedRepository.findByCpf(voteDto.getCpf()))
                .thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> voteService.castVote(voteDto));
    }

    @Test
    void shouldThrowException_whenVotingSessionNotFound() {
        when(cpfValidationService.validateCpf(voteDto.getCpf()))
                .thenReturn(CpfStatusDto.builder().status(CpfStatusEnum.ABLE_TO_VOTE).build());
        when(associatedRepository.findByCpf(voteDto.getCpf()))
                .thenReturn(Optional.of(associated));
        when(votingSessionService.findByVotingAgenda(agendaId))
                .thenThrow(new RuntimeException("Sessão não encontrada"));
        assertThrows(RuntimeException.class, () -> voteService.castVote(voteDto));
    }
}
