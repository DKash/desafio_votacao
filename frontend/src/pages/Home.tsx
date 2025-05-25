import '../css/home.css';

export default function Home() {
  return (
    <main className="home-container">
      <section className="welcome-section">
        <h1 className="title">Seja Bem-Vindo(a)</h1>

        <p className="description main-description">
          Aqui você poderá cadastrar pautas, abrir sessões de votação, votar nas pautas, consultar resultados e validar CPF.
        </p>

        <p className="description">
          Para começar, clique nas abas acima para navegar pelas funcionalidades.
        </p>

        <p className="description">
          Caso tenha alguma dúvida, entre em contato com o suporte.
        </p>

        <p className="description">
          Agradecemos por utilizar nosso sistema de votação!
        </p>
      </section>
    </main>
  );
}
