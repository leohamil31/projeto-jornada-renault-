import { useEffect, useState } from 'react';
import { extrairErros, getApontamentos, getAuditoriaSeguranca, getVolumeProducao } from '../api.js';
import { Errors, Metric, fmtData } from '../ui.jsx';
import { VERIFICACOES_SEGURANCA } from './AuditoriaSegurancaForm.jsx';

export default function Historico({ onSelecionar }) {
  const [lista, setLista] = useState([]);
  const [erros, setErros] = useState([]);
  const [detalhe, setDetalhe] = useState(null);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');
  const [area, setArea] = useState('');

  const carregar = () => {
    setLoading(true);
    getApontamentos()
      .then((l) => { setLista([...l].reverse()); setErros([]); })
      .catch((e) => setErros(extrairErros(e)))
      .finally(() => setLoading(false));
  };

  useEffect(carregar, []);

  const ver = async (a) => {
    const [seguranca, volume] = await Promise.all([
      getAuditoriaSeguranca(a.idApontamento).catch(() => null),
      getVolumeProducao(a.idApontamento).catch(() => null),
    ]);
    setDetalhe({ a, seguranca, volume });
  };

  const areas = [...new Set(lista.map((a) => a.nomeArea))];
  const q = busca.trim().toLowerCase();
  const filtrada = lista.filter((a) =>
    (!area || a.nomeArea === area) &&
    (!q || [a.nomeUsuario, a.nomeTurno, a.nomeArea, a.observacoes, a.idApontamento].join(' ').toLowerCase().includes(q)));

  return (
    <div className="card">
      <h2>Histórico de apontamentos</h2>
      <p className="sub">Consulte as auditorias de segurança e os volumes de produção.</p>
      <Errors erros={erros} />
      <div className="grid" style={{ marginBottom: 14 }}>
        <div>
          <label>Buscar</label>
          <input type="text" placeholder="Responsável, turno, observação..." value={busca} onChange={(e) => setBusca(e.target.value)} />
        </div>
        <div>
          <label>Área</label>
          <select value={area} onChange={(e) => setArea(e.target.value)}>
            <option value="">Todas</option>
            {areas.map((a) => <option key={a} value={a}>{a}</option>)}
          </select>
        </div>
      </div>
      <div className="actions" style={{ marginBottom: 10 }}>
        <button type="button" className="btn" onClick={carregar} disabled={loading}>{loading ? 'Atualizando...' : 'Atualizar'}</button>
      </div>
      <table>
        <thead>
          <tr><th>#</th><th>Data</th><th>Área</th><th>Turno</th><th>Responsável</th><th>Status</th><th /></tr>
        </thead>
        <tbody>
          {filtrada.map((a) => (
            <tr key={a.idApontamento}>
              <td>{a.idApontamento}</td>
              <td>{fmtData(a.dataHora)}</td>
              <td>{a.nomeArea}</td>
              <td>{a.nomeTurno}</td>
              <td>{a.nomeUsuario}</td>
              <td><span className="badge">{a.status}</span></td>
              <td>
                <button className="link" onClick={() => ver(a)}>Detalhes</button>
                <button className="link" onClick={() => onSelecionar(a, 'seguranca')}>Segurança</button>
                <button className="link" onClick={() => onSelecionar(a, 'volume')}>Produção</button>
              </td>
            </tr>
          ))}
          {!filtrada.length && <tr><td colSpan="7">{loading ? 'Carregando...' : 'Nenhum apontamento encontrado.'}</td></tr>}
        </tbody>
      </table>
      {detalhe && (
        <div style={{ marginTop: 20 }}>
          <h2>Apontamento #{detalhe.a.idApontamento}</h2>
          <p className="sub">{detalhe.a.observacoes}</p>
          {detalhe.seguranca ? (
            <>
              <span className={`badge ${detalhe.seguranca.status === 'CONFORME' ? 'ok' : 'bad'}`}>
                Auditoria de segurança: {detalhe.seguranca.status}
              </span>
              <div className="metrics">
                {VERIFICACOES_SEGURANCA.map(({ key, label }) => (
                  <Metric key={key} label={label} value={detalhe.seguranca.verificacoes[key] ? 'Conforme' : 'Não conforme'} />
                ))}
              </div>
              {detalhe.seguranca.planoAcao && <p className="sub">Plano de ação: {detalhe.seguranca.planoAcao}</p>}
            </>
          ) : <p className="sub">Sem auditoria de segurança.</p>}
          {detalhe.volume ? (
            <div className="metrics">
              <Metric label="Volume produzido" value={`${detalhe.volume.quantidadeProduzida} peças`} />
            </div>
          ) : <p className="sub">Sem volume de produção registrado.</p>}
        </div>
      )}
    </div>
  );
}
