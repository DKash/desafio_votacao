package com.audry.desafio_votacao.dto;

import com.audry.desafio_votacao.entities.CpfStatusEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Bonus: Verifica se o CPF do cooperado esta apto a votar
 */
public class CpfStatusDto {
    /**
     * Status do CPF do cooperado
     * |-> ABLE_TO_VOTE
     * |-> UNABLE_TO_VOTE
     */
    private CpfStatusEnum status;
}
