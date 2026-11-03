import { useEffect, useState } from 'react';
import { extrairErros, getApontamentos, getAuditoria5s, getOee } from '../api.js';
import { Errors, Metric, fmtData, pct } from '../ui.jsx';

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
    const [s5, oee] = await Promise.all([
      getAuditoria5s(a.idApontamento).catch(() => null),
      getOee(a.idApontamento).catch(() => null),
    ]);
    setDetalhe({ a, s5, oee });
  };

  const areas = [...new Set(lista.map((a) => a.nomeArea))];
  const q = busca.trim().toLowerCase();
  const filtrada = lista.filter((a) =>
    (!area || a.nomeArea === area) &&
    (!q || [a.nomeUsuario, a.nomeTurno, a.nomeArea, a.observacoes, a.idApontamento].join(' ').toLowerCase().includes(q)));

  const classeOee = (v) => (v >= 85 ? 'ok' : v >= 60 ? 'warn' : 'bad');

  return (
    <div className="card">
      <h2>Histórico de apontamentos</h2>
      <p className="sub">Consulte os registros e seus resultados de 5S e OEE.</p>
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
                <button className="link" onClick={() => onSelecionar(a, '5s')}>5S</button>
                <button className="link" onClick={() => onSelecionar(a, 'oee')}>OEE</button>
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
          {detalhe.s5 ? (
            <>
              <div className="metrics">
                <Metric label="Nota 5S total" value={detalhe.s5.notaTotal} />
                <Metric label="Seiri" value={detalhe.s5.notaSeiri} />
                <Metric label="Seiton" value={detalhe.s5.notaSeiton} />
                <Metric label="Seiso" value={detalhe.s5.notaSeiso} />
                <Metric label="Seiketsu" value={detalhe.s5.notaSeiketsu} />
                <Metric label="Shitsuke" value={detalhe.s5.notaShitsuke} />
              </div>
              {detalhe.s5.planoAcao && <p className="sub">Plano de ação: {detalhe.s5.planoAcao}</p>}
            </>
          ) : <p className="sub">Sem auditoria 5S.</p>}
          {detalhe.oee ? (
            <>
              <div className="metrics">
                <Metric label="Disponibilidade" value={pct(detalhe.oee.indiceDisponibilidade)} />
                <Metric label="Performance" value={pct(detalhe.oee.indicePerformance)} />
                <Metric label="Qualidade" value={pct(detalhe.oee.indiceQualidade)} />
                <Metric label="OEE" value={pct(detalhe.oee.indiceOee)} />
              </div>
              <span className={`badge ${classeOee(detalhe.oee.indiceOee)}`}>
                {detalhe.oee.indiceOee >= 85 ? 'Classe mundial (≥85%)' : detalhe.oee.indiceOee >= 60 ? 'Aceitável (60–85%)' : 'Abaixo do esperado (<60%)'}
              </span>
            </>
          ) : <p className="sub">Sem apuração de OEE.</p>}
        </div>
      )}
    </div>
  );
}
