package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.CpfStatusDto;
import com.audry.desafio_votacao.entities.CpfStatusEnum;
import com.audry.desafio_votacao.exception.InvalidCpfException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CpfValidationServiceTest {

    private CpfValidationService cpfValidationService;
    private Random mockRandom;

    @BeforeEach
    void setUp() throws Exception {
        cpfValidationService = new CpfValidationService();
        mockRandom = mock(Random.class);
        Field randomField = CpfValidationService.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(cpfValidationService, mockRandom);
    }

    @Test
    void shouldThrowException_whenCpfIsNull() {
        assertThrows(InvalidCpfException.class, () -> cpfValidationService.validateCpf(null));
    }

    @Test
    void shouldThrowException_whenCpfIsEmpty() {
        assertThrows(InvalidCpfException.class, () -> cpfValidationService.validateCpf(""));
    }

    @Test
    void shouldThrowInvalidCpfException_whenRandomGeneratesInvalid() {
        when(mockRandom.nextInt(10)).thenReturn(2);

        assertThrows(InvalidCpfException.class, () -> {
            cpfValidationService.validateCpf("12345678900");
        });
    }

    @Test
    void shouldNotThrowException_whenRandomIsExactly3() throws Exception {
        when(mockRandom.nextInt(10)).thenReturn(3);
        when(mockRandom.nextBoolean()).thenReturn(true);
        CpfStatusDto result = cpfValidationService.validateCpf("12345678900");
        assertNotNull(result);
    }

    @Test
    void shouldReturnAbleToVote_whenRandomIndicatesAble() {
        when(mockRandom.nextInt(10)).thenReturn(5);
        when(mockRandom.nextBoolean()).thenReturn(true);

        CpfStatusDto result = cpfValidationService.validateCpf("12345678900");

        assertEquals(CpfStatusEnum.ABLE_TO_VOTE, result.getStatus());
    }

    @Test
    void shouldReturnUnableToVote_whenRandomIndicatesUnable() {
        when(mockRandom.nextInt(10)).thenReturn(5);
        when(mockRandom.nextBoolean()).thenReturn(false);

        CpfStatusDto result = cpfValidationService.validateCpf("12345678900");

        assertEquals(CpfStatusEnum.UNABLE_TO_VOTE, result.getStatus());
    }

    @Test
    void shouldNotModifyInputCpf() {
        when(mockRandom.nextInt(10)).thenReturn(5);
        when(mockRandom.nextBoolean()).thenReturn(true);
        String cpf = "12345678900";
        cpfValidationService.validateCpf(cpf);
        assertEquals("12345678900", cpf);
    }
}
