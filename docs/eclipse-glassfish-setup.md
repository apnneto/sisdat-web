# Configurar Eclipse para trabalhar com GlassFish 3.x (e alternativa de depuração remota)

Objetivo: rodar e depurar a aplicação exatamente como no AWS (GlassFish 3.x, EJBs, deploy pelo IDE). Se o plugin "GlassFish Tools" não estiver disponível no seu Eclipse, há um caminho alternativo confiável: rodar GlassFish localmente em modo debug e usar "Remote Java Application" do Eclipse para conectar o depurador.

Pré-requisitos
- Ter o GlassFish 3.1.2.2 (ou versão compatível) disponível localmente. Se precisar baixar, procure por "glassfish 3.1.2.2 download" no site oficial/arquivos históricos.
- Java JDK (compatível com GlassFish 3; Java 6/7/8 conforme necessário para reproduzir ambiente original).
- Eclipse instalado (versão que você já usa). Preferível usar a mesma versão que vem com WTP (Web Tools Platform).

Opção A — Instalar plugin GlassFish / Payara no Eclipse (se disponível)
1. Abrir Eclipse.
2. Ajuda > Eclipse Marketplace...
3. Pesquisar por "GlassFish Tools" ou "Payara Tools".
4. Instalar o plugin encontrado (Payara Tools costuma ser disponível e facilita a integração; GlassFish Tools integra GlassFish diretamente).
5. Reiniciar Eclipse quando solicitado.
6. Abrir a visão "Servers" (Window > Show View > Other... > Server > Servers).
7. New > Server > procurar por **GlassFish 3** (ou Payara Server). Se aparecer, selecione e aponte para o diretório de instalação do GlassFish (GlassFish Home).
8. Configure a JRE usada pelo servidor (use o JDK apropriado).
9. Agora você pode iniciar/parar o servidor pelo Eclipse e fazer deploy do projeto web via contexto "Add and Remove...".

Observação: se o plugin não aparecer no Marketplace, ou não suportar a versão do Eclipse, use a opção B abaixo.

Opção B — (Recomendado como fallback) Rodar GlassFish local em modo debug e conectar o depurador remoto
Esta é a opção mais robusta — não depende do plugin Eclipse e reproduz exatamente o runtime.

Passos:
1. Baixar e extrair GlassFish 3.1.2.2 para um diretório local, ex.: `C:\glassfish3`.
2. Abrir um terminal (cmd.exe) e iniciar o domínio em modo debug:

   C:\glassfish3\bin\asadmin.bat start-domain --debug

   - A saída indicará em qual porta o JPDA (JDWP) está ouvindo (por padrão é 9009). Se a porta for diferente, anote-a.
3. Deploy do WAR:
   - Método rápido: copie/coloque o `yourapp.war` em `C:\glassfish3\glassfish\domains\domain1\autodeploy\` e o GlassFish irá implantar automaticamente.
   - Usando asadmin: (recomendado)

     C:\glassfish3\bin\asadmin.bat deploy C:\caminho\para\seuapp.war

4. Conectar o depurador do Eclipse (Remote Debug):
   - No Eclipse: Run > Debug Configurations...
   - Criar uma nova configuração "Remote Java Application".
   - Project: selecione o projeto web (ou qualquer projeto Java relacionado).
   - Host: localhost
   - Port: 9009 (ou a porta mostrada no passo 2)
   - Click Apply, depois Debug.
   - Coloque breakpoints no código Java (EJBs, Servlets, etc.) — Eclipse irá parar quando o código for atingido.

5. Fazer alterações de código e redeploy:
   - Para deploy rápido, reconstruir WAR (`mvn package` ou export WAR no Eclipse) e usar `asadmin deploy --force` ou sobrescrever o WAR em `autodeploy/`.

## Restaurar o banco local (script.sql) e configurar DataSource no GlassFish
No repositório há um script SQL em `src/META-INF/script.sql` com DDL/DML de inicialização.
Foram adicionados scripts de ajuda em `scripts/`:

- `scripts/restore-mysql.bat` — restaura o arquivo SQL num banco MySQL local.
- `scripts/configure-glassfish-datasource.bat` — copia o driver MySQL para o GlassFish (se houver no projeto), cria o JDBC connection pool e o JDBC resource (JNDI) via `asadmin`.

Ambos os scripts aceitam parâmetros (veja exemplos abaixo). Editar as variáveis no topo dos `.bat` se preferir valores padrão diferentes.

Exemplo de uso (abrir cmd.exe como Administrador se necessário):

1) Restaurar o banco `sisdat` usando o cliente `mysql` (supondo `mysql` no PATH):

```bat
cd C:\Projetos\sisdat-web
scripts\restore-mysql.bat "mysql" sisdat root "sua_senha" src\META-INF\script.sql
```

Parâmetros (ordem):
- Path para o executável `mysql` (ou `mysql` se estiver no PATH)
- Nome do banco (ex: sisdat)
- Usuário do MySQL (ex: root)
- Senha do MySQL (pode ser vazia "")
- Caminho para o arquivo SQL (relativo ou absoluto)

2) Configurar o DataSource no GlassFish (assume GlassFish em `C:\glassfish3`):

```bat
scripts\configure-glassfish-datasource.bat C:\glassfish3 sisdat root "sua_senha" sisdatPool jdbc/sisdat
```

Parâmetros (ordem):
- `GLASSFISH_HOME` (ex: C:\glassfish3)
- Nome do banco (ex: sisdat)
- Usuário do banco
- Senha do banco
- Nome do connection pool a criar (ex: sisdatPool)
- JNDI name do recurso JDBC (ex: jdbc/sisdat)

O script tentará copiar `WebContent\WEB-INF\lib\mysql-connector-java-*.jar` para `C:\glassfish3\glassfish\domains\domain1\lib` se encontrar o driver no repositório; caso contrário você deverá adicionar o driver manualmente.

Após executar `configure-glassfish-datasource.bat`, o script fará um `ping-connection-pool` para validar a conexão. Se o ping falhar, revise as credenciais, URL (host/porta) e se o driver MySQL está instalado no classpath do GlassFish.

Observação de segurança: evite usar senhas em texto puro em scripts; prefira configurar um usuário MySQL específico para desenvolvimento com permissões limitadas e remover credenciais dos scripts antes de commitar em um repositório público.

Dicas e resolução de problemas
- Se GlassFish não iniciar em modo debug, verifique permissões e se a JVM suportada está disponível. Você pode também adicionar manualmente as opções de JVM no `domain.xml` (campo `jvm-options`) para garantir `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9009`.
- Se o Eclipse não conectar, verifique firewall bloqueando a porta 9009.
- Para depurar classes que estão empacotadas em WAR, assegure que os fontes estejam disponíveis no projeto do Eclipse e que as informações de compilação correspondam ao WAR implantado.

Scripts de auxílio
- No repositório foram adicionados scripts em `scripts/` para facilitar iniciar o GlassFish em modo debug e deploy via `asadmin`.

Conclusão
- Se o plugin do GlassFish estiver disponível no Marketplace, instale-o para integração direta no Eclipse (deploy via Servers view). Caso contrário, use o método de depuração remota — é simples e reproduz o ambiente AWS com fidelidade.

Se quiser, eu posso:
- Criar um pequeno script adicional para detectar a porta JPDA automaticamente e abrir a configuração de deploy.
- Preparar um `.launch` do Eclipse (arquivo) que você pode importar.
- Baixar e preparar instruções passo-a-passo para instalar o GlassFish 3.1.2.2 localmente (se quiser que eu gere um checklist completo).