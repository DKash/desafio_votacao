import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Header from './Header';

describe('Header Component', () => {
  it('renderiza os links corretamente', () => {
    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );

    expect(screen.getByText(/Votação dos cooperados/i)).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Início/i })).toHaveAttribute('href', '/');
    expect(screen.getByRole('link', { name: /Pautas/i })).toHaveAttribute('href', '/voting-agendas');
    expect(screen.getByRole('link', { name: /Sessões/i })).toHaveAttribute('href', '/voting-session');
    expect(screen.getByRole('link', { name: /Votos/i })).toHaveAttribute('href', '/votes');
    expect(screen.getByRole('link', { name: /Resultados/i })).toHaveAttribute('href', '/results');
    expect(screen.getByRole('link', { name: /Validar CPF/i })).toHaveAttribute('href', '/cpf-validator');
  });

  it('link ativo tem destaque correto', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <Header />
      </MemoryRouter>
    );

    const inicioLink = screen.getByRole('link', { name: /Início/i });
    expect(inicioLink.getAttribute('aria-current')).toBe('page');
  });
});
