package com.audry.desafio_votacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingResultDto {
    /**
     * Total de votos sim
     */
    @JsonProperty("qtdVotosSim")
    private long yesCount;
    /**
     * Total de votos nao
     */
    @JsonProperty("qtdVotosNão")
    private long noCount;
    /**
     * Resultado da votacao
     */
    @JsonProperty("resultado")
    private String result;
}
