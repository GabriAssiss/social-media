# 📱 Social Media App

Aplicação completa de rede social composta por **API backend** e **aplicativo mobile Android**, desenvolvida com tecnologias modernas e foco em arquitetura escalável.

---

## 🚀 Visão Geral

O projeto consiste em um sistema de rede social com:

* Backend RESTful robusto
* Aplicativo Android moderno
* Autenticação segura
* Integração com banco de dados relacional

---

## 🧱 Arquitetura do Projeto

```
## 📂 Estrutura do projeto

social-media/
├── code/
│   ├── android/    # Projeto Android (Kotlin + Compose)
│   │   ├── app/
│   │   │   ├── src/
│   │   │   │   ├── main/
│   │   │   │   ├── test/
│   │   │   │   └── androidTest/
│   │   │   ├── build/
│   │   │   ├── build.gradle.kts
│   │   │   └── proguard-rules.pro
│   │   ├── gradle/
│   │   ├── build.gradle.kts
│   │   ├── settings.gradle.kts
│   │   └── gradle.properties
│
│   ├── api/          # Backend (Node.js + TypeScript)
│   │   ├── generated/          # Código gerado (Prisma)
│   │   ├── prisma/             # Schema e migrations
│   │   ├── src/
│   │   │   ├── @types/         # Tipagens customizadas
│   │   │   ├── controllers/    # Regras de negócio
│   │   │   ├── database/       # Configuração do banco
│   │   │   ├── dtos/   # Objetos de transferência de dados
│   │   │   ├── middlewares/ # Middlewares(auth, validações)
│   │   │   ├── repositories/   # Acesso ao banco (Prisma)
│   │   │   ├── routers/        # Definição de rotas
│   │   │   ├── services/       # Lógica de negócio
│   │   │   ├── utils/          # Funções utilitárias
│   │   │   ├── app.ts          # Configuração do Express
│   │   │   └── server.ts       # Inicialização do servidor
│   │   ├── .env
│   │   ├── docker-compose.yml  # Banco PostgreSQL
│   │   ├── package.json
│   │   ├── package-lock.json
│   │   └── tsconfig.json
│
├── README.md
└── LICENSE
```



---

## 🛠️ Tecnologias utilizadas

### 🔙 Backend (API)

* **Node.js**
* **TypeScript**
* **Express 5**
* **Prisma ORM**
* **PostgreSQL**
* **JWT (autenticação)**
* **Bcrypt (hash de senha)**
* **TSX (execução em dev)**
* **Docker (banco de dados)**

---

### 📱 Mobile (Android)

* **Kotlin**
* **Jetpack Compose**
* **Hilt (Dependency Injection)**
* **Retrofit (HTTP Client)**
* **Coil (carregamento de imagens)**
* **Navigation Compose**

---

## ⚙️ Funcionalidades

### 👤 Usuários

* Registro e login
* Autenticação com JWT
* Proteção de rotas

### 📝 Conteúdo

* Criação de posts
* Feed de publicações

### ❤️ Interações

* Curtidas
* Comentários

---

## 🗄️ Banco de Dados

* PostgreSQL
* ORM: Prisma

---

## 🐳 Docker (Banco de Dados)

```yaml id="docker01"
services:
  postgres:
    image: postgres:15-alpine
    container_name: api_db
    ports:
      - "5432:5432"
```

---

## ▶️ Como rodar o projeto

### 🔧 Backend

```bash id="backend01"
cd code

# instalar dependências
npm install

# rodar em desenvolvimento
npm run dev
```

---

### 🔐 Variáveis de ambiente

```env id="env02"
PORT=3000

DB_USER=postgres
DB_PASSWORD=postgres
DB_NAME=social_media

DATABASE_URL=postgresql://USER:PASSWORD@localhost:5432/DB_NAME

JWT_SECRET=your_secret
```

---

### 🐳 Subir banco com Docker

```bash id="docker02"
docker-compose up -d
```

---

### 📱 Mobile (Android)

Abra a pasta `app/` no Android Studio e execute o projeto.

---

## 🔌 Comunicação entre sistemas

O app Android consome a API via:

* **Retrofit**
* URL configurada via variável de ambiente:

```kotlin id="kotlin01"
buildConfigField("String", "API_URL", "\"${System.getenv("API_URL") ?: ""}\"")
```

---

## 🧪 Scripts disponíveis (Backend)

```bash id="scripts02"
npm run dev   # ambiente de desenvolvimento com TSX
```

---

## 📚 Conceitos aplicados

* Arquitetura em camadas
* API REST
* ORM com Prisma
* Autenticação com JWT
* Injeção de dependência (Hilt)
* Consumo de API com Retrofit
* UI declarativa com Compose

---

## 📸 Demonstração

<a href="https://github.com/GabriAssiss/social-media/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=GabriAssiss/social-media" />
</a>

---

## 📄 Licença

MIT

---

## 👨‍💻 Autor

**Gabriel Assis**
https://github.com/GabriAssiss
