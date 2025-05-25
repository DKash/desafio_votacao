import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import CreateVotingAgenda from './CreateVotingAgenda';
import * as api from '../api/api';
import { vi, describe, it, expect, beforeEach } from 'vitest';

vi.mock('../api/api');

describe('CreateVotingAgenda', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('desabilita o botão de criação com campos vazios', () => {
    render(<CreateVotingAgenda />);
    const button = screen.getByRole('button', { name: /criar/i });
    expect(button).toBeDisabled();
  });

  it('cria pauta com sucesso', async () => {
    const fakeId = '12345';
    (api.createVotingAgenda as any).mockResolvedValue({ id: fakeId });

    render(<CreateVotingAgenda />);

    fireEvent.change(screen.getByPlaceholderText(/título da pauta/i), { target: { value: 'Título Teste' } });
    fireEvent.change(screen.getByPlaceholderText(/descrição/i), { target: { value: 'Descrição Teste' } });

    fireEvent.click(screen.getByRole('button', { name: /criar/i }));

    expect(api.createVotingAgenda).toHaveBeenCalledWith({
      title: 'Título Teste',
      description: 'Descrição Teste',
    });

    expect(await screen.findByText(/pauta criada com sucesso!/i)).toBeInTheDocument();
    expect(screen.getByText(new RegExp(fakeId))).toBeInTheDocument();
  });

  it('exibe mensagem de erro quando API não retorna ID ao criar pauta', async () => {
    (api.createVotingAgenda as vi.Mock).mockResolvedValue({});
    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/título da pauta/i), { target: { value: 'Título válido' } });
    fireEvent.change(screen.getByPlaceholderText(/descrição/i), { target: { value: 'Descrição válida' } });
    fireEvent.click(screen.getByRole('button', { name: /criar/i }));
    expect(await screen.findByText(/id da pauta não retornado pela api/i)).toBeInTheDocument();
  });

  it('exibe erro ao falhar na criação da pauta', async () => {
    (api.createVotingAgenda as any).mockRejectedValue({
      response: { data: { message: 'Erro customizado' } }
    });

    render(<CreateVotingAgenda />);

    fireEvent.change(screen.getByPlaceholderText(/título da pauta/i), { target: { value: 'Título Teste' } });
    fireEvent.change(screen.getByPlaceholderText(/descrição/i), { target: { value: 'Descrição Teste' } });

    fireEvent.click(screen.getByRole('button', { name: /criar/i }));

    expect(await screen.findByText(/erro customizado/i)).toBeInTheDocument();
  });

  it('exibe mensagem de erro ao falhar na criação da pauta', async () => {
    (api.createVotingAgenda as any).mockRejectedValueOnce(new Error('Erro inesperado'));

    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/título da pauta/i), { target: { value: 'Teste' } });
    fireEvent.change(screen.getByPlaceholderText(/descrição/i), { target: { value: 'Descrição teste' } });
    fireEvent.click(screen.getByRole('button', { name: /criar/i }));

    expect(await screen.findByText(/erro inesperado/i)).toBeInTheDocument();
  });

  it('exibe erro ao tentar consultar com UUID inválido', async () => {
    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/uuid da pauta/i), { target: { value: 'invalido' } });
    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));
    expect(await screen.findByText('Formato de UUID inválido.')).toBeInTheDocument();
  });

  it('consulta pauta com sucesso', async () => {
    const fakeData = {
      id: '123e4567-e89b-12d3-a456-426614174000',
      title: 'Pauta Teste',
      description: 'Descrição da pauta'
    };
    (api.getVotingAgendaById as any).mockResolvedValue(fakeData);

    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/uuid da pauta/i), { target: { value: fakeData.id } });
    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    await waitFor(() => {
      expect(api.getVotingAgendaById).toHaveBeenCalledWith(fakeData.id);
    });

    expect(await screen.findByText(new RegExp(fakeData.id))).toBeInTheDocument();
    expect(screen.getByText(new RegExp(fakeData.title))).toBeInTheDocument();
    expect(screen.getByText(new RegExp(fakeData.description))).toBeInTheDocument();
  });

  it('exibe erro ao falhar na consulta da pauta', async () => {
    (api.getVotingAgendaById as any).mockRejectedValue({
      response: { data: { message: 'Erro na consulta' } }
    });

    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/uuid da pauta/i), { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });
    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    expect(await screen.findByText(/erro na consulta/i)).toBeInTheDocument();
  });

  it('exibe erro quando API retorna dados vazios ao consultar pauta', async () => {
    (api.getVotingAgendaById as any).mockResolvedValueOnce(null);

    render(<CreateVotingAgenda />);
    fireEvent.change(screen.getByPlaceholderText(/uuid da pauta/i), { target: { value: '123e4567-e89b-12d3-a456-426614174000' } });
    fireEvent.click(screen.getByRole('button', { name: /consultar/i }));

    expect(await screen.findByText(/nenhuma pauta encontrada com esse id/i)).toBeInTheDocument();
  });
});
