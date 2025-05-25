import { NavLink } from 'react-router-dom';
import '../css/header.css';

export default function Header() {
  return (
    <header className="header">
      <div className="logo">Votação dos cooperados</div>
      <nav className="nav">
        <NavLink to="/" end className="nav-link">Início</NavLink>
        <NavLink to="/voting-agendas" className="nav-link">Pautas</NavLink>
        <NavLink to="/voting-session" className="nav-link">Sessões</NavLink>
        <NavLink to="/votes" className="nav-link">Votos</NavLink>
        <NavLink to="/results" className="nav-link">Resultados</NavLink>
        <NavLink to="/cpf-validator" className="nav-link">Validar CPF</NavLink>
      </nav>
    </header>
  );
}
