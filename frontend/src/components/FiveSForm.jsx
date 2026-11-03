import { useState } from 'react';
import { criarAuditoria5s, extrairErros } from '../api.js';
import { Errors, Metric } from '../ui.jsx';

const SENSOS = [
  ['notaSeiri', 'Seiri (Utilização)'],
  ['notaSeiton', 'Seiton (Organização)'],
  ['notaSeiso', 'Seiso (Limpeza)'],
  ['notaSeiketsu', 'Seiketsu (Padronização)'],
  ['notaShitsuke', 'Shitsuke (Disciplina)'],
];

const vazio = { notaSeiri: '', notaSeiton: '', notaSeiso: '', notaSeiketsu: '', notaShitsuke: '', planoAcao: '' };

export default function FiveSForm({ idApontamento }) {
  const [form, setForm] = useState(vazio);
  const [erros, setErros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });
  const preenchidas = SENSOS.filter(([k]) => form[k] !== '');
  const media = preenchidas.length === SENSOS.length
    ? SENSOS.reduce((s, [k]) => s + Number(form[k]), 0) / SENSOS.length
    : null;

  const submit = async (e) => {
    e.preventDefault();
    setErros([]);
    setResultado(null);
    const msgs = SENSOS.filter(([k]) => form[k] === '' || Number(form[k]) < 0 || Number(form[k]) > 5)
      .map(([, l]) => `${l}: informe uma nota de 0 a 5.`);
    if (msgs.length) return setErros(msgs);

    setLoading(true);
    try {
      const payload = { idApontamento, planoAcao: form.planoAcao.trim() || null };
      SENSOS.forEach(([k]) => { payload[k] = Number(form[k]); });
      setResultado(await criarAuditoria5s(payload));
      setForm(vazio);
    } catch (err) {
      setErros(extrairErros(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Auditoria 5S</h2>
      <p className="sub">Avalie cada senso com nota de 0 a 5.</p>
      <Errors erros={erros} />
      {resultado && (
        <div className="alert success">
          Auditoria #{resultado.idAuditoria} registrada.
          <div className="metrics">
            <Metric label="Nota total" value={resultado.notaTotal} />
          </div>
        </div>
      )}
      <div className="grid">
        {SENSOS.map(([k, l]) => (
          <div key={k}>
            <label>{l}</label>
            <input type="number" min="0" max="5" value={form[k]} onChange={set(k)} />
          </div>
        ))}
        {media !== null && (
          <div className="full">
            <span className={`badge ${media >= 4 ? 'ok' : media >= 3 ? 'warn' : 'bad'}`}>
              Média prévia: {media.toFixed(2)} / 5
            </span>
            {media < 3 && <span className="sub" style={{ marginLeft: 10 }}>Nota baixa: preencha o plano de ação.</span>}
          </div>
        )}
        <div className="full">
          <label>Plano de ação (opcional)</label>
          <textarea value={form.planoAcao} onChange={set('planoAcao')} />
        </div>
      </div>
      <div className="actions">
        <button className="btn" disabled={loading}>{loading ? 'Salvando...' : 'Salvar auditoria 5S'}</button>
      </div>
    </form>
  );
}
