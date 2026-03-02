# Inventário de Dependências - SisDAT

Este documento lista os JARs encontrados em `WebContent/WEB-INF/lib`, a versão inferida (quando possível), o caminho do arquivo e observações iniciais sobre riscos/ações recomendadas.

Gerado: 2026-02-20

## Sumário
- Local analisado: `WebContent/WEB-INF/lib`
- Total de artefatos encontrados: 47

---

| JAR | Caminho | Versão (inferida) | Observações / Risco / Ação recomendada |
| --- | --- | --- | --- |
| cglib-nodep-2.1_3.jar | WebContent/WEB-INF/lib/cglib-nodep-2.1_3.jar | 2.1_3 | Muito antigo; verificar compatibilidade com libs que fazem proxy (wicket, eclipselink). Procurar artefato moderno em Maven. |
| iText-5.0.3.jar | WebContent/WEB-INF/lib/iText-5.0.3.jar | 5.0.3 | Versão antiga de iText (licenciamento mudou em v5+); avaliar uso e migrar para OpenPDF ou iText7 (com atenção à licença). |
| httpcore-4.1.jar | WebContent/WEB-INF/lib/httpcore-4.1.jar | 4.1 | Apache HttpComponents antigo; atualizar para 4.4+ ou 5.x dependendo do código. |
| httpclient-4.1.jar | WebContent/WEB-INF/lib/httpclient-4.1.jar | 4.1 | Verificar chamadas HTTP; atualizar para versão suportada (4.5+ ou 5.x). |
| gson-2.2.4.jar | WebContent/WEB-INF/lib/gson-2.2.4.jar | 2.2.4 | Versão antiga; atualizar para 2.8.x (compatível) é recomendado. |
| ezmorph-1.0.6.jar | WebContent/WEB-INF/lib/ezmorph-1.0.6.jar | 1.0.6 | Usado com json-lib; considerar substituir por jackson/gson moderno. |
| eclipselink.jar | WebContent/WEB-INF/lib/eclipselink.jar | ? (provavelmente 2.x) | Existem múltiplos artefatos EclipseLink no lib; consolidar para uma versão única (preferível Maven coordinate org.eclipse.persistence:eclipselink:2.x). |
| eclipselink-javax.persistence-2.0.jar | WebContent/WEB-INF/lib/eclipselink-javax.persistence-2.0.jar | 2.0 | Adapter para JPA 2.0; muito antigo; ao migrar para Java 17/Jakarta pode ser necessário atualizar para EclipseLink compatível ou usar Hibernate/Jakarta. |
| eclipselink-2.0.2.jar | WebContent/WEB-INF/lib/eclipselink-2.0.2.jar | 2.0.2 | Versão bem antiga; plano: mover para dependência Maven mais recente ou trocar de provedor. |
| commons-logging-1.1.2.jar | WebContent/WEB-INF/lib/commons-logging-1.1.2.jar | 1.1.2 | Logging antigo; ver conflitos com slf4j. Considere unificar logging via slf4j + bridge. |
| commons-lang-2.5.jar | WebContent/WEB-INF/lib/commons-lang-2.5.jar | 2.5 | Commons-lang 2.x (antigo); avaliar migração para Apache Commons Lang 3.x e adaptar chamadas. |
| commons-io-2.0.1.jar | WebContent/WEB-INF/lib/commons-io-2.0.1.jar | 2.0.1 | Versão antiga; atualizar para 2.8+ quando mover para Maven. |
| commons-discovery-0.2.jar | WebContent/WEB-INF/lib/commons-discovery-0.2.jar | 0.2 | Raro; usado por commons-logging em alguns cenários. Revisar necessidade. |
| commons-digester-1.7.jar | WebContent/WEB-INF/lib/commons-digester-1.7.jar | 1.7 | Antiga; manter se código depender diretamente. |
| commons-collections-3.2.1.jar | WebContent/WEB-INF/lib/commons-collections-3.2.1.jar | 3.2.1 | Conhecida por vulnerabilidades em versões 3.x; atualizar para 4.x ou revisar uso. |
| commons-codec-1.5.jar | WebContent/WEB-INF/lib/commons-codec-1.5.jar | 1.5 | Versão antiga; atualizar para 1.15+ recomendado. |
| commons-beanutils-1.8.0.jar | WebContent/WEB-INF/lib/commons-beanutils-1.8.0.jar | 1.8.0 | Histórico de vulnerabilidades; atualizar para 1.9.x+ ou substituir. |
| bean-validator.jar | WebContent/WEB-INF/lib/bean-validator.jar | ? | Pacote genérico — provavelmente Hibernate Validator antigo; identificar manifest para coordenadas corretas. |
| axis.jar | WebContent/WEB-INF/lib/axis.jar | ? (Apache Axis 1.x) | Apache Axis 1.x — EOL e com vulnerabilidades conhecidas. Se possível migrar para JAX-WS ou modernizar stack. |
| javax.inject-1.jar | WebContent/WEB-INF/lib/javax.inject-1.jar | 1 | JSR-330 API jar; fine but check duplication with runtime. |
| javax.servlet-api.jar | WebContent/WEB-INF/lib/javax.servlet-api.jar | ? | API do servlet; normalmente fornecida pelo container - remover do WAR para evitar conflitos. |
| javax.ws.rs-api-2.0.jar | WebContent/WEB-INF/lib/javax.ws.rs-api-2.0.jar | 2.0 | JAX-RS API; container normalmente fornece — avaliar duplicação. |
| json-lib-2.4-jdk15.jar | WebContent/WEB-INF/lib/json-lib-2.4-jdk15.jar | 2.4 | Antiga e baseada em jdk1.5; substituir por Jackson/Gson moderno. |
| json-org.jar | WebContent/WEB-INF/lib/json-org.jar | ? | Versão não clara; avaliar e substituir por coordenada Maven (org.json:json). |
| joda-time-1.5.1.jar | WebContent/WEB-INF/lib/joda-time-1.5.1.jar | 1.5.1 | Muito antiga; JSR-310 (java.time) recommended when on Java 8+, or update Joda to latest if needed. |
| jfreechart-1.0.16.jar | WebContent/WEB-INF/lib/jfreechart-1.0.16.jar | 1.0.16 | Biblioteca gráfica antiga; ok se necessário. |
| jcommon-1.0.20.jar | WebContent/WEB-INF/lib/jcommon-1.0.20.jar | 1.0.20 | Dependência de jfreechart. |
| wsdl4j.jar | WebContent/WEB-INF/lib/wsdl4j.jar | ? | WS stack; avaliar versão e necessidade. |
| wicket-ioc-1.4.22.jar | WebContent/WEB-INF/lib/wicket-ioc-1.4.22.jar | 1.4.22 | Wicket 1.4.22 presente — versão bem antiga (API breaking changes since 6.x/7.x). Planejar análise de migração se desejado. |
| wicket-extensions-1.4.22.jar | WebContent/WEB-INF/lib/wicket-extensions-1.4.22.jar | 1.4.22 | Ver nota Wicket acima. |
| wicket-devutils-1.4.22.jar | WebContent/WEB-INF/lib/wicket-devutils-1.4.22.jar | 1.4.22 | Ferramentas de desenvolvimento Wicket. |
| wicket-auth-roles-1.4.22.jar | WebContent/WEB-INF/lib/wicket-auth-roles-1.4.22.jar | 1.4.22 | Módulo de autenticação para Wicket. |
| wicket-datetime-1.4.22.jar | WebContent/WEB-INF/lib/wicket-datetime-1.4.22.jar | 1.4.22 | Extensão date/time para Wicket; verificar compatibilidade com Java 17. |
| wicket-1.4.22.jar | WebContent/WEB-INF/lib/wicket-1.4.22.jar | 1.4.22 | Núcleo do Wicket (antigo). Revisar esforço de upgrade (migrar para Wicket 9+ pode ser custoso). |
| slf4j-jdk14-1.5.10.jar | WebContent/WEB-INF/lib/slf4j-jdk14-1.5.10.jar | 1.5.10 | Adapter SLF4J para java.util.logging; versão antiga. Recomenda-se unificar logging em SLF4J+Logback ou usar bridges. |
| slf4j-api-1.5.10.jar | WebContent/WEB-INF/lib/slf4j-api-1.5.10.jar | 1.5.10 | Muito antigo; atualizar para 1.7.x+ ou 2.x conforme ecossistema. |
| simple-xml-2.7.jar | WebContent/WEB-INF/lib/simple-xml-2.7.jar | 2.7 | Biblioteca de serialização XML; verificar versão precisa (2.7.x). |
| postgresql-9.1-901.jdbc4.jar | WebContent/WEB-INF/lib/postgresql-9.1-901.jdbc4.jar | 9.1 | Driver antigo (Postgres 9.1 era de 2012); atualizar para driver 42.x quando migrar. |
| portlet-api-1.0.jar | WebContent/WEB-INF/lib/portlet-api-1.0.jar | 1.0 | API de portlet — remover se não for necessário no deploy container. |
| poi-3.9-20121203.jar | WebContent/WEB-INF/lib/poi-3.9-20121203.jar | 3.9 | POI antigo; existe também poi-3.10 — consolidar para uma versão moderna (>=4.x ou 5.x). |
| poi-3.10-FINAL-20140208.jar | WebContent/WEB-INF/lib/poi-3.10-FINAL-20140208.jar | 3.10 | Duplicata de POI; consolidar. |
| mysql-connector-java-5.1.16-bin.jar | WebContent/WEB-INF/lib/mysql-connector-java-5.1.16-bin.jar | 5.1.16 | Driver MySQL antigo; atualizar para 8.x para MySQL 8.0. |
| LogicWicket-1.4.jar | WebContent/WEB-INF/lib/LogicWicket-1.4.jar | 1.4 | Custom/third-party Wicket extension — pode não ter artefato Maven público. Preservar e considerar deploy em repositório interno. |
| log4j-1.2.13.jar | WebContent/WEB-INF/lib/log4j-1.2.13.jar | 1.2.13 | log4j 1.x é EOL e inseguro; migrar para log4j2 or slf4j+logback. |
| jaxrpc.jar | WebContent/WEB-INF/lib/jaxrpc.jar | ? | JAX-RPC (antigo) — substituir por JAX-WS ou modernizar. |
| javax.ejb.jar | WebContent/WEB-INF/lib/javax.ejb.jar | ? | EJB API — normalmente fornecida pelo container; remover do WAR. |
| jasperreports-4.7.1.jar | WebContent/WEB-INF/lib/jasperreports-4.7.1.jar | 4.7.1 | Antigo; atualizar se possível. |

---

## Observações gerais e próximos passos recomendados
1. Remover do `WEB-INF/lib` as APIs providas pelo container (ex.: `javax.servlet-api.jar`, `javax.ejb.jar`, possivelmente `javax.ws.rs-api-2.0.jar`) para evitar conflitos com o runtime escolhido.
2. Consolidar dependências duplicadas (ex.: POI 3.9 e 3.10; múltiplos EclipseLink jars) e migrar para dependências gerenciadas por Maven com coordenadas precisas.
3. Priorizar atualização de drivers e bibliotecas com vulnerabilidades conhecidas: `log4j-1.2.13`, `commons-collections-3.2.1`, `mysql-connector-java-5.1.16`, `postgresql-9.1`, `commons-beanutils`.
4. Identificar JARs sem artifact público (ex.: `LogicWicket-1.4.jar`, possivelmente `bean-validator.jar`) e armazená-los em um repositório interno (Nexus/Artifactory) ou criar um `libs/` com instruções documentadas antes de migrar para Maven.
5. Planejar a estratégia de migração javax→jakarta (se optar por Jakarta EE 9+): dependendo do runtime escolhido, pode ser necessário aplicar transformação de pacote ou usar um runtime que mantenha compatibilidade com `javax`.
6. Adicionar `docs/dependency-inventory.json` ou CSV gerado automaticamente em próximos passos, com checksums e manifest inspection para identificar versões reais.

---

Se desejar, próximo passo automático que posso executar agora:
- Gerar uma saída JSON/CSV com entradas, checksums SHA256 e tentar extrair manifest (Implementation-Version/Bundle-Version) de cada JAR.
- Criar um `pom.xml` base e iniciar a conversão das dependências para coordenadas Maven.

