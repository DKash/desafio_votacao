package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VotingAgendaDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.exception.VotingAgendaNotFoundException;
import com.audry.desafio_votacao.services.VotingAgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotingAgendaController.class)
class VotingAgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotingAgendaService votingAgendaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddVotingAgendaSuccessfully() throws Exception {
        VotingAgendaDto dto = VotingAgendaDto.builder()
                .title("Título da Pauta")
                .description("Descrição da Pauta")
                .build();

        UUID generatedId = UUID.randomUUID();

        VotingAgenda agenda = VotingAgenda.builder()
                .id(generatedId)
                .title("Título da Pauta")
                .description("Descrição da Pauta")
                .build();

        Mockito.when(votingAgendaService.addVotingAgenda(any(VotingAgendaDto.class)))
                .thenReturn(agenda);

        mockMvc.perform(post("/api/v1/voting-agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.title").value("Título da Pauta"))
                .andExpect(jsonPath("$.description").value("Descrição da Pauta"));
    }

    @Test
    void shouldGetVotingAgendaByIdSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        VotingAgenda agenda = VotingAgenda.builder()
                .id(id)
                .title("Título Recuperado")
                .description("Descrição Recuperada")
                .build();

        Mockito.when(votingAgendaService.findById(id)).thenReturn(agenda);

        mockMvc.perform(get("/api/v1/voting-agendas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Título Recuperado"))
                .andExpect(jsonPath("$.description").value("Descrição Recuperada"));
    }

    @Test
    void shouldReturn404WhenVotingAgendaNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(votingAgendaService.findById(id))
                .thenThrow(new VotingAgendaNotFoundException(id));

        mockMvc.perform(get("/api/v1/voting-agendas/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                    .value("A pauta com o id: " + id + " não foi encontrada"));
    }
}
