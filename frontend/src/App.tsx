/* c8 ignore file */
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import CreateVotingAgenda from './pages/CreateVotingAgenda';
import OpenVotingSession from './pages/OpenVotingSession';
import Vote from './pages/Vote';
import VotingResult from './pages/VotingResult';
import CpfValidator from './pages/CpfValidator';

export default function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/voting-agendas" element={<CreateVotingAgenda />} />
          <Route path="/voting-session" element={<OpenVotingSession />} />
          <Route path="/votes" element={<Vote />} />
          <Route path="/results" element={<VotingResult />} />
          <Route path="/cpf-validator" element={<CpfValidator />} />
        </Routes>
      </Layout>
    </Router>
  );
}
