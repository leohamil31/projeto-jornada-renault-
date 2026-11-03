import { useState } from 'react';
import { criarOee, extrairErros } from '../api.js';
import { Errors, Metric, pct } from '../ui.jsx';

const CAMPOS = [
  ['tempoPlanejadoMinutos', 'Tempo planejado (min)', '1', 'Deve ser maior que 0.'],
  ['tempoParadaMinutos', 'Tempo de parada (min)', '0', 'Não pode ser negativo.'],
  ['producaoTotal', 'Produção total (peças)', '0', 'Não pode ser negativa.'],
  ['pecasRefugadas', 'Peças refugadas', '0', 'Não pode ser negativo.'],
  ['tempoCicloPadrao', 'Tempo de ciclo padrão (seg)', '0.01', 'Deve ser maior que 0.'],
];

const vazio = Object.fromEntries(CAMPOS.map(([k]) => [k, '']));

export default function OEEForm({ idApontamento }) {
  const [form, setForm] = useState(vazio);
  const [erros, setErros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setErros([]);
    setResultado(null);
    const msgs = CAMPOS.filter(([k, , min]) => form[k] === '' || Number(form[k]) < Number(min))
      .map(([, l, , m]) => `${l}: ${m}`);
    if (msgs.length) return setErros(msgs);

    setLoading(true);
    try {
      const payload = { idApontamento };
      CAMPOS.forEach(([k]) => { payload[k] = Number(form[k]); });
      setResultado(await criarOee(payload));
      setForm(vazio);
    } catch (err) {
      setErros(extrairErros(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Apuração de OEE</h2>
      <p className="sub">Informe tempos e quantidades do turno.</p>
      <Errors erros={erros} />
      {resultado && (
        <div className="alert success">
          Apuração #{resultado.idApuracaoOee} registrada.
          <div className="metrics">
            <Metric label="Disponibilidade" value={pct(resultado.indiceDisponibilidade)} />
            <Metric label="Performance" value={pct(resultado.indicePerformance)} />
            <Metric label="Qualidade" value={pct(resultado.indiceQualidade)} />
            <Metric label="OEE" value={pct(resultado.indiceOee)} />
          </div>
        </div>
      )}
      <div className="grid">
        {CAMPOS.map(([k, l, min]) => (
          <div key={k}>
            <label>{l}</label>
            <input type="number" min={min} step={k === 'tempoCicloPadrao' ? '0.01' : '1'} value={form[k]} onChange={set(k)} />
          </div>
        ))}
      </div>
      <div className="actions">
        <button className="btn" disabled={loading}>{loading ? 'Calculando...' : 'Salvar apuração OEE'}</button>
      </div>
    </form>
  );
}
