# Projeto Jornada Renault

## Publicação no GitHub Pages

O frontend é publicado automaticamente pelo GitHub Actions quando há um push na branch `main`.
O site funciona em modo demonstração, sem backend, e salva os dados no armazenamento local do navegador.

Antes do primeiro deploy, configure **Settings → Pages → Build and deployment → Source** como **GitHub Actions** em [Settings do repositório](https://github.com/leohamil31/projeto-jornada-renault-/settings/pages).
O endereço será:

`https://leohamil31.github.io/projeto-jornada-renault-/`

Para executar localmente:

```bash
cd frontend
npm ci
npm run dev
```