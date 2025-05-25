package com.audry.desafio_votacao.repository;

import com.audry.desafio_votacao.entities.Associated;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssociatedRepository extends JpaRepository<Associated, UUID> {
    Optional<Associated> findByCpf(String cpf);
}
