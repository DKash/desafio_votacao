package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VoteDto;
import com.audry.desafio_votacao.dto.VotingResultDto;
import com.audry.desafio_votacao.entities.VoteEnum;
import com.audry.desafio_votacao.exception.AssociatedAlreadyVotedException;
import com.audry.desafio_votacao.exception.VotingAgendaNotFoundException;
import com.audry.desafio_votacao.exception.VotingSessionClosedException;
import com.audry.desafio_votacao.exception.VotingSessionNotFoundException;
import com.audry.desafio_votacao.services.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(VoteController.class)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCastVoteSuccessfully() throws Exception {
        VoteDto voteDto = VoteDto.builder()
                .cpf("12345678901")
                .votingAgendaId(UUID.randomUUID())
                .vote(VoteEnum.Sim)
                .build();

        mockMvc.perform(post("/api/v1/votes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnVotingResultSuccessfully() throws Exception {
        UUID agendaId = UUID.randomUUID();

        VotingResultDto resultDto = VotingResultDto.builder()
                .yesCount(5)
                .noCount(2)
                .build();

        Mockito.when(voteService.getVotingResult(agendaId)).thenReturn(resultDto);

        mockMvc.perform(get("/api/v1/votes/result/" + agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qtdVotosSim").value(5))
                .andExpect(jsonPath("$.qtdVotosNão").value(2));
    }

    @Test
    void shouldReturn404WhenVotingSessionNotFound() throws Exception {
    VoteDto voteDto = VoteDto.builder()
            .cpf("12345678901")
            .votingAgendaId(UUID.randomUUID())
            .vote(VoteEnum.Sim)
            .build();

    Mockito.doThrow(new VotingSessionNotFoundException(voteDto.getVotingAgendaId()))
            .when(voteService).castVote(Mockito.any());

    mockMvc.perform(post("/api/v1/votes")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(voteDto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("A sessão da pauta com id: " + voteDto.getVotingAgendaId() + " não foi encontrada"));
    }

    @Test
    void shouldReturn400WhenUserAlreadyVoted() throws Exception {
        VoteDto voteDto = VoteDto.builder()
                .cpf("12345678901")
                .votingAgendaId(UUID.randomUUID())
                .vote(VoteEnum.Não)
                .build();

        Mockito.doThrow(new AssociatedAlreadyVotedException())
                .when(voteService).castVote(Mockito.any());

        mockMvc.perform(post("/api/v1/votes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Você já votou nesta pauta"));
    }

    @Test
    void shouldReturn404WhenVotingResultNotFound() throws Exception {
        UUID agendaId = UUID.randomUUID();

        Mockito.when(voteService.getVotingResult(agendaId))
                .thenThrow(new VotingAgendaNotFoundException(agendaId));

        mockMvc.perform(get("/api/v1/votes/result/" + agendaId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("A pauta com o id: " + agendaId + " não foi encontrada"));
    }

    @Test
    void shouldReturn400WhenVotingSessionIsClosed() throws Exception {
        VoteDto voteDto = VoteDto.builder()
                .cpf("12345678901")
                .votingAgendaId(UUID.randomUUID())
                .vote(VoteEnum.Sim)
                .build();

        Mockito.doThrow(new VotingSessionClosedException())
                .when(voteService).castVote(Mockito.any());

        mockMvc.perform(post("/api/v1/votes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A sessão já está encerrada"));
    }
}
