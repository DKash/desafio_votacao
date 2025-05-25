package com.audry.desafio_votacao.controller;

import com.audry.desafio_votacao.dto.CpfStatusDto;
import com.audry.desafio_votacao.services.CpfValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cpf-validation")
@RequiredArgsConstructor
/**
 * Controller da validacao de CPF
 */
public class CpfValidationController {

    private final CpfValidationService cpfValidationService;

    @GetMapping("/{cpf}")
    public ResponseEntity<CpfStatusDto> validate(@PathVariable String cpf) {
        return ResponseEntity.ok(cpfValidationService.validateCpf(cpf));
    }
}
