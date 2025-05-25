package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VotingSessionDto;
import com.audry.desafio_votacao.entities.VotingSession;
import com.audry.desafio_votacao.services.VotingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class VotingSessionController {

    private final VotingSessionService votingSessionService;

    @PostMapping
    public ResponseEntity<VotingSession> openVotingSession(@RequestBody VotingSessionDto dto) {
        return ResponseEntity.ok(votingSessionService.openVotingSession(dto));
    }

    @GetMapping("/voting-session/{votingAgendaId}")
    public ResponseEntity<VotingSession> findByVotingAgenda(@PathVariable UUID votingAgendaId) {
        return ResponseEntity.ok(votingSessionService.findByVotingAgenda(votingAgendaId));
    }
}
