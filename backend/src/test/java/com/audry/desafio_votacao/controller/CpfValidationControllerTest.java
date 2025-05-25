package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.CpfStatusDto;
import com.audry.desafio_votacao.entities.CpfStatusEnum;
import com.audry.desafio_votacao.services.CpfValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(CpfValidationController.class)
class CpfValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CpfValidationService cpfValidationService;

    private CpfStatusDto mockCpfStatusDto;

    @BeforeEach
    void setUp() {
        mockCpfStatusDto = CpfStatusDto.builder()
                .status(CpfStatusEnum.ABLE_TO_VOTE)
                .build();
    }

    @Test
    void shouldReturnCpfStatusDtoWhenCpfIsValid() throws Exception {
        Mockito.when(cpfValidationService.validateCpf(anyString()))
                .thenReturn(mockCpfStatusDto);

        mockMvc.perform(get("/api/v1/cpf-validation/12345678901")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ABLE_TO_VOTE"));
    }
    
    @Test
    void shouldReturnCpfStatusUnableToVote() throws Exception {
        Mockito.when(cpfValidationService.validateCpf(anyString()))
                .thenReturn(CpfStatusDto.builder()
                        .status(CpfStatusEnum.UNABLE_TO_VOTE)
                        .build());

        mockMvc.perform(get("/api/v1/cpf-validation/12345678901")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNABLE_TO_VOTE"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        Mockito.when(cpfValidationService.validateCpf(anyString()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/v1/cpf-validation/12345678901")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
