import axios from 'axios';
import { mock } from './mock.js';

const api = axios.create({ baseURL: '/api' });

// A API não expõe endpoints de usuários/tipos de indicador; valores do seed do banco.
export const USUARIOS = [
  { id: 1, nome: 'Emilio Rodrigues (Analista Lean)' },
  { id: 2, nome: 'Operador Padrão' },
  { id: 3, nome: 'Leonardo' },
  { id: 4, nome: 'Gabriel' },
  { id: 5, nome: 'Marcel' },
  { id: 6, nome: 'João' },
];

export const TIPOS_INDICADOR = [
  { id: 1, nome: 'Segurança' },
  { id: 2, nome: 'Produção' },
  { id: 3, nome: 'Geral' },
];

// Extrai mensagens de erro de 400 (mensagens[]/message) e 404 (message).
export function extrairErros(error) {
  if (!error.response) {
    return ['Não foi possível conectar à API (http://localhost:8080). Verifique se o backend está rodando.'];
  }
  const data = error.response.data;
  if (data && Array.isArray(data.mensagens) && data.mensagens.length) return data.mensagens;
  if (data && data.message) return [data.message];
  if (typeof data === 'string' && data) return [data];
  return [`Erro ${error.response.status} ao processar a requisição.`];
}

// Modo demonstração (sem backend/banco): VITE_USE_MOCK=true em .env.development
export const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true';

const real = {
  getAreas: () => api.get('/areas').then((r) => r.data),
  getTurnos: () => api.get('/turnos').then((r) => r.data),
  getApontamentos: () => api.get('/apontamentos').then((r) => r.data),
  criarApontamento: (p) => api.post('/apontamentos', p).then((r) => r.data),
  criarAuditoriaSeguranca: (p) => api.post('/seguranca', {
    idApontamento: p.idApontamento,
    ...p.verificacoes,
    planoAcao: p.planoAcao,
  }).then((r) => r.data),
  criarVolumeProducao: (p) => api.post('/volume-producao', p).then((r) => r.data),
  getAuditoriaSeguranca: (id) => api.get(`/seguranca/apontamento/${id}`).then((r) => r.data),
  getVolumeProducao: (id) => api.get(`/volume-producao/apontamento/${id}`).then((r) => r.data),
};

const impl = USE_MOCK ? mock : real;

export const getAreas = impl.getAreas;
export const getTurnos = impl.getTurnos;
export const getApontamentos = impl.getApontamentos;
export const criarApontamento = impl.criarApontamento;
export const criarAuditoriaSeguranca = impl.criarAuditoriaSeguranca;
export const criarVolumeProducao = impl.criarVolumeProducao;
export const getAuditoriaSeguranca = impl.getAuditoriaSeguranca;
export const getVolumeProducao = impl.getVolumeProducao;

export default api;
