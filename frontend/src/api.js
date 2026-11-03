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
  { id: 1, nome: '5S' },
  { id: 2, nome: 'OEE' },
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
  criarAuditoria5s: (p) => api.post('/5s', p).then((r) => r.data),
  criarOee: (p) => api.post('/oee', p).then((r) => r.data),
  getAuditoria5s: (id) => api.get(`/5s/apontamento/${id}`).then((r) => r.data),
  getOee: (id) => api.get(`/oee/apontamento/${id}`).then((r) => r.data),
};

const impl = USE_MOCK ? mock : real;

export const getAreas = impl.getAreas;
export const getTurnos = impl.getTurnos;
export const getApontamentos = impl.getApontamentos;
export const criarApontamento = impl.criarApontamento;
export const criarAuditoria5s = impl.criarAuditoria5s;
export const criarOee = impl.criarOee;
export const getAuditoria5s = impl.getAuditoria5s;
export const getOee = impl.getOee;

export default api;
