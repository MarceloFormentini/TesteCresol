# Frontend
O frontend é uma aplicação web desenvolvida em **React** que permite gerenciar instituições e seus eventos associados. A interface é responsiva e intuitiva, facilitando a criação, edição e remoção de instituições e eventos.

Este projeto se comunica com um backend desenvolvido em **Spring Boot**, utilizando **RabbitMQ** para mensagens assíncronas e **PostgreSQL** como banco de dados.

# Tecnologias Utilizadas
- **React** - Biblioteca principal para a interface do usuário
- **React Router** - Gerenciamento de rotas
- **Axios** - Requisições HTTP para a API
- **Tailwind CSS** - Estilização responsiva e modular
- **Nginx** - Servidor para hospedagem estática em containers Docker
- **Docker** - Containerização da aplicação

# Funcionalidades Implementadas

## 1. **Gerenciamento de Instituições**
- Listagem de todas as instituições cadastradas (paginada)
- Cadastro de uma nova instituição
- Edição de instituição existente
- Exclusão de instituição (com verificação de eventos vinculados)

## 2. **Gerenciamento de Eventos**
- Listagem de eventos vinculados a uma instituição
- Cadastro de novos eventos para uma instituição
- Edição de eventos
- Exclusão de eventos

## 3. **Paginação e Filtros**
- Paginação na listagem de instituições e eventos
- Ordenação por nome e data de criação
- Filtragem de eventos ativos/inativos

## 4. **Validações e Feedback ao Usuário**
- Mensagens de erro ao tentar cadastrar instituições e eventos inválidos
- Alertas ao excluir registros
- Mensagens de confirmação após operações bem-sucedidas

## Componentes Principais

### 📌 **`InstitutionList.jsx`**
- Lista todas as instituições cadastradas
- Permite acessar a tela de criação/edição
- Possui paginação e botão de acesso aos eventos vinculados

### 📌 **`InstitutionForm.jsx`**
- Formulário para criação e edição de instituições
- Validações de campos (nome e tipo)
- Exibe mensagens de erro do backend

### 📌 **`EventList.jsx`**
- Lista todos os eventos de uma instituição
- Paginação e filtragem de eventos ativos/inativos

### 📌 **`EventForm.jsx`**
- Formulário para criação e edição de eventos
- Validação das datas de início e fim
- Conversão automática de datas para o formato esperado pela API

### 📌 **`api.js`**
- Configuração centralizada do **Axios** para chamadas à API
- Funções como `getInstituicoes()`, `createInstituicao()`, `getEventosByInstituicao()`, etc.

## Como Executar o Projeto

### Instalar dependências
```bash
npm install
```

### Rodar a aplicação localmente
```bash
npm start
```
A aplicação será iniciada em [`http://localhost:3000`](http://localhost:3000).