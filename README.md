# SisDAT Web Application

Sistema de Dados e Análise Técnica (SisDAT) - Web Application

## 📋 Descrição

SisDAT é uma aplicação web Java EE para gerenciamento de dados e pesquisas técnicas, desenvolvida com Apache Wicket e JPA/EclipseLink.

## 🛠️ Tecnologias

- **Java**: JDK 17
- **Framework Web**: Apache Wicket
- **Servidor de Aplicação**: Payara Server 5.2022.4
- **Banco de Dados**: MySQL 8.0
- **ORM**: JPA com EclipseLink
- **Build**: Maven
- **Containerização**: Docker & Docker Compose

## 🚀 Início Rápido com Docker

### Pré-requisitos
- Docker Desktop instalado
- Docker Compose
- Git

### Executar a Aplicação

1. Clone o repositório:
```bash
git clone <repository-url>
cd sisdat-web
```

2. Inicie os containers:
```bash
docker-compose up -d
```

3. Aguarde os serviços iniciarem (MySQL + Payara)

4. Restaure o banco de dados:
```bash
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < seu_backup.sql
```

5. Acesse a aplicação:
```
http://localhost:8080
```

### Serviços

- **Aplicação Web**: http://localhost:8080
- **MySQL**: localhost:3307 (externo) / 3306 (interno)
  - Database: `sisdat`
  - User: `sisdat`
  - Password: `sisdat`

## 📁 Estrutura do Projeto

```
sisdat-web/
├── src/                    # Código fonte Java
│   ├── com/frw/           # Pacotes da aplicação
│   └── META-INF/          # Configurações JPA
├── WebContent/            # Recursos web (HTML, CSS, JS)
├── scripts/               # Scripts de deploy e configuração
├── docs/                  # Documentação
├── docker-compose.yml     # Configuração Docker
├── Dockerfile.payara      # Imagem Docker do Payara
└── pom.xml               # Configuração Maven
```

## 🔧 Desenvolvimento Local

### Construir o projeto

```bash
mvn clean package
```

O arquivo WAR será gerado em `target/sisdat-web.war`

### Deploy Manual

1. Construa o WAR:
```bash
mvn clean package
```

2. Deploy no Payara/GlassFish:
```bash
# Configure o datasource primeiro
scripts\configure-glassfish-datasource.bat

# Deploy da aplicação
scripts\deploy-war.bat
```

## 🐳 Docker

### Reconstruir imagens

```bash
docker-compose build
```

### Ver logs

```bash
# Logs do Payara
docker-compose logs payara -f

# Logs do MySQL
docker-compose logs db -f
```

### Parar containers

```bash
docker-compose down
```

### Remover volumes (CUIDADO: apaga dados do banco)

```bash
docker-compose down -v
```

## 💾 Banco de Dados

### Backup

```bash
docker-compose exec db mysqldump -u sisdat -psisdat sisdat > backup_$(date +%Y%m%d).sql
```

### Restauração

```bash
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < seu_backup.sql
```

### Conectar via cliente MySQL

```bash
docker-compose exec db mysql -u sisdat -psisdat sisdat
```

Ou via MySQL Workbench/DBeaver:
- Host: `localhost`
- Port: `3307`
- Database: `sisdat`
- User: `sisdat`
- Password: `sisdat`

## ⚙️ Configuração

### MySQL Case Sensitivity Fix

O projeto está configurado com `lower_case_table_names=1` no MySQL para evitar problemas de case-sensitivity em sistemas Linux/Docker. Veja [docs/table-case-sensitivity-fix.md](docs/table-case-sensitivity-fix.md) para detalhes.

### Persistence Unit

A configuração JPA está em `src/META-INF/persistence.xml`:
- Persistence Unit: `xq.pu`
- Datasource: `jdbc/sisdat-ds`
- Provider: EclipseLink

## 📚 Documentação

- [Docker Setup](README_DOCKER.md)
- [Fix de Case Sensitivity](docs/table-case-sensitivity-fix.md)
- [Guia Completo de Fix](README_FIX.md)
- [Restauração do Banco](RESTORE_DATABASE.md)
- [Eclipse GlassFish Setup](docs/eclipse-glassfish-setup.md)
- [Inventário de Dependências](docs/dependency-inventory.md)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Comandos Úteis

### Maven

```bash
# Compilar
mvn compile

# Executar testes
mvn test

# Limpar e construir
mvn clean package

# Pular testes
mvn clean package -DskipTests
```

### Docker

```bash
# Status dos containers
docker-compose ps

# Reiniciar serviço específico
docker-compose restart payara
docker-compose restart db

# Verificar configuração MySQL
docker-compose exec db mysql -u sisdat -psisdat -e "SHOW VARIABLES LIKE 'lower_case_table_names';"

# Acessar shell do container
docker-compose exec payara /bin/bash
docker-compose exec db /bin/bash
```

## 🐛 Troubleshooting

### Erro: "Table 'sisdat.usuario' doesn't exist"

Este erro foi corrigido configurando `lower_case_table_names=1` no MySQL. Se ainda ocorrer:

1. Verifique se o banco foi restaurado:
```bash
docker-compose exec db mysql -u sisdat -psisdat sisdat -e "SHOW TABLES;"
```

2. Verifique a configuração do MySQL:
```bash
docker-compose exec db mysql -u sisdat -psisdat -e "SHOW VARIABLES LIKE 'lower_case_table_names';"
```

3. Se necessário, recrie os containers:
```bash
docker-compose down -v
docker-compose up -d
```

### Aplicação não inicia

Verifique os logs:
```bash
docker-compose logs payara --tail 100
```

### Banco de dados não conecta

1. Verifique se o MySQL está rodando:
```bash
docker-compose ps db
```

2. Teste a conexão:
```bash
docker-compose exec db mysql -u sisdat -psisdat -e "SELECT 1;"
```

## 📄 Licença

[Especificar licença do projeto]

## 👥 Autores

- Juliano
- Carlos Santos
- Miller
- Marcos Lisboa

## 📞 Suporte

Para problemas e dúvidas, abra uma [issue](../../issues) no GitHub.

---

**Última Atualização**: 2026-03-02
