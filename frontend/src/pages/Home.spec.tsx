import { render, screen } from '@testing-library/react';
import Home from './Home';

describe('Home', () => {
  it('renderiza o título de boas-vindas', () => {
    render(<Home />);
    expect(screen.getByText(/Seja Bem-Vindo\(a\)/i)).toBeInTheDocument();
  });

  it('exibe todas as mensagens da página inicial', () => {
    render(<Home />);

    expect(screen.getByText(/Aqui você poderá cadastrar pautas/i)).toBeInTheDocument();
    expect(screen.getByText(/Para começar, clique nas abas acima/i)).toBeInTheDocument();
    expect(screen.getByText(/Caso tenha alguma dúvida, entre em contato/i)).toBeInTheDocument();
    expect(screen.getByText(/Agradecemos por utilizar nosso sistema de votação/i)).toBeInTheDocument();
  });

  it('possui a classe correta no container principal', () => {
    const { container } = render(<Home />);
    expect(container.querySelector('.home-container')).toBeInTheDocument();
  });
});
