package com.audry.desafio_votacao.dto;

import com.audry.desafio_votacao.entities.VoteEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {
    private String cpf;
    private UUID votingAgendaId;
    private VoteEnum vote;
}
