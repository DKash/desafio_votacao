package com.audry.desafio_votacao.services;

import com.audry.desafio_votacao.dto.VotingResultDto;
import com.audry.desafio_votacao.dto.VoteDto;
import com.audry.desafio_votacao.entities.*;
import com.audry.desafio_votacao.repository.AssociatedRepository;
import com.audry.desafio_votacao.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.audry.desafio_votacao.exception.AssociatedUnableToVoteException;
import com.audry.desafio_votacao.exception.VotingSessionClosedException;
import com.audry.desafio_votacao.exception.AssociatedAlreadyVotedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final AssociatedRepository associatedRepository;
    private final VoteRepository voteRepository;
    private final VotingSessionService votingSessionService;
    private final CpfValidationService cpfValidationService;

    /**
     * Metodo responsavel por registrar o voto de um associado em uma sessão de votacao.
     * @param dto Objeto contendo as informacoes do voto.
     * @throws RuntimeException Se o CPF nao for valido, se a sessao ja tiver terminado ou se o associado ja tiver votado.
     */
    public void castVote(VoteDto dto) {
        // Valida o CPF do cooperado
        var status = cpfValidationService.validateCpf(dto.getCpf());
        if (status.getStatus() != CpfStatusEnum.ABLE_TO_VOTE) {
            throw new AssociatedUnableToVoteException();
        }

        // Consulta no banco se o cooperado ja existe, se nao existir cria um novo
        Associated associated = associatedRepository.findByCpf(dto.getCpf())
                .orElseGet(() -> associatedRepository.save(
                        Associated.builder().cpf(dto.getCpf()).build()));

        // Consulta a sessao da pauta
        VotingSession session = votingSessionService.findByVotingAgenda(dto.getVotingAgendaId());

        // Verifica se a sessao ja terminou
        if (LocalDateTime.now().isAfter(session.getDtEnd())) {
            if (!session.isFinished()) {
                session.setFinished(true);
                // Atualiza a sessao como finalizada
                votingSessionService.finishVotingSession(session);
            }
            throw new VotingSessionClosedException();
        }

        // Verifica se o cooperado ja votou
        if (voteRepository.findByAssociatedAndSession(associated, session).isPresent()) {
            throw new AssociatedAlreadyVotedException();
        }

        Vote vote = Vote.builder()
                .associated(associated)
                .session(session)
                .vote(dto.getVote())
                .build();

        voteRepository.save(vote);
    }

    /**
     * Retorna o resultado da votacao de uma sessao
     * @param votingAgendaId ID da pauta
     */
    public VotingResultDto getVotingResult(UUID votingAgendaId) {
        VotingSession session = votingSessionService.findByVotingAgenda(votingAgendaId);
        List<Vote> voteList = voteRepository.findAllBySession(session);

        long yes = voteList.stream().filter(v -> v.getVote() == VoteEnum.Sim).count();
        long no  = voteList.stream().filter(v -> v.getVote() == VoteEnum.Não).count();

        String result = "EMPATE";
        if (yes > no) {
            result = "APROVADA";
        } else if (no > yes) {
            result = "REPROVADA";
        } else if (yes == 0 && no == 0) {
            result = "NAO REALIZADA";
        }

        return VotingResultDto.builder()
                .yesCount(yes)
                .noCount(no)
                .result(result)
                .build();
    }
}
