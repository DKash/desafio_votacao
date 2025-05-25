import { useState } from 'react';
import { createVotingAgenda, getVotingAgendaById } from '../api/api';
import '../css/votingAgenda.css';

export default function VotingAgendaManager() {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [message, setMessage] = useState('');
  const [createdAgendaId, setCreatedAgendaId] = useState('');

  const [agendaId, setAgendaId] = useState('');
  const [agendaInfo, setAgendaInfo] = useState<any | null>(null);
  const [fetchError, setFetchError] = useState<string | null>(null);

  const handleCreate = async () => {
    setMessage('');
    
    if (!title.trim() || !description.trim()) {
      setMessage('Título e descrição são obrigatórios.');
      return;
    }

    try {
      const response = await createVotingAgenda({ title, description });
      const id = response?.id || response?.uuid || response?.data?.id || response?.data?.uuid;

      if (!id) {
        throw new Error('ID da pauta não retornado pela API.');
      }
      setCreatedAgendaId(id);
      setMessage('Pauta criada com sucesso!');
      setTitle('');
      setDescription('');
    } catch (error: any) {
      setMessage(error?.response?.data?.message || error?.message || 'Erro ao criar pauta.');
    }
  };

  const handleFetchAgenda = async () => {
    setAgendaInfo(null);
    setFetchError(null);

    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(agendaId)) {
      setFetchError('Formato de UUID inválido.');
      return;
    }

    try {
      const data = await getVotingAgendaById(agendaId);
      if (!data) throw new Error('Nenhuma pauta encontrada com esse ID.');
      setAgendaInfo(data);
    } catch (error: any) {
      setFetchError(error?.response?.data?.message || error?.message || 'Erro ao buscar pauta.');
    }
  };

  return (
    <main className="voting-manager-container">
      <section className="create-agenda-section">
        <h1 className="title">Criar Nova Pauta</h1>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Título da pauta"
          className="input-field"
        />
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Descrição"
          rows={4}
          className="textarea-field"
        />
        <button onClick={handleCreate} className="action-button" disabled={!title.trim() || !description.trim()}>
          Criar
        </button>

        {createdAgendaId && (
          <p className="info-message">
            <strong>ID Criado:</strong> {createdAgendaId}
          </p>
        )}
        {message && (
          <p className={`info-message ${message.includes('sucesso') ? 'success' : 'error'}`}>
            {message}
          </p>
        )}
      </section>

      <hr className="separator" />

      <section className="fetch-agenda-section">
        <h1 className="title">Consultar Pauta</h1>
        <input
          type="text"
          value={agendaId}
          onChange={(e) => setAgendaId(e.target.value)}
          placeholder="UUID da pauta"
          className="input-field"
        />
        <button onClick={handleFetchAgenda} className="action-button" disabled={!agendaId.trim()}>
          Consultar
        </button>

        {fetchError && <p className="info-message error">{fetchError}</p>}

        {agendaInfo && (
          <div className="agenda-info">
            <p><strong>ID:</strong> {agendaInfo.id}</p>
            <p><strong>Título:</strong> {agendaInfo.title}</p>
            <p><strong>Descrição:</strong> {agendaInfo.description}</p>
          </div>
        )}
      </section>
    </main>
  );
}
