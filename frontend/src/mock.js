const KEY = 'labLeanMock';

const AREAS = [
  { idArea: 1, codigo: 'SLD01', nome: 'Solda Ponto' },
  { idArea: 2, codigo: 'PNT01', nome: 'Pintura' },
  { idArea: 3, codigo: 'MNT01', nome: 'Montagem Final' },
];
const TURNOS = [
  { idTurno: 1, codigo: 'T1', nome: 'Turno 1', horaInicio: '06:00:00', horaFim: '14:00:00' },
  { idTurno: 2, codigo: 'T2', nome: 'Turno 2', horaInicio: '14:00:00', horaFim: '22:00:00' },
  { idTurno: 3, codigo: 'T3', nome: 'Turno 3', horaInicio: '22:00:00', horaFim: '06:00:00' },
];
const USUARIOS = { 1: 'Emilio Rodrigues', 2: 'Operador Padrão', 3: 'Leonardo', 4: 'Gabriel', 5: 'Marcel', 6: 'João' };
const TIPOS = { 1: '5S', 2: 'OEE', 3: 'GERAL' };

const load = () => JSON.parse(localStorage.getItem(KEY) || '{"apontamentos":[],"s5":[],"oee":[]}');
const save = (db) => localStorage.setItem(KEY, JSON.stringify(db));

const erro = (status, message) => Promise.reject({ response: { status, data: { message } } });
const find = (arr, id) => arr.find((x) => x.idApontamento === Number(id));

export const mock = {
  getAreas: async () => AREAS,
  getTurnos: async () => TURNOS,
  getApontamentos: async () => load().apontamentos.slice().reverse(),

  criarApontamento: async (p) => {
    if (!p.observacoes) return erro(400, 'observacoes: não pode estar em branco');
    const db = load();
    const novo = {
      idApontamento: db.apontamentos.length + 1,
      nomeArea: AREAS.find((a) => a.idArea === p.idArea)?.nome,
      nomeTurno: TURNOS.find((t) => t.idTurno === p.idTurno)?.nome,
      nomeUsuario: USUARIOS[p.idUsuario],
      tipoIndicadorDescricao: TIPOS[p.idTipoIndicador],
      dataHora: new Date().toISOString(),
      observacoes: p.observacoes,
      status: 'ABERTO',
    };
    db.apontamentos.push(novo);
    save(db);
    return novo;
  },

  criarAuditoria5s: async (p) => {
    const db = load();
    if (!find(db.apontamentos, p.idApontamento)) return erro(404, 'Apontamento não encontrado');
    const notas = [p.notaSeiri, p.notaSeiton, p.notaSeiso, p.notaSeiketsu, p.notaShitsuke];
    const reg = { ...p, idAuditoria: db.s5.length + 1, notaTotal: notas.reduce((a, b) => a + b, 0) / notas.length };
    db.s5.push(reg);
    save(db);
    return reg;
  },

  criarOee: async (p) => {
    const db = load();
    if (!find(db.apontamentos, p.idApontamento)) return erro(404, 'Apontamento não encontrado');
    const produtivo = p.tempoPlanejadoMinutos - p.tempoParadaMinutos;
    const disp = p.tempoPlanejadoMinutos > 0 ? produtivo / p.tempoPlanejadoMinutos : 0;
    const perf = produtivo > 0 ? (p.tempoCicloPadrao * p.producaoTotal) / (produtivo * 60) : 0;
    const qual = p.producaoTotal > 0 ? (p.producaoTotal - p.pecasRefugadas) / p.producaoTotal : 0;
    const reg = {
      ...p,
      idApuracaoOee: db.oee.length + 1,
      indiceDisponibilidade: disp * 100,
      indicePerformance: perf * 100,
      indiceQualidade: qual * 100,
      indiceOee: disp * perf * qual * 100,
    };
    db.oee.push(reg);
    save(db);
    return reg;
  },

  getAuditoria5s: async (id) => find(load().s5, id) || erro(404, 'Auditoria 5S não encontrada'),
  getOee: async (id) => find(load().oee, id) || erro(404, 'Apuração OEE não encontrada'),
};
