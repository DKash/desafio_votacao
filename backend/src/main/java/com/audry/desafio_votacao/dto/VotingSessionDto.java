package com.audry.desafio_votacao.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingSessionDto {
    /**
     * Id da sessao de votacao
     */
    private UUID votingAgendaId;
    /**
     * Se nao for informado, a sessao de votacao sera criada com duracao de 1 minuto
     */
    private Integer duration;
}
