import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Vote from './Vote';
import * as api from '../api/api';
import { vi } from 'vitest';

vi.mock('../api/api');

describe('Vote', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renderiza os campos e botão corretamente', () => {
    render(<Vote />);

    expect(screen.getByPlaceholderText('UUID da pauta')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('CPF')).toBeInTheDocument();
    expect(screen.getByText('Sim')).toBeInTheDocument();
    expect(screen.getByText('Não')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Votar' })).toBeInTheDocument();
  });

  it('permite votar com sucesso', async () => {
    (api.castVote as vi.Mock).mockResolvedValue({});

    render(<Vote />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), { target: { value: '1234' } });
    fireEvent.change(screen.getByPlaceholderText('CPF'), { target: { value: '12345678900' } });
    fireEvent.change(screen.getByDisplayValue('Sim'), { target: { value: 'Não' } });
    fireEvent.click(screen.getByRole('button', { name: 'Votar' }));

    await waitFor(() => {
      expect(screen.getByText('Voto enviado com sucesso!')).toBeInTheDocument();
    });
  });

  it('exibe mensagem de erro se envio falhar', async () => {
    (api.castVote as vi.Mock).mockRejectedValue({
      response: { data: { message: 'Erro ao votar.' } },
    });

    render(<Vote />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), { target: { value: '1234' } });
    fireEvent.change(screen.getByPlaceholderText('CPF'), { target: { value: '00000000000' } });
    fireEvent.click(screen.getByRole('button', { name: 'Votar' }));

    await waitFor(() => {
      expect(screen.getByText('Erro ao votar.')).toBeInTheDocument();
    });
  });

  it('envia valor padrão do voto como "Sim"', async () => {
    (api.castVote as vi.Mock).mockResolvedValue({});

    render(<Vote />);
    fireEvent.change(screen.getByPlaceholderText('UUID da pauta'), { target: { value: 'abc123' } });
    fireEvent.change(screen.getByPlaceholderText('CPF'), { target: { value: '98765432100' } });

    const select = screen.getByDisplayValue('Sim') as HTMLSelectElement;
    expect(select.value).toBe('Sim');

    fireEvent.click(screen.getByRole('button', { name: 'Votar' }));

    await waitFor(() => {
      expect(api.castVote).toHaveBeenCalledWith({
        votingAgendaId: 'abc123',
        cpf: '98765432100',
        vote: 'Sim',
      });
    });
  });
});
