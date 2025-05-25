import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import CpfValidator from './CpfValidator';
import * as api from '../api/api';
import { vi } from 'vitest';

vi.mock('../api/api');

describe('CpfValidator', () => {
  const mockValidateCpf = api.validateCpf as vi.Mock;

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renderiza o título e os campos corretamente', () => {
    render(<CpfValidator />);
    expect(screen.getByText('Validar CPF')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Digite o CPF')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /validar/i })).toBeDisabled();
  });

  it('ativa o botão quando CPF é digitado', () => {
    render(<CpfValidator />);
    const input = screen.getByPlaceholderText('Digite o CPF');
    fireEvent.change(input, { target: { value: '12345678900' } });
    expect(screen.getByRole('button', { name: /validar/i })).toBeEnabled();
  });

  it('mostra mensagem de sucesso para CPF apto', async () => {
    mockValidateCpf.mockResolvedValueOnce({ data: { status: 'ABLE_TO_VOTE' } });

    render(<CpfValidator />);
    fireEvent.change(screen.getByPlaceholderText('Digite o CPF'), {
      target: { value: '12345678900' },
    });
    fireEvent.click(screen.getByRole('button', { name: /validar/i }));

    await waitFor(() => {
      expect(screen.getByText('ABLE_TO_VOTE')).toBeInTheDocument();
    });
  });

  it('mostra mensagem de erro para CPF não apto', async () => {
    mockValidateCpf.mockResolvedValueOnce({ data: { status: 'UNABLE_TO_VOTE' } });

    render(<CpfValidator />);
    fireEvent.change(screen.getByPlaceholderText('Digite o CPF'), {
      target: { value: '98765432100' },
    });
    fireEvent.click(screen.getByRole('button', { name: /validar/i }));

    await waitFor(() => {
      expect(screen.getByText('UNABLE_TO_VOTE')).toBeInTheDocument();
    });
  });

  it('exibe mensagem de erro em caso de falha na API', async () => {
    mockValidateCpf.mockRejectedValueOnce({
      response: { data: { message: 'Erro de validação' } },
    });

    render(<CpfValidator />);
    fireEvent.change(screen.getByPlaceholderText('Digite o CPF'), {
      target: { value: '00000000000' },
    });
    fireEvent.click(screen.getByRole('button', { name: /validar/i }));

    await waitFor(() => {
      expect(screen.getByText('Erro de validação')).toBeInTheDocument();
    });
  });
});
