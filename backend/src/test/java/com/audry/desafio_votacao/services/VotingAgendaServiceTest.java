package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VotingAgendaDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.exception.VotingAgendaNotFoundException;
import com.audry.desafio_votacao.repository.VotingAgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotingAgendaServiceTest {

    @Mock
    private VotingAgendaRepository votingAgendaRepository;

    @InjectMocks
    private VotingAgendaService votingAgendaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddVotingAgenda() {
        VotingAgendaDto dto = VotingAgendaDto.builder()
                .title("Agenda Title")
                .description("Agenda Description")
                .build();

        VotingAgenda savedAgenda = VotingAgenda.builder()
                .id(UUID.randomUUID())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();

        when(votingAgendaRepository.save(any(VotingAgenda.class))).thenReturn(savedAgenda);

        VotingAgenda result = votingAgendaService.addVotingAgenda(dto);

        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        verify(votingAgendaRepository, times(1)).save(any(VotingAgenda.class));
    }

    @Test
    void shouldFindVotingAgendaById() {
        UUID id = UUID.randomUUID();
        VotingAgenda votingAgenda = VotingAgenda.builder()
                .id(id)
                .title("Agenda Title")
                .description("Agenda Description")
                .build();

        when(votingAgendaRepository.findById(id)).thenReturn(Optional.of(votingAgenda));

        VotingAgenda result = votingAgendaService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Agenda Title", result.getTitle());
        verify(votingAgendaRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAgendaDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(votingAgendaRepository.findById(id)).thenReturn(Optional.empty());

        VotingAgendaNotFoundException exception = assertThrows(VotingAgendaNotFoundException.class, () -> {
            votingAgendaService.findById(id);
        });

        String expectedMessage = "A pauta com o id: " + id + " não foi encontrada";
        assertEquals(expectedMessage, exception.getMessage());

        verify(votingAgendaRepository, times(1)).findById(id);
    }
}
