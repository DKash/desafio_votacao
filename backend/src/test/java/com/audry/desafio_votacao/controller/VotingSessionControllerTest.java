package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VotingSessionDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.entities.VotingSession;
import com.audry.desafio_votacao.exception.VotingSessionNotFoundException;
import com.audry.desafio_votacao.services.VotingSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotingSessionController.class)
class VotingSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotingSessionService votingSessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldOpenVotingSessionSuccessfully() throws Exception {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder()
                .id(agendaId)
                .title("Pauta Teste")
                .description("Descrição da pauta")
                .build();

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(2)
                .build();

        VotingSession session = VotingSession.builder()
                .id(UUID.randomUUID())
                .votingAgenda(votingAgenda)
                .dtStart(LocalDateTime.now())
                .dtEnd(LocalDateTime.now().plusMinutes(2))
                .finished(false)
                .build();

        Mockito.when(votingSessionService.openVotingSession(any(VotingSessionDto.class)))
                .thenReturn(session);

        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votingAgenda.id").value(agendaId.toString()))
                .andExpect(jsonPath("$.dtStart").exists())
                .andExpect(jsonPath("$.dtEnd").exists())
                .andExpect(jsonPath("$.finished").value(false));
    }

    @Test
    void shouldFindVotingSessionByVotingAgendaIdSuccessfully() throws Exception {
        UUID agendaId = UUID.randomUUID();

        VotingAgenda votingAgenda = VotingAgenda.builder()
                .id(agendaId)
                .title("Pauta")
                .description("Descrição")
                .build();

        VotingSession session = VotingSession.builder()
                .id(UUID.randomUUID())
                .votingAgenda(votingAgenda)
                .dtStart(LocalDateTime.now())
                .dtEnd(LocalDateTime.now().plusMinutes(1))
                .finished(false)
                .build();

        Mockito.when(votingSessionService.findByVotingAgenda(agendaId)).thenReturn(session);

        mockMvc.perform(get("/api/v1/sessions/voting-session/" + agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votingAgenda.id").value(agendaId.toString()))
                .andExpect(jsonPath("$.dtStart").exists())
                .andExpect(jsonPath("$.dtEnd").exists())
                .andExpect(jsonPath("$.finished").value(false));
    }

    @Test
    void shouldOpenSessionWithDefaultDurationWhenDurationIsNull() throws Exception {
        UUID agendaId = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder()
                .id(agendaId)
                .title("Pauta com duração default")
                .description("Testando null")
                .build();

        VotingSessionDto dto = VotingSessionDto.builder()
                .votingAgendaId(agendaId)
                .duration(null)
                .build();

        VotingSession session = VotingSession.builder()
                .id(UUID.randomUUID())
                .votingAgenda(votingAgenda)
                .dtStart(LocalDateTime.now())
                .dtEnd(LocalDateTime.now().plusMinutes(1))
                .finished(false)
                .build();

        Mockito.when(votingSessionService.openVotingSession(any(VotingSessionDto.class)))
                .thenReturn(session);

        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votingAgenda.id").value(agendaId.toString()))
                .andExpect(jsonPath("$.finished").value(false));
    }
    
    @Test
    void shouldReturn404WhenVotingSessionNotFound() throws Exception {
        UUID agendaId = UUID.randomUUID();

        Mockito.when(votingSessionService.findByVotingAgenda(agendaId))
                .thenThrow(VotingSessionNotFoundException.class);

        mockMvc.perform(get("/api/v1/sessions/voting-session/" + agendaId))
                .andExpect(status().isNotFound());
    }
}
