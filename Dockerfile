# Dockerfile multi-mode para SisDAT
# Estratégia:
# - Se houver um build Maven (pom.xml) o workflow compila o WAR em target/*.war.
# - Em ausência de um WAR, o Dockerfile copia a pasta WebContent como aplicação web explodida para Tomcat.

FROM tomcat:9.0-jdk17 as runtime

ARG HAS_POM=false
WORKDIR /tmp/app

# Copia todo o contexto para dentro da imagem (usado para deploy da WebContent quando não houver WAR)
COPY . /tmp/app

# Se o WAR foi gerado (via Maven), copia-o para o diretório de webapps do Tomcat como ROOT.war
# Senão, copia a pasta WebContent como aplicação explodida em ROOT/
RUN if [ "$HAS_POM" = "true" ] && ls target/*.war 1> /dev/null 2>&1; then \
      echo "WAR encontrado em target/ - instalando como ROOT.war" && cp target/*.war /usr/local/tomcat/webapps/ROOT.war; \
    else \
      echo "Nenhum WAR encontrado - deployando WebContent como aplicação explodida" && rm -rf /usr/local/tomcat/webapps/* && mkdir -p /usr/local/tomcat/webapps/ROOT && cp -r WebContent/* /usr/local/tomcat/webapps/ROOT/; \
    fi

EXPOSE 8080
CMD ["catalina.sh", "run"]
