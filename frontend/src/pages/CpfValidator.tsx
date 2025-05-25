import { useState } from 'react';
import { validateCpf } from '../api/api';
import '../css/cpfValidator.css';

export default function CpfValidator() {
  const [cpf, setCpf] = useState('');
  const [message, setMessage] = useState('');

  const handleValidateCpf = async () => {
    try {
      const response = await validateCpf(cpf);
      const status = response.data?.status;
      setMessage(status);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erro ao validar CPF.');
    }
  };

  return (
    <main className="cpf-validator-container">
      <section className="welcome-section">
        <h1 className="title">Validar CPF</h1>

        <input
          type="text"
          value={cpf}
          onChange={(e) => setCpf(e.target.value)}
          placeholder="Digite o CPF"
          className="cpf-input"
        />

        <button onClick={handleValidateCpf} className="validate-button" disabled={!cpf.trim()}>
          Validar
        </button>

        {message && (
          <p className={`description ${message.includes('apto') ? 'success' : 'error'}`}>
            {message}
          </p>
        )}
      </section>
    </main>
  );
}
