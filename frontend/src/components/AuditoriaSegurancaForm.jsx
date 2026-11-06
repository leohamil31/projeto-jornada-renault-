import { useState } from 'react';
import { criarAuditoriaSeguranca, extrairErros } from '../api.js';
import { Errors } from '../ui.jsx';

export const VERIFICACOES_SEGURANCA = [
  { key: 'epiConforme', label: 'EPIs adequados e em boas condições' },
  { key: 'protecoesMaquinasConformes', label: 'Proteções e dispositivos de segurança das máquinas em condições de uso' },
  { key: 'rotasEmergenciaDesobstruidas', label: 'Rotas de fuga e equipamentos de emergência desobstruídos' },
  { key: 'sinalizacaoAdequada', label: 'Sinalização de segurança visível e adequada' },
  { key: 'areaSemRiscosEvidentes', label: 'Área sem riscos evidentes, como piso escorregadio ou cabos expostos' },
];

const vazio = {
  verificacoes: Object.fromEntries(VERIFICACOES_SEGURANCA.map(({ key }) => [key, null])),
  planoAcao: '',
};

export default function AuditoriaSegurancaForm({ idApontamento }) {
  const [form, setForm] = useState(vazio);
  const [erros, setErros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);

  const setVerificacao = (key, value) => {
    setForm((current) => ({ ...current, verificacoes: { ...current.verificacoes, [key]: value } }));
  };

  const submit = async (e) => {
    e.preventDefault();
    setErros([]);
    setResultado(null);
    const naoRespondidas = VERIFICACOES_SEGURANCA.filter(({ key }) => form.verificacoes[key] === null);
    if (naoRespondidas.length) {
      return setErros(naoRespondidas.map(({ label }) => `Responda à verificação: ${label}.`));
    }

    const temNaoConformidade = Object.values(form.verificacoes).some((conforme) => !conforme);
    if (temNaoConformidade && !form.planoAcao.trim()) {
      return setErros(['Informe o plano de ação para os itens não conformes.']);
    }

    setLoading(true);
    try {
      setResultado(await criarAuditoriaSeguranca({
        idApontamento,
        verificacoes: form.verificacoes,
        planoAcao: form.planoAcao.trim() || null,
      }));
      setForm(vazio);
    } catch (err) {
      setErros(extrairErros(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Auditoria de segurança</h2>
      <p className="sub">Verifique os principais riscos da área. Esta checklist não substitui os procedimentos de segurança da unidade.</p>
      <Errors erros={erros} />
      {resultado && (
        <div className={`alert ${resultado.status === 'CONFORME' ? 'success' : 'error'}`} role="status">
          Auditoria #{resultado.idAuditoriaSeguranca} registrada: {resultado.status === 'CONFORME' ? 'todos os itens conformes.' : 'há itens que precisam de ação.'}
        </div>
      )}
      <div className="safety-checks">
        {VERIFICACOES_SEGURANCA.map(({ key, label }) => (
          <fieldset className="safety-check" key={key}>
            <legend>{label}</legend>
            <label className="safety-choice">
              <input
                type="radio"
                name={key}
                value="conforme"
                checked={form.verificacoes[key] === true}
                onChange={() => setVerificacao(key, true)}
              />
              Conforme
            </label>
            <label className="safety-choice">
              <input
                type="radio"
                name={key}
                value="nao-conforme"
                checked={form.verificacoes[key] === false}
                onChange={() => setVerificacao(key, false)}
              />
              Não conforme
            </label>
          </fieldset>
        ))}
        <div>
          <label htmlFor="plano-acao">Plano de ação</label>
          <textarea
            id="plano-acao"
            value={form.planoAcao}
            onChange={(e) => setForm((current) => ({ ...current, planoAcao: e.target.value }))}
            placeholder="Descreva como os itens não conformes serão tratados"
            required={Object.values(form.verificacoes).some((conforme) => conforme === false)}
          />
        </div>
      </div>
      <div className="actions">
        <button className="btn" disabled={loading}>{loading ? 'Salvando...' : 'Salvar auditoria de segurança'}</button>
      </div>
    </form>
  );
}
