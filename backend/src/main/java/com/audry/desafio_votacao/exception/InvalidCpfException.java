package com.audry.desafio_votacao.exception;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException(String cpf) {
        super("O CPF: " + cpf + " não é valido");
    }
}
