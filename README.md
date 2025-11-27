# **Ziro — Sistema de Gestão Financeira Inteligente**  

Pequenas e médias empresas enfrentam dificuldades em organizar e interpretar suas finanças, o que compromete decisões estratégicas (Serasa Experian, 2020; Contábeis, 2023). Diante desse cenário, surge a necessidade de soluções acessíveis que facilitem o controle e a análise dos dados financeiros (Oliveira Filho & Grossi, 2010; Revista Espacios, 2015).

O **Ziro** foi criado com esse propósito: fornecer uma aplicação web simples, moderna e inteligente para gestão financeira.  
A plataforma permite registrar e analisar lançamentos, visualizar saldos, acompanhar categorias financeiras e — como diferencial — utiliza **inteligência artificial** para identificar padrões, detectar anomalias e sugerir oportunidades de economia, apoiando decisões mais estratégicas.

---

## 🌐 **Ambiente de Produção**

Acesse a versão online do Ziro:  
👉 **http://webziro.duckdns.org:5173/login**

### 🔑 Usuário de Demonstração  
*(Apenas para testes — não use credenciais reais)*

```makefile
email: demo1@ziro.com  
senha: Demo123
```
```makefile
email: demo2@ziro.com  
senha: Demo123
```
---

## 🚀 **Tecnologias Utilizadas**

### **Frontend**
- **Vue.js 3 (Composition API)**  
- **TypeScript**  
- **Pinia** para gerenciamento de estado  
- **Vue Router**  
- **Axios** para consumo da API  
- **Vite** para build e desenvolvimento  

### **Backend**
- Java + Spring Boot  
- Spring Security  
- JPA/Hibernate  
- PostgreSQL  
- Docker Compose para orquestração  

### **Inteligência Artificial**
- Integração com **API Gemini**, responsável por:
  - análise de dados financeiros,
  - identificação de padrões e tendências,
  - detecção de anomalias,
  - geração de insights automatizados.

---

## 🔍 Qualidade do Código — SonarCloud

### Frontend
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gchavess_ziro-frontend&metric=alert_status)](https://sonarcloud.io/project/overview?id=gchavess_ziro-frontend)

### Backend
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gchavess_ziro-backend&metric=alert_status)](https://sonarcloud.io/project/overview?id=gchavess_ziro-backend)

---

## 🏗️ **Arquitetura do Projeto**

- Arquitetura baseada em **MVC (Model–View–Controller)**.  
- Comunicação entre frontend e backend via **API RESTful**.  
- Módulo de IA recebe dados processados e retorna insights.  
- Estrutura modular e escalável.  
- Interface responsiva, leve e focada na experiência do usuário.  

---

# 💻 **Como Executar o Projeto**

> Abaixo temos instruções separadas para frontend e backend. Cada um tem seu próprio `docker-compose` e deve ser executado no seu respectivo diretório.

## **📌 1. FRONTEND**

### 1. Clonar o repositório
```bash
git clone https://github.com/gchavess/ziro-frontend.git
cd ziro-frontend
```

### 2. Instalar dependências
```bash
yarn install
```

### 3. Subir o container do frontend
```bash
docker compose up -d --build
```

### 4. Acessar no navegador
```text
http://localhost:5173/
```

---

## 🔧 **2. BACKEND**

O backend é executado separadamente e possui seu próprio `docker-compose` no repositório `ziro-backend`.

### 1. Clonar o repositório do backend
```bash
git clone https://github.com/gchavess/ziro-backend.git
cd ziro-backend
```

### 2. Subir os containers (API + PostgreSQL)
```bash
docker compose up -d --build
```

### 3. API rodando em:
```text
http://localhost:8080/
```

> Dica: caso existam problemas de CORS ao acessar a API pelo frontend, confirme se o backend está permitindo requisições do `http://localhost:5173` (ou ajuste a porta do frontend).

---

## ⚙️ Integração Frontend ↔ Backend
- O frontend consome a API REST exposta pelo backend. Por padrão, as URLs locais são `http://localhost:5173` (frontend) e `http://localhost:8080` (backend).
- Ajuste a variável de ambiente do frontend que aponta para a URL da API caso esteja usando outro host/porta (ex.: `VITE_API_BASE_URL` no `.env` do frontend).

---

## 👤 **Autor**
**Gustavo Chaves**  
🔗 GitHub: https://github.com/gchavess
