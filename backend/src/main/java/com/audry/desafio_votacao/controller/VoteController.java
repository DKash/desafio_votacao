package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.VotingResultDto;
import com.audry.desafio_votacao.dto.VoteDto;
import com.audry.desafio_votacao.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> castVote(@RequestBody VoteDto dto) {
        voteService.castVote(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/result/{votingAgendaId}")
    public ResponseEntity<VotingResultDto> getVotingResult(@PathVariable UUID votingAgendaId) {
        return ResponseEntity.ok(voteService.getVotingResult(votingAgendaId));
    }
}
