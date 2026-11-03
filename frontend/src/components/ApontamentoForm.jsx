import { useEffect, useState } from 'react';
import { criarApontamento, extrairErros, getAreas, getTurnos, TIPOS_INDICADOR, USUARIOS } from '../api.js';
import { Errors } from '../ui.jsx';

const vazio = { idArea: '', idTurno: '', idUsuario: '', idTipoIndicador: '', observacoes: '' };

export default function ApontamentoForm({ onCriado }) {
  const [areas, setAreas] = useState([]);
  const [turnos, setTurnos] = useState([]);
  const [erros, setErros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState(vazio);

  useEffect(() => {
    Promise.all([getAreas(), getTurnos()])
      .then(([a, t]) => { setAreas(a); setTurnos(t); })
      .catch((e) => setErros(extrairErros(e)));
  }, []);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setErros([]);
    const faltando = [];
    if (!form.idArea) faltando.push('Selecione a área.');
    if (!form.idTurno) faltando.push('Selecione o turno.');
    if (!form.idUsuario) faltando.push('Selecione o responsável.');
    if (!form.idTipoIndicador) faltando.push('Selecione o tipo de indicador.');
    if (!form.observacoes.trim()) faltando.push('Informe as observações.');
    if (faltando.length) return setErros(faltando);

    setLoading(true);
    try {
      const criado = await criarApontamento({
        idArea: Number(form.idArea),
        idTurno: Number(form.idTurno),
        idUsuario: Number(form.idUsuario),
        idTipoIndicador: Number(form.idTipoIndicador),
        observacoes: form.observacoes.trim(),
      });
      setForm(vazio);
      onCriado(criado);
    } catch (err) {
      setErros(extrairErros(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Novo apontamento</h2>
      <p className="sub">Passo 1: registre o apontamento. Depois você poderá lançar a auditoria 5S e a apuração de OEE.</p>
      <Errors erros={erros} />
      <div className="grid">
        <div>
          <label>Área</label>
          <select value={form.idArea} onChange={set('idArea')}>
            <option value="">Selecione...</option>
            {areas.map((a) => <option key={a.idArea} value={a.idArea}>{a.nome}</option>)}
          </select>
        </div>
        <div>
          <label>Turno</label>
          <select value={form.idTurno} onChange={set('idTurno')}>
            <option value="">Selecione...</option>
            {turnos.map((t) => <option key={t.idTurno} value={t.idTurno}>{t.nome} ({t.horaInicio?.slice(0, 5)}–{t.horaFim?.slice(0, 5)})</option>)}
          </select>
        </div>
        <div>
          <label>Responsável / Operador</label>
          <select value={form.idUsuario} onChange={set('idUsuario')}>
            <option value="">Selecione...</option>
            {USUARIOS.map((u) => <option key={u.id} value={u.id}>{u.nome}</option>)}
          </select>
        </div>
        <div>
          <label>Tipo de indicador</label>
          <select value={form.idTipoIndicador} onChange={set('idTipoIndicador')}>
            <option value="">Selecione...</option>
            {TIPOS_INDICADOR.map((t) => <option key={t.id} value={t.id}>{t.nome}</option>)}
          </select>
        </div>
        <div className="full">
          <label>Observações</label>
          <textarea value={form.observacoes} onChange={set('observacoes')} placeholder="Descreva o contexto do apontamento" />
        </div>
      </div>
      <div className="actions">
        <button className="btn" disabled={loading}>{loading ? 'Salvando...' : 'Criar apontamento'}</button>
      </div>
    </form>
  );
}
