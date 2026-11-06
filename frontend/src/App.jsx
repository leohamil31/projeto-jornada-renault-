import { useEffect, useState } from 'react';
import ApontamentoForm from './components/ApontamentoForm.jsx';
import AuditoriaSegurancaForm from './components/AuditoriaSegurancaForm.jsx';
import VolumeProducaoForm from './components/VolumeProducaoForm.jsx';
import Historico from './components/Historico.jsx';
import { USE_MOCK, getApontamentos } from './api.js';

const ABAS = [
  { id: 'apontamento', label: 'Apontamento', icon: 'M12 5v14M5 12h14' },
  { id: 'seguranca', label: 'Auditoria de segurança', icon: 'M12 3 4 7v5c0 5 3.4 8.5 8 10 4.6-1.5 8-5 8-10V7l-8-4Zm-3 9 2 2 4-4' },
  { id: 'volume', label: 'Volume de produção', icon: 'M3 3v18h18M7 15v-3m5 3V7m5 8V5' },
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

  const selecionar = (a, destino = 'seguranca') => { setAtual(a); setAba(destino); };
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
        {(aba === 'seguranca' || aba === 'volume') && (
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
        {aba === 'seguranca' && atual && <AuditoriaSegurancaForm key={atual.idApontamento} idApontamento={atual.idApontamento} />}
        {aba === 'volume' && atual && <VolumeProducaoForm key={atual.idApontamento} idApontamento={atual.idApontamento} />}
        {aba === 'historico' && <Historico onSelecionar={selecionar} />}
      </main>
    </>
  );
}
