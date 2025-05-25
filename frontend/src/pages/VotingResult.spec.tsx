import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import VotingResult from './VotingResult';
import * as api from '../api/api';
import { vi } from 'vitest';

vi.mock('../api/api');

describe('VotingResult', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renderiza campos e botão corretamente', () => {
    render(<VotingResult />);
    expect(screen.getByPlaceholderText('UUID da pauta')).toBeInTheDocument();
    expect(screen.getByText('Consultar')).toBeInTheDocument();
  });

  it('exibe erro com UUID inválido', async () => {
    render(<VotingResult />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), {
      target: { value: '123' },
    });
    fireEvent.click(screen.getByText('Consultar'));

    await waitFor(() => {
      expect(screen.getByText('Formato de UUID inválido.')).toBeInTheDocument();
    });
  });

  it('exibe resultado da votação com sucesso', async () => {
    (api.getVotingResult as vi.Mock).mockResolvedValue({
      qtdVotosSim: 10,
      qtdVotosNão: 5,
      resultado: 'Aprovado',
    });

    render(<VotingResult />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), {
      target: { value: '123e4567-e89b-12d3-a456-426614174000' },
    });
    fireEvent.click(screen.getByText('Consultar'));

    await waitFor(() => {
      expect(screen.getByText('Total de Votos:')).toBeInTheDocument();
      expect(screen.getByText('10')).toBeInTheDocument();
      expect(screen.getByText('5')).toBeInTheDocument();
      expect(screen.getByText('Aprovado')).toBeInTheDocument();
    });
  });

  it('exibe mensagem de erro ao falhar na API', async () => {
    (api.getVotingResult as vi.Mock).mockRejectedValue({
      response: { data: { message: 'Erro ao buscar resultado.' } },
    });

    render(<VotingResult />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), {
      target: { value: '123e4567-e89b-12d3-a456-426614174000' },
    });
    fireEvent.click(screen.getByText('Consultar'));

    await waitFor(() => {
      expect(screen.getByText('Erro ao buscar resultado.')).toBeInTheDocument();
    });
  });

  it('exibe botão de carregamento corretamente', async () => {
    let resolveFn: (value?: any) => void = () => {};
    const promise = new Promise((resolve) => {
      resolveFn = resolve;
    });

    (api.getVotingResult as vi.Mock).mockReturnValue(promise);

    render(<VotingResult />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), {
      target: { value: '123e4567-e89b-12d3-a456-426614174000' },
    });
    fireEvent.click(screen.getByText('Consultar'));

    expect(screen.getByText('Carregando...')).toBeInTheDocument();

    resolveFn({
      qtdVotosSim: 5,
      qtdVotosNão: 3,
      resultado: 'Aprovado',
    });

    await waitFor(() => {
      expect(screen.getByText('Total de Votos:')).toBeInTheDocument();
    });
  });
});
