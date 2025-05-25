import { useState } from 'react';
import { castVote } from '../api/api';
import '../css/vote.css';

export default function Vote() {
  const [agendaId, setAgendaId] = useState('');
  const [cpf, setCpf] = useState('');
  const [vote, setVote] = useState<'Sim' | 'Não'>('Sim');
  const [message, setMessage] = useState('');

  const handleVote = async () => {
    try {
      await castVote({
        votingAgendaId: agendaId,
        cpf,
        vote,
      });
      setMessage('Voto enviado com sucesso!');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erro ao enviar voto.');
    }
  };

  return (
    <div className="vote-container">
      <h2>Votar</h2>
      <input
        type="text"
        value={agendaId}
        onChange={(e) => setAgendaId(e.target.value)}
        placeholder="UUID da pauta"
        className="input"
      />
      <input
        type="text"
        value={cpf}
        onChange={(e) => setCpf(e.target.value)}
        placeholder="CPF"
        className="input"
      />
      <select
        value={vote}
        onChange={(e) => setVote(e.target.value as 'Sim' | 'Não')}
        className="input"
      >
        <option value="Sim">Sim</option>
        <option value="Não">Não</option>
      </select>
      <button onClick={handleVote} className="button">Votar</button>
      {message && <p className="message">{message}</p>}
    </div>
  );
}
