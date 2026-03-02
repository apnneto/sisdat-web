# Publicação de Container (GHCR) - SisDAT

Este documento descreve como o pipeline do GitHub Actions publica a imagem Docker do SisDAT no GitHub Container Registry (GHCR).

Pré-requisitos:
- Conta GitHub com permissões de `packages:write` para o repositório.
- No repositório, Secrets configurados (Settings -> Secrets -> Actions):
  - `GHCR_PAT` (opcional) — Personal Access Token com scope `packages:write` — não estritamente necessário se usar o login do Actions Runner.

O workflow principal: `.github/workflows/docker-publish.yml` fará:
1. Fazer checkout do código.
2. Detectar se existe `pom.xml` no repositório:
   - Se existir, rodar `mvn -DskipTests package` para gerar o WAR em `target/`.
   - Se não existir, o Dockerfile fará deploy da pasta `WebContent` como aplicação explodida.
3. Buildar a imagem usando `docker/build-push-action@v5` e publicar em `ghcr.io/<ORG_OR_USER>/sisdat-web` com tags `latest` e o SHA do commit.

Instruções manuais para publicar localmente:
- Build local (com WAR gerado):

```bash
mvn -DskipTests package
# depois
docker build --build-arg HAS_POM=true -t ghcr.io/<OWNER>/sisdat-web:latest .
docker push ghcr.io/<OWNER>/sisdat-web:latest
```

- Build local (sem WAR, deploy WebContent):

```bash
docker build --build-arg HAS_POM=false -t ghcr.io/<OWNER>/sisdat-web:latest .
docker push ghcr.io/<OWNER>/sisdat-web:latest
```

Notas e recomendações:
- Configure o repositório para permitir publishing em pacotes. Use o `ghcr.io` e não o Docker Hub se quiser integrar com GitHub Actions facilmente.
- Se a imagem contiver credenciais sensíveis (não recomendado), utilize Secrets e arguem variáveis apenas em runtime; evite bake-in no Dockerfile.
- Considere criar tags semânticas (v1.0.0) no workflow quando gerar releases.

