import { useState } from 'react';
import { criarVolumeProducao, extrairErros } from '../api.js';
import { Errors, Metric } from '../ui.jsx';

export default function VolumeProducaoForm({ idApontamento }) {
  const [quantidadeProduzida, setQuantidadeProduzida] = useState('');
  const [erros, setErros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);

  const submit = async (e) => {
    e.preventDefault();
    setErros([]);
    setResultado(null);
    if (quantidadeProduzida === '' || !Number.isInteger(Number(quantidadeProduzida)) || Number(quantidadeProduzida) < 0) {
      return setErros(['Informe um volume produzido em número inteiro igual ou maior que zero.']);
    }

    setLoading(true);
    try {
      setResultado(await criarVolumeProducao({ idApontamento, quantidadeProduzida: Number(quantidadeProduzida) }));
    } catch (err) {
      setErros(extrairErros(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Volume de produção</h2>
      <p className="sub">Informe a quantidade total de peças produzidas neste apontamento.</p>
      <Errors erros={erros} />
      {resultado && (
        <div className="alert success">
          Volume registrado.
          <div className="metrics">
            <Metric label="Peças produzidas" value={resultado.quantidadeProduzida} />
          </div>
        </div>
      )}
      <div>
        <label htmlFor="quantidade-produzida">Quantidade produzida (peças)</label>
        <input
          id="quantidade-produzida"
          type="number"
          min="0"
          step="1"
          value={quantidadeProduzida}
          onChange={(e) => setQuantidadeProduzida(e.target.value)}
          required
        />
      </div>
      <div className="actions">
        <button className="btn" disabled={loading}>{loading ? 'Salvando...' : 'Salvar volume de produção'}</button>
      </div>
    </form>
  );
}
