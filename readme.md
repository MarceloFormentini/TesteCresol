# Projeto
Teste técnico para desenvolvedor Java/React Fullstack

Projeto de um sistema CRUD onde os usuário podem cadastrar eventos com uma data de vigência (data de inicio e fim). A partir dessa data, os eventos se tornam ativos automaticamente e ao final desta data são inativados. Cada evento pertence a uma instituição que pode ter um ou mais eventos.

# Docker
Docker é uma plataforma que containeriza aplicações, tornando-as portáveis e fáceis de rodas em qualquer ambiente.

## Por que isso é importante
- Elimina o problema de "funciona na minha máquina";
- Facilita o deploy e escalabilidade;
- Permitee isolar dependência e rodas múltiplos serviços com facilidade;

## Como funciona a construção da aplicação com Docker?
- criamos um Dockerfle que define o ambiente da aplicação;
- Usamos Docker Compose para gerenciar múltiplos containers;
- Executamos a aplicação dentro do caontainer de forma isolada;
- Principais componentes do projeto:

| Componente | Função |
|------------|--------|
| Dockerfile | Define a imagem do contaner com as dependenências necessárias.|
| Docker Compose | Orquestra containers e serviços. |
| Volumes | Mantêm dados persistentes entre reinícios |
| Redes | Permitem a comunicação entre containers |
- Diferencial: Usar Docker facilita desenvolvimento, teste e deploy da aplicação sem conflitos de ambiente.
- Comparação:

| Sem Docker | Com Docker |
|------------|--------|
| Dependências instaladas manualmente. | Dependências gerenciadas via Dockerfile. |
| Conflitos entre versões de bibliotecas | Cada container tem seu próprio ambiente isolado. |
| Deploy pode falhar por diferenças de ambiente. | Imagem do container garante consistencia. |
| Difícil configurar múltiplos serviços. | Doker Compose facilita a orquestração. |

# Dockerfile Backend
Usado o build em duas etapas para evitar que o container final tenha dependências desnecessárias, reduzindo o tamanho da imagem final.
1. Usa a imagem oficial do Maven com Java 21.
2. Define ```/app``` como o diretório de trabalho dentro do container.
3. Copia o arquivo ```pom.xml``` e baixa as dependências antes de copiar o código-fonte, agilizando o build, pois as dependências são baixadas apenas uma vez.
4. Copia todo o código fonte e compila o projeto, gerando o JAR.
5. Usa uma imagem leve do **Java 21** para executar a aplicação e define a pasta ```/app``` como diretório de trabalho.
6. Copia o JAR da etapa anterior para o container final.
7. Expõe a porta 9090 para permitir conexões.
8. Define o comando de execução da aplicação.

# Dockerfile Frontend
1. Usa  uma imagem do **Node.js 18** como base.
2. Define ```/app``` como o diretório de trabalho dentro do container.
3. Copia o arquivo **package.json** e instala as dependências, acelerando os builds futuros.
4. Copia o código-fonte e gera o build do React.
5. Usa Nginx, servidor leve de HTTP e de alta performance.
6. Apaga os arquivos padrão do Nginx e copia o build do React.
7. Expõe a porta 80 e executa o Nginx.

# Docker-Compose
1. PostgreSQL - banco de dados para armazenar instituições e eventos.
	- ```image``` - Usando a imagem oficial do PostegreSQL versão 14;
	- ```container_name``` - Nome do container defino com **postgres**;
	- ```restart``` - Configurado para o container reiniciar automaticamente caso ocorra falhas;
	- ```environment``` - As variáveis de ambiente de configuração do banco definidas com valor **postgres** para usuário e senha e nome do banco de dados como **db_cresol**;
	- ```ports``` - Expõe a porta 5432, padrão do PostgreSQL;
	- ```networks``` - Definido a rede para conectar o container à rede do Docker;
2. RabbitMQ - sistema de mensageria para processar eventos assíncronos.
	- ```image``` - Definido a versão 3.13-management que tem interface de administração;
	- ```container_name``` - Nome do container definido como **rabbitmq**;
	- ```restart``` - Configurado para o container reiniciar automaticamente caso ocorra falhas;
	- ```enviroment``` - Definido o usuário e senha como **guest**;
	- ```ports``` - Configurado as portas de acesso. Porta 5672 para conexão com o banckend e a porta 15672 para acesso a interface web.
3. Backend (Spring Boot) - API que gerencia o sistema.
	- ```build``` - Informado que o Dockerfile está dentro do diretório do projeto backend e o caminho do Dokerfile;
	- ```container_name``` - Nome do container definido como **backend**;
	- ```restart``` - Configurado para o container reiniciar automaticamente caso ocorra falhas;
	- ```depends_on``` - Define que o container só inicia após o PostegreSQL e RabbitMQ estarem executando;
	- ```environment``` - Configuração de conexão para o PostgreSQL e RabbitMQ;
	- ```ports``` - Expõe a porta 9090 para a API;
4. Frontend (React + Nginx) - interface de usuário para interação com a API.
	- ```build``` - Informado que o Dockerfile está dentro do diretório do projeto backend e o caminho do Dokerfile;
	- ```container_name``` - Nome do container definido como **frontend**;
	- ```restart``` - Configurado para o container reiniciar automaticamente caso ocorra falhas;
	- ```depends_on``` - Define que o container só inicia após o Backend estar executando;
	- ```ports``` - Mapeia a porta 80 do Nginx e expõe a porta 3000 do host;
5. Networks - Cria uma rede isolada onde todos os container podem se comunicar.
	- ```driver``` - usa um driver de rede bridge para a comunucação entre os serviços;
6. Rodar o projeto
Na pasta raiz do projeto executar o comando
```
docker-compose up --build
```
- Acessando os serviços
  - backend - ```http://localhost:9090```
  - frontend - ```http://localhost:3000```
  - banco de dados - ```localhost:5432``` com usuário **postgres** e senha **postgres**
  - RabbitMQ - ```http://localhost:15672```