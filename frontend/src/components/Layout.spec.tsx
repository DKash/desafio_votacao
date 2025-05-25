import { render, screen } from '@testing-library/react';
import Layout from './Layout';
import { BrowserRouter } from 'react-router-dom';

describe('Layout Component', () => {
  it('renderiza o Header', () => {
    render(
      <BrowserRouter>
        <Layout>
          <div>Conteúdo de teste</div>
        </Layout>
      </BrowserRouter>
    );

    expect(screen.getByRole('banner')).toBeInTheDocument(); // exemplo de teste para header
  });

  it('renderiza corretamente os children', () => {
    render(
      <BrowserRouter>
        <Layout>
          <div>Conteúdo de teste</div>
        </Layout>
      </BrowserRouter>
    );

    expect(screen.getByText('Conteúdo de teste')).toBeInTheDocument();
  });
});
