import { useState } from 'react';
import { openVotingSession, getSessionByVotingAgendaId } from '../api/api';
import '../css/openVotingSession.css';

interface SessionInfo {
  id: string;
  votingAgenda: {
    title: string;
  };
  dtStart: string;
  dtEnd: string;
  finished: boolean;
}

export default function OpenVotingSession() {
  const [agendaId, setAgendaId] = useState('');
  const [duration, setDuration] = useState('');
  const [message, setMessage] = useState('');
  const [consultaAgendaId, setConsultaAgendaId] = useState('');
  const [sessionInfo, setSessionInfo] = useState<SessionInfo | null>(null);
  const [consultaErro, setConsultaErro] = useState<string | null>(null);

  const handleOpenSession = async () => {
    setMessage('');
    try {
      const response = await openVotingSession({
        votingAgendaId: agendaId,
        duration: duration ? parseInt(duration, 10) : undefined,
      });
      setMessage(response?.message || 'Sessão aberta com sucesso!');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erro ao abrir sessão.');
      console.error(error);
    }
  };

  const handleConsultarSessao = async () => {
    setSessionInfo(null);
    setConsultaErro(null);

    const uuidRegex =
      /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(consultaAgendaId.trim())) {
      setConsultaErro('Formato de UUID inválido.');
      return;
    }

    try {
      const data = await getSessionByVotingAgendaId(consultaAgendaId.trim());
      setSessionInfo(data);
    } catch (error: any) {
      setConsultaErro(error.response?.data?.message || 'Erro ao consultar sessão.');
      console.error(error);
    }
  };

  return (
    <div className="session-container">
      <section className="session-section">
        <h2>Abrir Sessão</h2>
        <input
          type="text"
          value={agendaId}
          onChange={(e) => setAgendaId(e.target.value)}
          placeholder="UUID da pauta"
          className="input"
        />
        <input
          type="text"
          value={duration}
          onChange={(e) => setDuration(e.target.value)}
          placeholder="Duração (min)"
          className="input"
        />
        <button onClick={handleOpenSession} className="button">
          Abrir Sessão
        </button>
        {message && <p className="message">{message}</p>}
      </section>

      <hr className="divider" />

      <section className="session-section">
        <h2>Consultar Sessão</h2>
        <input
          type="text"
          value={consultaAgendaId}
          onChange={(e) => setConsultaAgendaId(e.target.value)}
          placeholder="UUID da pauta"
          className="input"
        />
        <button onClick={handleConsultarSessao} className="button">
          Consultar
        </button>

        {consultaErro && <p className="error-message">{consultaErro}</p>}

        {sessionInfo && (
          <div className="session-info">
            <p><strong>ID da Sessão:</strong> {sessionInfo.id}</p>
            <p><strong>Pauta:</strong> {sessionInfo.votingAgenda.title}</p>
            <p><strong>Início da Sessão:</strong> {new Date(sessionInfo.dtStart).toLocaleString()}</p>
            <p><strong>Fim da Sessão:</strong> {new Date(sessionInfo.dtEnd).toLocaleString()}</p>
            <p><strong>Situação:</strong> {sessionInfo.finished ? "Encerrada" : "Aberta"}</p>
          </div>
        )}
      </section>
    </div>
  );
}
