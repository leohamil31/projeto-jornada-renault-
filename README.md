# Projeto Jornada Renault

## Publicação no GitHub Pages

O frontend é publicado automaticamente pelo GitHub Actions quando há um push na branch `main`.
O site funciona em modo demonstração, sem backend, e salva os dados no armazenamento local do navegador.

Depois do primeiro push, confira em **Settings → Pages** se a origem está configurada como **GitHub Actions**.
O endereço será:

`https://leohamil31.github.io/projeto-jornada-renault-/`

Para executar localmente:

```bash
cd frontend
npm ci
npm run dev
```