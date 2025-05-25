import { useState } from 'react';
import { getVotingResult } from '../api/api';
import '../css/votingResult.css';

interface VotingResultData {
  qtdVotosSim: number;
  qtdVotosNão: number;
  resultado: string;
}

export default function VotingResult() {
  const [agendaId, setAgendaId] = useState('');
  const [result, setResult] = useState<VotingResultData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleFetchResult = async () => {
    setResult(null);
    setError(null);

    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(agendaId.trim())) {
      setError('Formato de UUID inválido.');
      return;
    }

    setLoading(true);

    try {
      const response = await getVotingResult(agendaId.trim());
      setResult(response);
    } catch (error: any) {
      setError(error?.response?.data?.message || 'Erro ao buscar resultado.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h2>Resultado da Votação</h2>

      <input
        type="text"
        value={agendaId}
        onChange={(e) => setAgendaId(e.target.value)}
        placeholder="UUID da pauta"
        className="input"
        disabled={loading}
      />

      <button onClick={handleFetchResult} className="button" disabled={loading}>
        {loading ? 'Carregando...' : 'Consultar'}
      </button>

      {error && <p className="error-message">{error}</p>}

      {result && (
        <div className="result">
          <p><strong>Total de Votos:</strong> {result.qtdVotosSim + result.qtdVotosNão}</p>
          <p><strong>Votos com Sim:</strong> {result.qtdVotosSim}</p>
          <p><strong>Votos com Não:</strong> {result.qtdVotosNão}</p>
          <p><strong>Resultado:</strong> {result.resultado}</p>
        </div>
      )}
    </div>
  );
}
