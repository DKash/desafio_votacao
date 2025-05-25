package com.audry.desafio_votacao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingAgendaDto {
    private String title;
    private String description;
}
