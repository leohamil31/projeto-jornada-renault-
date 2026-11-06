import { useEffect, useState } from 'react';
import ApontamentoForm from './components/ApontamentoForm.jsx';
import FiveSForm from './components/FiveSForm.jsx';
import OEEForm from './components/OEEForm.jsx';
import Historico from './components/Historico.jsx';
import { USE_MOCK, getApontamentos } from './api.js';

const ABAS = [
  { id: 'apontamento', label: 'Apontamento', icon: 'M12 5v14M5 12h14' },
  { id: '5s', label: 'Auditoria 5S', precisa: true, icon: 'M9 11l3 3 8-8M20 12v7a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h9' },
  { id: 'oee', label: 'OEE', precisa: true, icon: 'M3 3v18h18M7 15l4-4 3 3 5-6' },
  { id: 'historico', label: 'Histórico', icon: 'M3 12a9 9 0 1 0 3-6.7L3 8M3 3v5h5M12 7v5l3 2' },
];

export default function App() {
  const [aba, setAba] = useState('apontamento');
  const [atual, setAtual] = useState(null);

  const [lista, setLista] = useState([]);

  useEffect(() => {
    if (aba === 'historico') return;
    getApontamentos().then((l) => {
      setLista(l);
      setAtual((cur) => cur || l[l.length - 1] || null);
    }).catch(() => setLista([]));
  }, [aba]);

  const selecionar = (a, destino = '5s') => { setAtual(a); setAba(destino); };
  const escolher = (e) => setAtual(lista.find((x) => String(x.idApontamento) === e.target.value) || null);

  return (
    <>
      <header className="header">
        <div className="header-top">
          <img className="logo-img" src={`${import.meta.env.BASE_URL}renault-logo.svg`} alt="Renault" />
          <div className="header-title">Lab Lean Digital · Renault do Brasil</div>
          {USE_MOCK && <span className="badge" style={{ marginLeft: 'auto' }}>Modo demonstração (sem banco)</span>}
        </div>
        <nav className="tabs">
          {ABAS.map((t) => (
            <button
              key={t.id}
              className={`tab ${aba === t.id ? 'active' : ''}`}
              onClick={() => setAba(t.id)}
            >
              <span className="icon-glass">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d={t.icon} />
                </svg>
              </span>
              {t.label}
            </button>
          ))}
        </nav>
      </header>

      <main className="container">
        {(aba === '5s' || aba === 'oee') && (
          <div className="card" style={{ marginBottom: 16 }}>
            <label>Apontamento</label>
            {lista.length ? (
              <select value={atual ? atual.idApontamento : ''} onChange={escolher}>
                {lista.map((a) => (
                  <option key={a.idApontamento} value={a.idApontamento}>
                    #{a.idApontamento} · {a.nomeArea} · {a.nomeTurno} · {a.nomeUsuario}
                  </option>
                ))}
              </select>
            ) : (
              <p className="sub">
                Nenhum apontamento ainda. <button className="link" onClick={() => setAba('apontamento')}>Criar apontamento</button>
              </p>
            )}
          </div>
        )}
        {aba === 'apontamento' && (
          <>
            <ApontamentoForm onCriado={(a) => selecionar(a)} />
          </>
        )}
        {aba === '5s' && atual && <FiveSForm key={atual.idApontamento} idApontamento={atual.idApontamento} />}
        {aba === 'oee' && atual && <OEEForm key={atual.idApontamento} idApontamento={atual.idApontamento} />}
        {aba === 'historico' && <Historico onSelecionar={selecionar} />}
      </main>
    </>
  );
}
