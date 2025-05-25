import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import OpenVotingSession from './OpenVotingSession';
import * as api from '../api/api';
import { vi, describe, it, expect, beforeEach } from 'vitest';

vi.mock('../api/api');

describe('OpenVotingSession', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('exibe mensagem de sucesso ao abrir sessão', async () => {
    (api.openVotingSession as any).mockResolvedValue({ message: 'Sessão aberta com sucesso!' });

    render(<OpenVotingSession />);

    fireEvent.change(screen.getAllByPlaceholderText(/uuid da pauta/i)[0], { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });
    fireEvent.change(screen.getByPlaceholderText(/duração \(min\)/i), { target: { value: '30' } });

    fireEvent.click(screen.getByRole('button', { name: /abrir sessão/i }));

    await waitFor(() => {
      expect(api.openVotingSession).toHaveBeenCalledWith({
        votingAgendaId: '123e4567-e89b-12d3-a456-426614174000',
        duration: 30,
      });
    });

    expect(await screen.findByText(/sessão aberta com sucesso!/i)).toBeDefined();
  });

  it('exibe mensagem de erro ao falhar ao abrir sessão', async () => {
    (api.openVotingSession as any).mockRejectedValue({
      response: { data: { message: 'Erro ao abrir sessão.' } },
    });

    render(<OpenVotingSession />);

    fireEvent.change(screen.getAllByPlaceholderText(/uuid da pauta/i)[0], { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });
    fireEvent.change(screen.getByPlaceholderText(/duração \(min\)/i), { target: { value: '30' } });

    fireEvent.click(screen.getByRole('button', { name: /abrir sessão/i }));

    expect(await screen.findByText(/erro ao abrir sessão\./i)).toBeDefined();
  });

  it('exibe erro ao consultar com UUID inválido', async () => {
    render(<OpenVotingSession />);

    fireEvent.change(screen.getAllByPlaceholderText(/uuid da pauta/i)[1], { target: { value: 'uuid-invalido' } });

    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    expect(await screen.findByText(/formato de uuid inválido\./i)).toBeDefined();
  });

  it('exibe informações da sessão ao consultar com sucesso', async () => {
    const fakeSession = {
      id: 'session-uuid-123',
      votingAgenda: { title: 'Pauta Teste' },
      dtStart: '2025-05-24T10:00:00Z',
      dtEnd: '2025-05-24T10:30:00Z',
      finished: false,
    };
    (api.getSessionByVotingAgendaId as any).mockResolvedValue(fakeSession);

    render(<OpenVotingSession />);

    fireEvent.change(screen.getAllByPlaceholderText(/uuid da pauta/i)[1], { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });

    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    await waitFor(() => {
      expect(api.getSessionByVotingAgendaId).toHaveBeenCalledWith('123e4567-e89b-12d3-a456-426614174000');
    });

    expect(await screen.findByText(/id da sessão:/i)).toBeDefined();
    expect(screen.getByText(/session-uuid-123/i)).toBeDefined();
    expect(screen.getByText(/pauta teste/i)).toBeDefined();
    expect(screen.getByText(/situação:/i)).toBeDefined();
    expect(screen.getByText(/aberta/i)).toBeDefined();
  });

  it('exibe mensagem de erro ao falhar ao consultar sessão', async () => {
    (api.getSessionByVotingAgendaId as any).mockRejectedValue({
      response: { data: { message: 'Erro ao consultar sessão.' } },
    });

    render(<OpenVotingSession />);

    fireEvent.change(screen.getAllByPlaceholderText(/uuid da pauta/i)[1], { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });

    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    expect(await screen.findByText(/erro ao consultar sessão\./i)).toBeDefined();
  });
});
