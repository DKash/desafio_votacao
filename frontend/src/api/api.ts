import axios from 'axios';

export const validateCpf = (cpf: string) =>
  axios.get(`/api/v1/cpf-validation/${cpf}`);

export const createVotingAgenda = (data: { title: string; description: string }) =>
  axios.post('/api/v1/voting-agendas', data);

export const getVotingAgendaById = async (id: string) =>{
  const response = await axios.get(`api/v1/voting-agendas/${id}`);
  return response.data;
}

export const openVotingSession = (data: { votingAgendaId: string; duration?: number }) =>
  axios.post('/api/v1/sessions', data);

export const getSessionByVotingAgendaId = async (votingAgendaId: string) =>{
  const response = await axios.get(`api/v1/sessions/voting-session/${votingAgendaId}`);
  return response.data;
}

export const castVote = (data: { votingAgendaId: string; cpf: string; vote: 'Sim' | 'Não' }) =>
  axios.post('/api/v1/votes', data);

export const getVotingResult = async (votingAgendaId: string) => {
  const response = await axios.get(`/api/v1/votes/result/${votingAgendaId}`);
  return response.data;
};
