package com.audry.desafio_votacao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Associated associated;

    @ManyToOne
    private VotingSession session;

    @Enumerated(EnumType.STRING)
    private VoteEnum vote;
}
