export function Errors({ erros }) {
  if (!erros || !erros.length) return null;
  return (
    <div className="alert error" role="alert">
      <ul>{erros.map((e, i) => <li key={i}>{e}</li>)}</ul>
    </div>
  );
}

export function Metric({ label, value }) {
  return (
    <div className="metric">
      <strong>{value}</strong>
      <span>{label}</span>
    </div>
  );
}

export const pct = (v) => (v == null ? '-' : `${Number(v).toFixed(1)}%`);
export const fmtData = (d) => (d ? new Date(d).toLocaleString('pt-BR') : '-');
