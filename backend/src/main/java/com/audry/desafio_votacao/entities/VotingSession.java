package com.audry.desafio_votacao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private VotingAgenda votingAgenda;

    private LocalDateTime dtStart;

    private LocalDateTime dtEnd;

    private boolean finished;
}
