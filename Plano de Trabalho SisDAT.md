# Plano de Trabalho: Recuperação e Modernização - Projeto Sisdat

Este documento descreve as etapas de recuperação do ambiente legado e o cronograma para atualização das ferramentas de desenvolvimento para padrões modernos.

## 1. Visão Geral
Nome: Sisdat (Módulo Web)
Natureza: Aplicação Enterprise (Java EE/Jakarta EE)
Arquitetura Legada: Servidor GlassFish 3.1.2.2, Java 7, MySQL (dump), dependências empacotadas em `WEB-INF/lib`.

Objetivo deste plano: atualizar o projeto legado para um stack suportado, seguro e replicável em 2026, reduzindo dívida técnica e preparando a aplicação para manutenção e evolução.

---

## 2. Status Atual (Resumo das ações já realizadas)
- [x] Recuperação de fontes: arquivos baixados do servidor AWS.
- [x] Instalação do JDK: configuração local (Java 8 usada inicialmente para desenvolvimento local).
- [x] Configuração da IDE: Eclipse (versão antiga usada para restauração).
- [x] Resolução de dependências locais: bibliotecas JAR em `WEB-INF/lib` mapeadas para o build path.
- [x] Ajuste de compilação: ajustes de compliance do compilador para Java 1.8.
- [x] **Ambiente Docker local operacional**: aplicação rodando via Docker Compose (Payara 5.2022.4 + MySQL 8.0) com deploy automático do WAR e datasource JDBC configurado — concluído em 2026-02-27.

---

## 3. Objetivos e Critérios de Aceitação
Objetivos principais:
- Modernizar toolchain: migração para Maven (ou Gradle), adoção de Java LTS (17, com planejamento para 21), e uso de Payara ou WildFly como runtime suportado.
- Reprodutibilidade: executar a aplicação localmente via Docker Compose (app server + MySQL) e em CI.
- Segurança: atualizar dependências para versões conhecidas e sem CVEs críticos; configurar scans automáticos.
- Qualidade: introduzir análise estática (SpotBugs/PMD/Checkstyle) e testes automatizados básicos (unit/integration).

Critérios de aceitação (mínimos):
- Projeto builda com Maven (`mvn -DskipTests package`) sem erros de compilação em Java 17.
- Aplicação inicia em Payara 6 (ou WildFly 30+) com datasource apontando para MySQL 8 (em container) e endpoints principais (ex.: login, listagens CRUD) funcionam em smoke tests.
- Dependências com CVEs críticos resolvidas ou com plano de mitigação documentado.

---

## 4. Plano de Recuperação (Curto Prazo — objetivo: rodar localmente como está hoje)
Passos imediatos:
1. ~~Consolidar inventário de JARs: listar todo o conteúdo de `WEB-INF/lib` e salvar um inventário (nome + versão quando possível).~~ ✅ Concluído
2. ~~Reinstalar/validar JDK local (Java 8 ou 11) apenas para reproduzir o run-on-server original, se desejado.~~ ✅ Concluído
3. Configurar MySQL local (ou Docker) e restaurar dump existente `script.sql` em `src/META-INF`. ✅ MySQL rodando via Docker; **dump `script.sql` ainda pendente** (ver observação abaixo).
4. ~~Ajustar `persistence.xml`/`web.xml` com credenciais locais (ou criar um DataSource JNDI para o runtime local).~~ ✅ DataSource JNDI `jdbc/sisdat-ds` configurado via asadmin pós-boot.
5. ~~Realizar o primeiro "Run on Server" em GlassFish/Payara local para validar que a aplicação está funcional antes de migrações maiores.~~ ✅ Aplicação rodando em Payara 5.2022.4 via Docker — HTTP 200 confirmado.

> **Observação sobre o `script.sql`:** o arquivo em `src/META-INF/script.sql` é um script de *migração incremental* (ALTER TABLE, UPDATE, INSERT) sobre um banco já existente, não um dump DDL completo. Ele não pode ser montado diretamente como init script do MySQL 8 (causa erro de parse). O banco atual sobe vazio (schema `sisdat` criado, tabelas ausentes). Para ter dados funcionais é necessário obter ou recriar o dump DDL completo — ver item "Próximos Passos".

---

## 5. Plano de Modernização (Médio/Longo Prazo — objetivo: migrar para toolchain moderno)
Fase A — Preparação e Inventário ✅ *Concluído*
- Inventariar dependências e checar compatibilidade com Java 17/Jakarta EE.
- Identificar bibliotecas sem versões modernas ou sem manutenção; propor substituições.
- Definir meta: Java 17 para migração inicial; planejar Java 21 em etapa posterior.

Fase B — Gerenciamento de Dependências (Maven) 🔄 *Parcialmente concluído*
- ✅ Criar `pom.xml` minimal para encapsular o build (packaging war).
- ⏳ Mover dependências que existem em `WEB-INF/lib` para dependências do Maven sempre que possível.
- ⏳ Para JARs sem artefato público, adicionar a um repositório interno ou usar `system` scope temporariamente.

Fase C — Atualização do Servidor de Aplicação 🔄 *Parcialmente concluído*
- ✅ Runtime Payara 5.2022.4 rodando localmente via Docker.
- ⏳ Migrar para Payara 6 (ou WildFly 30+) que suporte Java 17/21.
- ✅ Configurações JNDI/datasource e scripts de deploy funcionando.

Fase D — Código e Namespace ⏳ *Pendente*
- Verificar a necessidade de migrar de `javax.*` para `jakarta.*`.
- Corrigir APIs obsoletas e chamadas a classes removidas/alteradas.

Fase E — Containerização e Automação ✅ *Concluído (base)*
- ✅ `Dockerfile.payara` e `docker-compose.yml` funcionais.
- ✅ Scripts de build do WAR (`scripts/build-war.ps1`) e configuração pós-boot do Payara (`scripts/payara-post-boot.asadmin`).
- ⏳ Scripts para inicializar DB a partir do dump DDL completo quando disponível.

Fase F — CI/CD e Scans ⏳ *Pendente*
- Pipeline (GitHub Actions) para: Checkout, build (Maven), testes, container build, security scanning.
- Habilitar Dependabot para manter dependências atualizadas automaticamente.

Fase G — Qualidade e Testes ⏳ *Pendente*
- Adicionar testes unitários e de integração.
- Configurar SpotBugs, PMD e Checkstyle.

Fase H — Release e Documentação ⏳ *Pendente*
- Documentar instruções de build, execução local via Docker, e rollout para staging/prod.
- Planejar janela de deploy para atualização do Prod/Stage com rollback documentado.

---

## 6. Tabela de Versões (De → Para)
| Componente    | Legado                  | Atual (2026-02-27)         | Meta                        |
|---------------|-------------------------|----------------------------|-----------------------------|
| Java          | 7/8                     | 8 (build local)            | 17 LTS → 21                 |
| Servidor      | GlassFish 3.1.2.2       | Payara 5.2022.4 (Docker)   | Payara 6 / WildFly 30+      |
| Dependências  | JARs manuais em WEB-INF | JARs no WAR + Jersey via PS| Maven Central / repo interno |
| Banco         | MySQL (legado, dump)    | MySQL 8.0 (Docker)         | MySQL 8.0 (containers)      |
| Build         | Ant/Eclipse-managed     | `build-war.ps1` + jar      | Maven 3.9+                  |
| Containerização | Não existia           | Docker Compose funcional   | Docker Compose + CI         |

---

## 7. Riscos e Mitigações
- **Código dependente de APIs internas do GlassFish:** manter um ambiente de testes com GlassFish enquanto adapta código para Payara.
- **Bibliotecas sem artefato público (JARs customizados):** documentar e armazenar em repositório interno ou como assets versionados.
- **Migração javax → jakarta:** pode exigir mudanças amplas. Mitigação: migrar incrementalmente; usar versões do runtime que ainda suportem `javax` se necessário (Payara 5 ainda usa `javax`).
- **Banco sem DDL completo:** o `script.sql` é incremental — sem o dump DDL original as tabelas não existem. Risco alto para testes funcionais completos.
- **Driver MySQL desatualizado:** ~~o projeto usa `mysql-connector-java-5.1.16` (2011), incompatível com autenticação padrão do MySQL 8 (`caching_sha2_password`). Contornado via `--default-authentication-plugin=mysql_native_password` no container; solução definitiva é atualizar o driver para 8.x.~~ ✅ **Resolvido em 2026-03-04:** driver atualizado para `mysql-connector-j-8.0.33`; workaround `mysql_native_password` removido do `docker-compose.yml`.
- **Tempo e esforço de QA:** estimar sprints dedicados para testes de regressão; priorizar telas/fluxos críticos.

---

## 8. Plano de Trabalho Detalhado (tarefas e estimativas)
Nota: estimativas em dias úteis para uma equipe pequena (1–2 desenvolvedores). Ajustar conforme disponibilidade.

| Tarefa | Estimativa | Status |
|--------|-----------|--------|
| Inventário de dependências e análise de risco | 2–3 dias | ✅ Concluído |
| Ambiente Docker local (Payara + MySQL + deploy) | 2–4 dias | ✅ Concluído |
| Configurar Maven base e build reproduzível | 3–5 dias | 🔄 Base criada, compilação pendente |
| Obter/recriar DDL completo do banco e validar app | 1–2 dias | ⏳ Pendente |
| Atualizar driver MySQL (5.1 → 8.x) | 1 dia | ✅ Concluído em 2026-03-04 |
| Migrar dependências para Maven e resolver conflitos | 4–8 dias | ⏳ Pendente |
| Migrar runtime para Payara 6 / Java 17 | 5–15 dias | ⏳ Pendente |
| Migração javax → jakarta (se necessário) | 5–15 dias | ⏳ Pendente |
| Adicionar testes e análise estática | 5–10 dias | ⏳ Pendente |
| CI/CD e scans de segurança | 2–4 dias | ⏳ Pendente |
| Validação funcional e correções | 5–10 dias | ⏳ Pendente |

---

## 9. Registro das Ações Realizadas

### Ações realizadas até 2026-02-20
- Inventário das dependências extraídas de `WebContent/WEB-INF/lib` gerado e salvo em:
  - `docs/dependency-inventory.md`
  - `docs/dependency-inventory.json`
- Arquivos criados para iniciar migração e automação:
  - `pom.xml` (POM base, packaging `war`; target Java 1.8 e perfil `java17` disponível)
  - `Dockerfile.payara`
  - `docker-compose.yml`
  - `.github/workflows/maven.yml`

### Ações realizadas em 2026-02-27 — Ambiente Docker local operacional

#### Problemas encontrados e resolvidos

| # | Problema | Causa raiz | Solução aplicada |
|---|----------|-----------|-----------------|
| 1 | `mvn package` não compilava | Maven não instalado / dependências ausentes no `pom.xml` | Usado `build/classes` (compilado pelo Eclipse) como fonte de classes |
| 2 | WAR incompleto (sem páginas web) | Staging montado incorretamente no Windows | Script PowerShell `scripts/build-war.ps1` criado para montar o WAR corretamente |
| 3 | `Permission denied` nos JARs Jersey | Docker `ADD` copia arquivos como `root:root 600`; Payara roda como usuário `payara` | JARs Jersey 1.x e `jsr311-api` incluídos dentro do WAR em `WEB-INF/lib` |
| 4 | `jdbc/sisdat-ds` não encontrado | DataSource não configurado no Payara | `scripts/payara-post-boot.asadmin` criado e injetado no Dockerfile para criar pool + resource antes do deploy |
| 5 | Driver MySQL incompatível com MySQL 8 | `mysql-connector-java-5.1.16` não suporta `caching_sha2_password` | `--default-authentication-plugin=mysql_native_password` adicionado ao serviço `db` no `docker-compose.yml` |
| 6 | Payara iniciava antes do MySQL estar pronto | Race condition no startup dos containers | `healthcheck` adicionado ao serviço `db`; `depends_on: condition: service_healthy` no serviço `payara` |
| 7 | `script.sql` montado como diretório | Caminho `META-INF/script.sql` era um diretório no Windows (criado erroneamente por Docker) | Diretório removido; mount do init SQL removido do `docker-compose.yml` (script é incremental, não DDL) |

#### Artefatos novos criados
| Arquivo | Descrição |
|---------|-----------|
| `scripts/build-war.ps1` | Script PowerShell que monta `war-staging/` (WebContent + build/classes + Jersey jars) e empacota `target/sisdat-web.war` |
| `scripts/payara-post-boot.asadmin` | Comandos asadmin executados no boot do Payara: cria pool `SisdatPool` e resource `jdbc/sisdat-ds` apontando para MySQL |

#### Arquivos modificados
| Arquivo | Modificação |
|---------|------------|
| `Dockerfile.payara` | Simplificado: apenas `COPY scripts/payara-post-boot.asadmin` + `COPY target/sisdat-web.war` |
| `docker-compose.yml` | Adicionado `command: --default-authentication-plugin=mysql_native_password`; adicionado `healthcheck` no `db`; `depends_on: condition: service_healthy` no `payara`; removido mount do `script.sql` quebrado |

#### Estado final confirmado (2026-02-27)
- `sisdat-web-db-1`: **Up (healthy)** — MySQL 8.0 na porta 3307
- `sisdat-web-payara-1`: **Up** — Payara 5.2022.4 na porta 8080
- Deploy: `sisdat-web was successfully deployed in 7,974 milliseconds` ✅
- HTTP: `GET http://localhost:8080/sisdat-web/` → **HTTP 200** ✅
- HTTP: `GET http://localhost:8080/sisdat-web/index.jsp` → **HTTP 200** ✅

---

### Ações realizadas em 2026-03-05 — Correção do HTTP 404 pós-Prioridade 2

#### Causa raiz
Após a atualização do driver MySQL (Prioridade 2), o WAR passou a falhar no deploy com `ClassNotFoundException: Glassfish` dentro do predeploy JPA do EclipseLink. O motivo: os JARs legados `eclipselink.jar` / `eclipselink-2.0.2.jar` / `eclipselink-javax.persistence-2.0.jar` ainda estavam em `WEB-INF/lib`. Com `delegate="false"` no `sun-web.xml`, o EclipseLink 2.0.2 bundled assumia precedência sobre o EclipseLink 2.7.9 do Payara; ao inicializar, a versão 2.0.2 tentava carregar `com.sun.enterprise.server.Glassfish` (target-server interna do GlassFish 3.x) que não existe no Payara 5.

#### Problemas encontrados e resolvidos

| # | Problema | Causa raiz | Solução aplicada |
|---|----------|-----------|-----------------|
| 8 | `ClassNotFoundException: Glassfish` no predeploy JPA | EclipseLink 2.0.2 bundled no WAR conflitava com EclipseLink 2.7.9 do Payara 5 | Removidos `eclipselink.jar`, `eclipselink-2.0.2.jar` e `eclipselink-javax.persistence-2.0.jar` de `WebContent/WEB-INF/lib` |
| 9 | Classloader do WAR tinha precedência sobre o container | `sun-web.xml` com `delegate="false"` | Alterado para `delegate="true"` em `WebContent/WEB-INF/sun-web.xml` |
| 10 | Pool JDBC falhava silenciosamente na criação | `com.mysql.jdbc.jdbc2.optional.MysqlDataSource` removida no Connector/J 8.x | Atualizado para `com.mysql.cj.jdbc.MysqlDataSource` em `scripts/payara-post-boot.asadmin` |

#### Arquivos modificados / criados
| Arquivo | Modificação |
|---------|------------|
| `WebContent/WEB-INF/lib/` | Removidos `eclipselink.jar`, `eclipselink-2.0.2.jar`, `eclipselink-javax.persistence-2.0.jar` |
| `WebContent/WEB-INF/sun-web.xml` | `<class-loader delegate="false"/>` → `<class-loader delegate="true"/>` |
| `scripts/payara-post-boot.asadmin` | `com.mysql.jdbc.jdbc2.optional.MysqlDataSource` → `com.mysql.cj.jdbc.MysqlDataSource` |
| `scripts/build-war.ps1` | Adicionada etapa 2b para remover os 3 JARs EclipseLink do staging antes de empacotar |

#### Estado final confirmado (2026-03-05)
- `sisdat-web-db-1`: **Up (healthy)** — MySQL 8.0 na porta 3307
- `sisdat-web-payara-1`: **Up** — Payara 5.2022.4 na porta 8080
- EclipseLink em uso: `Eclipse Persistence Services - 2.7.9.payara-p2` (do Payara, não bundled) ✅
- Deploy: `sisdat-web was successfully deployed in 7,478 milliseconds` ✅
- HTTP: `GET http://localhost:8080/sisdat-web/` → **HTTP 200** ✅

---

## 10. Checklist de Entrega (Status atual — 2026-02-27)

### Recuperação (Curto Prazo)
- [x] Inventário de dependências (MD + JSON) — concluído
- [x] `pom.xml` base criado — concluído
- [x] `docker-compose.yml` + `Dockerfile.payara` criado e **funcional** — concluído
- [x] CI pipeline (workflow) criado — concluído
- [x] DataSource JNDI `jdbc/sisdat-ds` configurado — concluído
- [x] Aplicação acessível via `http://localhost:8080/sisdat-web/` — ✅ confirmado novamente em 2026-03-05 após correção do 404 (EclipseLink bundled + delegate)
- [ ] Dump DDL completo do banco restaurado (tabelas presentes) — **pendente**
- [ ] Login funcional end-to-end com dados no banco — **pendente**

### Modernização (Médio/Longo Prazo)
- [ ] Compilar com Maven (`mvn -DskipTests package`) sem erros — pendente
- [ ] Mover dependências para Maven e resolver conflitos — pendente
- [x] Atualizar `mysql-connector-java` para versão 8.x — ✅ Concluído em 2026-03-04 (`mysql-connector-j-8.0.33`)
- [ ] Publicar JARs customizados em repositório interno — pendente
- [ ] Migração para Java 17 / Payara 6 e validação de smoke tests — pendente
- [ ] Migração `javax.*` → `jakarta.*` (se necessário) — pendente
- [ ] Testes unitários e de integração — pendente
- [ ] CI com scan de vulnerabilidades (OWASP Dependency-Check) — pendente

---

## 11. Próximos Passos Imediatos (recomendados — 2026-02-27)

### Prioridade 1 — Banco de dados (desbloqueador para testes funcionais)
O banco sobe vazio (sem tabelas). Para ter a aplicação totalmente funcional:
1. Obter o dump DDL completo do banco de produção/staging (via `mysqldump --no-data` ou dump completo).
2. Salvar como `src/main/sql/sisdat-schema.sql` (arquivo UTF-8 sem comentários com `\\`).
3. Montar o arquivo no `docker-compose.yml`:
```yaml
- ./src/main/sql/sisdat-schema.sql:/docker-entrypoint-initdb.d/01-schema.sql:ro
```
4. Rodar `docker-compose down -v && docker-compose up -d` para reinicializar o banco com o schema.

### Prioridade 2 — Atualizar driver MySQL
Substituir `mysql-connector-java-5.1.16-bin.jar` em `WebContent/WEB-INF/lib/` pelo conector 8.x:
```powershell
# Baixar mysql-connector-java-8.0.33.jar do Maven Central e substituir no WEB-INF/lib
# Depois rodar scripts/build-war.ps1 e docker-compose up --build -d
```

### Prioridade 3 — Rebuild completo para próximos ciclos
Sempre que alterar código ou dependências:
```powershell
# 1. Recompilar classes no Eclipse (ou via javac manual)
# 2. Reconstruir e redeployar:
powershell -File scripts\build-war.ps1
docker-compose up --build -d
```

### Prioridade 4 — Migrar build para Maven
```bash
# Pré-requisitos: JDK 8 ou 17 instalado, Maven 3.9+
mvn -B -DskipTests package
docker-compose up --build -d
```

---

## 12. Registro de Decisões e Justificativas
- **Java 1.8 como target padrão:** mantido para compatibilidade com o runtime atual; perfil `java17` no POM para avançar progressivamente.
- **Não convertemos JARs para Maven automaticamente:** a conversão automática pode introduzir conflitos; abordagem incremental com smoke tests é a recomendada.
- **Payara 5.2022.4 em vez de Payara 6:** escolhido por usar `javax.*` (compatível com o código existente sem refactoring de namespace); migração para Payara 6/Jakarta EE será etapa separada.
- **Jersey 1.x bundled no WAR:** os JARs de Jersey e `jsr311-api` foram movidos para `WEB-INF/lib` dentro do WAR (em vez de `domain1/lib`) para contornar a limitação de permissões do Docker com o usuário `payara`.
- **`--default-authentication-plugin=mysql_native_password`:** workaround necessário enquanto o driver MySQL 5.1 legado for usado. Remover quando o driver for atualizado para 8.x.
- **Init SQL removido do docker-compose:** `script.sql` é um script incremental, não um DDL completo — montá-lo causava falha no MySQL 8. Quando o DDL completo estiver disponível, readicioná-lo.

---

## 13. Contato e Próximos Responsáveis
- Recomendado: atribuir 1 desenvolvedor técnico responsável pela conversão inicial de dependências e 1 pelo setup de CI/containers.
- Documentar PRs com checklist: "incluir dependência X no POM, validar build local, rodar smoke tests no container".

---

## 14. Histórico de Versões do Documento
- **2026-02-20:** Versão inicial com registro das ações realizadas até a data (inventário, POM base, Docker, CI workflow).
- **2026-02-27:** Atualização completa — ambiente Docker local totalmente operacional; registro detalhado de todos os problemas encontrados e soluções aplicadas; checklists e próximos passos revisados.
- **2026-03-04:** Driver MySQL atualizado de `mysql-connector-java-5.1.16` para `mysql-connector-j-8.0.33`; workaround `--default-authentication-plugin=mysql_native_password` removido do `docker-compose.yml`; WAR reconstruído e containers redeploy com sucesso.
- **2026-03-05:** Corrigido HTTP 404 causado por falha no deploy após atualização do driver MySQL (Prioridade 2). Três problemas resolvidos: (1) JARs `eclipselink.jar`, `eclipselink-2.0.2.jar` e `eclipselink-javax.persistence-2.0.jar` removidos de `WEB-INF/lib` — o EclipseLink 2.0.2 bundled conflitava com o EclipseLink 2.7.9 do Payara 5, causando `ClassNotFoundException: Glassfish` no predeploy JPA; (2) `sun-web.xml` atualizado de `delegate="false"` para `delegate="true"` para que o classloader do container tenha precedência; (3) `payara-post-boot.asadmin` corrigido de `com.mysql.jdbc.jdbc2.optional.MysqlDataSource` para `com.mysql.cj.jdbc.MysqlDataSource` (classe correta do Connector/J 8.x). WAR reconstruído e deploy confirmado com HTTP 200.

---

## 15. Variáveis de Ambiente e Verificação

Esta seção lista as variáveis de ambiente que o projeto e os scripts usam, os valores recomendados e os comandos para você validar no Windows (cmd.exe).

### Variáveis e valores esperados

- `JAVA_HOME`
  - Esperado: `C:\Program Files\Java\jdk1.8.0_xxx` (Java 8) ou `C:\Program Files\Java\jdk-17` (Java 17).
  - Uso: compilação (`mvn`) e execução local.

- `M2_HOME` e `PATH` com `mvn`
  - Esperado: `C:\Program Files\apache-maven-3.9.x`, com `%M2_HOME%\bin` no `PATH`.

- `GLASSFISH_HOME`
  - Relevante para scripts legados: `scripts\deploy-war.bat`, etc.
  - Valor padrão nos scripts: `C:\glassfish3`.

- Variáveis do Docker Compose (serviço `db`):
  - `MYSQL_ROOT_PASSWORD=root`
  - `MYSQL_DATABASE=sisdat`
  - `MYSQL_USER=sisdat`
  - `MYSQL_PASSWORD=sisdat`

### Comandos para verificar o ambiente (cmd.exe)

```cmd
echo %JAVA_HOME%
java -version
where mvn
mvn -v
docker --version
docker compose version
docker ps
```

### Como iniciar o ambiente local (estado atual — 2026-02-27)

```powershell
# 1. Construir o WAR completo (WebContent + classes + JARs)
powershell -ExecutionPolicy Bypass -File scripts\build-war.ps1

# 2. Subir os containers (MySQL + Payara) com rebuild da imagem
docker-compose up --build -d

# 3. Acompanhar o deploy
docker-compose logs -f payara

# 4. Acessar a aplicação
# http://localhost:8080/sisdat-web/
```

### Para um reinício completo (limpar volumes do banco)

```powershell
docker-compose down -v
powershell -ExecutionPolicy Bypass -File scripts\build-war.ps1
docker-compose up --build -d
```