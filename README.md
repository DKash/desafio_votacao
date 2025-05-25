# Sistema de Votação de Cooperado — Desafio Técnico

Este projeto consiste em uma aplicação completa de votação, desenvolvida como desafio técnico. Ele permite a criação de pautas, abertura de sessões de votação, registro de votos e apuração de resultados. A solução foi construída com foco em boas práticas de arquitetura, testes, escalabilidade e experiência do usuário.

## 🔧 Tecnologias Utilizadas

### Backend (Java + Spring Boot)
- Java 21
- Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL
- JUnit 5 e Mockito (testes unitários)
- Docker e Docker Compose
- Swagger para documentação da API

### Frontend (React + Vite)
- React 18 com Vite
- TypeScript
- Material UI (MUI)
- ESLint 9
- Vitest + Testing Library (testes unitários)
- React Router DOM (suporte à navegação)
- React Hook Form (gestão de formulários)
- Axios

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados
- Node.js (recomenda-se versão 18+)
- Java 21
- Git

### 1. Clonar o Repositório

```bash
git clone https://github.com/DKash/desafio_votacao.git
cd desafio_votacao
```

### 2.  Subir o Ambiente com Docker junto com o Banco de Dados (PostgreSQL)

```bash
docker-compose up --build
```

### 3. Backend

```bash
cd backend
./mvnw spring-boot:run
```

> A API estará disponível em: `http://localhost:8080`
> Swagger UI (Documentação da API): http://localhost:8080/swagger-ui/index.html
### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

> A aplicação estará disponível em: `http://localhost:5173`

---

## 🧪 Executando os Testes

### Backend

```bash
cd backend
./mvnw test
```

### Frontend

```bash
cd frontend
npm run test
```

---

## 🧹 Verificação de Código

O lint foi configurado com ESLint 9:

```bash
cd frontend
npm run lint
```

---

## 📌 Funcionalidades Implementadas

### Backend
- Criar pauta com título e descrição
- Abrir sessão de votação com tempo customizável
- Registrar votos (com validação de CPF e voto único)
- Apuração do resultado ao final da sessão
- Testes unitários para services e controllers

### Frontend
- Interface limpa para consulta de resultados
- Validação de UUID
- Visualização do total de votos, votos "Sim" e "Não"
- Indicação de resultado final com mensagem clara
- Feedbacks visuais (carregamento e erros)

---

## 🚀 Sobre o Desafio

Este projeto foi desenvolvido como parte de um desafio técnico proposto. A solução tem como foco demonstrar domínio em arquitetura de software, integração com banco de dados, testes automatizados, domínio das linguagens de programação.

---

## 👨‍💻 Desenvolvido por

**Audry Martins**  
[LinkedIn](https://www.linkedin.com/in/audrymartins) | [GitHub](https://github.com/DKash)