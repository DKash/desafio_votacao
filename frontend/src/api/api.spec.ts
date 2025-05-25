import { describe, it, expect, vi } from 'vitest';
import axios from 'axios';
import {
  validateCpf,
  createVotingAgenda,
  getVotingAgendaById,
  openVotingSession,
  getSessionByVotingAgendaId,
  castVote,
  getVotingResult,
} from './api';

vi.mock('axios');

describe('API functions', () => {
  it('validateCpf calls axios.get with correct URL', async () => {
    const mockedGet = vi.mocked(axios.get);
    mockedGet.mockResolvedValue({ data: 'valid' });

    const cpf = '12345678900';
    const response = await validateCpf(cpf);

    expect(mockedGet).toHaveBeenCalledWith(`/api/v1/cpf-validation/${cpf}`);
    expect(response.data).toBe('valid');
  });

  it('createVotingAgenda calls axios.post with correct URL and data', async () => {
    const mockedPost = vi.mocked(axios.post);
    mockedPost.mockResolvedValue({ data: { id: 1 } });

    const data = { title: 'Test', description: 'Desc' };
    const response = await createVotingAgenda(data);

    expect(mockedPost).toHaveBeenCalledWith('/api/v1/voting-agendas', data);
    expect(response.data).toEqual({ id: 1 });
  });

  it('getVotingAgendaById calls axios.get and returns data', async () => {
    const mockedGet = vi.mocked(axios.get);
    mockedGet.mockResolvedValue({ data: { id: '1', title: 'Test' } });

    const id = '1';
    const response = await getVotingAgendaById(id);

    expect(mockedGet).toHaveBeenCalledWith(`api/v1/voting-agendas/${id}`);
    expect(response).toEqual({ id: '1', title: 'Test' });
  });

  it('openVotingSession calls axios.post with correct URL and data', async () => {
    const mockedPost = vi.mocked(axios.post);
    mockedPost.mockResolvedValue({ data: { sessionId: 1 } });

    const data = { votingAgendaId: '1', duration: 30 };
    const response = await openVotingSession(data);

    expect(mockedPost).toHaveBeenCalledWith('/api/v1/sessions', data);
    expect(response.data).toEqual({ sessionId: 1 });
  });

  it('getSessionByVotingAgendaId calls axios.get and returns data', async () => {
    const mockedGet = vi.mocked(axios.get);
    mockedGet.mockResolvedValue({ data: { sessionId: '1' } });

    const votingAgendaId = '1';
    const response = await getSessionByVotingAgendaId(votingAgendaId);

    expect(mockedGet).toHaveBeenCalledWith(`api/v1/sessions/voting-session/${votingAgendaId}`);
    expect(response).toEqual({ sessionId: '1' });
  });

  it('castVote calls axios.post with correct URL and data', async () => {
    const mockedPost = vi.mocked(axios.post);
    mockedPost.mockResolvedValue({ data: { voteId: 1 } });

    const data = { votingAgendaId: '1', cpf: '123', vote: 'Sim' as const };
    const response = await castVote(data);

    expect(mockedPost).toHaveBeenCalledWith('/api/v1/votes', data);
    expect(response.data).toEqual({ voteId: 1 });
  });

  it('getVotingResult calls axios.get and returns data', async () => {
    const mockedGet = vi.mocked(axios.get);
    mockedGet.mockResolvedValue({ data: { result: 'approved' } });

    const votingAgendaId = '1';
    const response = await getVotingResult(votingAgendaId);

    expect(mockedGet).toHaveBeenCalledWith(`/api/v1/votes/result/${votingAgendaId}`);
    expect(response).toEqual({ result: 'approved' });
  });
});
