package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.entities.CpfStatusEnum;
import com.audry.desafio_votacao.dto.CpfStatusDto;
import com.audry.desafio_votacao.exception.InvalidCpfException;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
/**
 * Fake Client para validação de CPF.
 * Este serviço simula a validação de um CPF e retorna um status de votação.
 */
public class CpfValidationService {

    private final Random random = new Random();

    /**
     * Valida o CPF e retorna o status de votação.
     *
     * @param cpf CPF a ser validado
     * @return Status de votação
     */
    public CpfStatusDto validateCpf(String cpf) {
        // Gera 30% de CPFs inválidos
        if (random.nextInt(10) < 3) {
            throw new InvalidCpfException(cpf);
        }

        boolean ableToVote = random.nextBoolean();
        return CpfStatusDto.builder()
                .status(ableToVote ? CpfStatusEnum.ABLE_TO_VOTE : CpfStatusEnum.UNABLE_TO_VOTE)
                .build();
    }
}
