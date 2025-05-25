package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VotingAgendaDto;
import com.audry.desafio_votacao.entities.VotingAgenda;
import com.audry.desafio_votacao.services.VotingAgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/voting-agendas")
@RequiredArgsConstructor
public class VotingAgendaController {

    private final VotingAgendaService votingAgendaService;

    @PostMapping
    public ResponseEntity<VotingAgenda> addVotingAgenda(@RequestBody VotingAgendaDto dto) {
        return ResponseEntity.ok(votingAgendaService.addVotingAgenda(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingAgenda> getVotingAgendaById(@PathVariable UUID id) {
        return ResponseEntity.ok(votingAgendaService.findById(id));
    }
}
