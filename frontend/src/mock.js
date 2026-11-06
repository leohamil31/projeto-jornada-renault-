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
const TIPOS = { 1: 'Segurança', 2: 'Produção', 3: 'Geral' };

const load = () => {
  const db = JSON.parse(localStorage.getItem(KEY) || '{"apontamentos":[]}');
  return {
    ...db,
    auditoriasSeguranca: db.auditoriasSeguranca || [],
    volumesProducao: db.volumesProducao || [],
  };
};
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

  criarAuditoriaSeguranca: async (p) => {
    const db = load();
    if (!find(db.apontamentos, p.idApontamento)) return erro(404, 'Apontamento não encontrado');
    const verificacoes = p.verificacoes || {};
    const chaves = [
      'epiConforme',
      'protecoesMaquinasConformes',
      'rotasEmergenciaDesobstruidas',
      'sinalizacaoAdequada',
      'areaSemRiscosEvidentes',
    ];
    if (chaves.some((chave) => typeof verificacoes[chave] !== 'boolean')) {
      return erro(400, 'Responda a todos os itens da auditoria de segurança.');
    }
    if (Object.values(verificacoes).some((conforme) => !conforme) && !p.planoAcao?.trim()) {
      return erro(400, 'Informe um plano de ação para os itens não conformes.');
    }
    const reg = {
      ...p,
      idAuditoriaSeguranca: db.auditoriasSeguranca.find((a) => a.idApontamento === p.idApontamento)?.idAuditoriaSeguranca
        || Math.max(0, ...db.auditoriasSeguranca.map((a) => a.idAuditoriaSeguranca)) + 1,
      status: Object.values(verificacoes).every(Boolean) ? 'CONFORME' : 'AÇÃO NECESSÁRIA',
    };
    const index = db.auditoriasSeguranca.findIndex((a) => a.idApontamento === p.idApontamento);
    if (index >= 0) db.auditoriasSeguranca[index] = reg;
    else db.auditoriasSeguranca.push(reg);
    save(db);
    return reg;
  },

  criarVolumeProducao: async (p) => {
    const db = load();
    if (!find(db.apontamentos, p.idApontamento)) return erro(404, 'Apontamento não encontrado');
    if (!Number.isInteger(p.quantidadeProduzida) || p.quantidadeProduzida < 0) {
      return erro(400, 'A quantidade produzida deve ser um número inteiro igual ou maior que zero.');
    }
    const reg = {
      ...p,
      idVolumeProducao: db.volumesProducao.find((v) => v.idApontamento === p.idApontamento)?.idVolumeProducao
        || Math.max(0, ...db.volumesProducao.map((v) => v.idVolumeProducao)) + 1,
      dataHora: new Date().toISOString(),
    };
    const index = db.volumesProducao.findIndex((v) => v.idApontamento === p.idApontamento);
    if (index >= 0) db.volumesProducao[index] = reg;
    else db.volumesProducao.push(reg);
    save(db);
    return reg;
  },

  getAuditoriaSeguranca: async (id) => find(load().auditoriasSeguranca, id) || erro(404, 'Auditoria de segurança não encontrada'),
  getVolumeProducao: async (id) => find(load().volumesProducao, id) || erro(404, 'Volume de produção não encontrado'),
};
