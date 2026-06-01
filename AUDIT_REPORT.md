# 📋 RELATÓRIO DE VARREDURA DE CÓDIGO
**Data:** 1 de Junho de 2026  
**Status:** ✅ Sem erros de compilação encontrados

---

## 1. ✅ FLUXO DE AUTENTICAÇÃO - BEM IMPLEMENTADO

### Android (Cliente)
- ✅ **Registro**: `RegisterViewModel` → `AuthRepositoryImpl` → `AuthService` → API
- ✅ **Login**: `AuthViewModel` valida credenciais, salva token em `SharedPreferences`
- ✅ **Token Management**: `TokenManager` extrai `id` do payload JWT usando Base64 decode
- ✅ **Logout**: Limpa token e estado corretamente via `clearToken()`
- ✅ **Interceptor**: `AuthInterceptor` injeta "Bearer {token}" em headers HTTP
- ✅ **FCM Token**: `FirebaseMessagingService.onNewToken()` chama `updateFcmToken()` quando token renova

### API (Servidor)
- ✅ **Registro**: `AuthService.create()` com validação de email/phone/name únicos
- ✅ **Hashing**: Usa `bcrypt` com salt 10 para senhas
- ✅ **Login**: `AuthService.login()` verifica senha com `bcrypt.compare()`
- ✅ **JWT**: Assina com `JWT_SECRET`, expira em `JWT_EXPIRES_IN` (padrão 1h)
- ✅ **Middleware Auth**: Verifica "Bearer" token, extrai payload, valida usuário existe
- ✅ **FCM Token**: `updateFcmToken()` salva token no Prisma após validação

### ⚠️ GAPS IDENTIFICADOS:

#### 1. **Refresh Token NÃO implementado**
- **Problema**: Token expira em 1h, sem mecanismo de renovação
- **Impacto**: Usuário será desconectado após 1h de inatividade
- **Recomendação**: Implementar refresh token com sliding window
```typescript
// Sugestão para API:
async refreshToken(refreshToken: string) {
  const payload = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
  const newToken = jwt.sign({ id: payload.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
  return newToken;
}
```

#### 2. **Token expirado não é tratado no Android**
- **Problema**: Se token expirar, requisições retornarão 401, mas app não detecta
- **Recomendação**: Adicionar interceptor para logout automático em 401
```kotlin
// No AuthInterceptor
if (response.code == 401) {
  tokenManager.clearToken()
  // Redirecionar para login
}
```

#### 3. **Criação de usuário retorna senha em plaintext**
- **Problema**: `AuthController.create()` retorna `password` no JSON
- **Impacto**: Risco de segurança (não deve expor senha)
- **Recomendação**: Remover campo `password` da resposta
```typescript
// Em auth.controller.ts linha 24
// ATUAL:
password: newUser.password  // ❌ REMOVAR

// CORRETO: não incluir password
```

---

## 2. ✅ FLUXO DE CHAT - BEM IMPLEMENTADO

### Android (Cliente)
- ✅ **Conexão Socket**: `SocketManager.connect()` autentica com token JWT
- ✅ **Enviar Mensagem**: `sendMessage(receiverId, text)` emite evento `send_message`
- ✅ **Receber Mensagem**: `newMessageFlow()` retorna `Flow<JSONObject>` em IO thread
- ✅ **Histórico**: `ChatViewModel.loadHistory()` carrega últimas 50 mensagens com paginação
- ✅ **Listen Socket**: Múltiplos flows (`newMessageFlow`, `messageSentFlow`) dentro de `viewModelScope`
- ✅ **Memory Leak Protection**: `onCleared()` desconecta socket e cancela flows
- ✅ **Rate Limiting Local**: Impede envio de múltiplas mensagens em <500ms

### API (Servidor)
- ✅ **Socket.IO Integration**: `initSocket(server)` anexado antes de `server.listen()`
- ✅ **JWT Auth**: Middleware valida token antes de permitir conexão
- ✅ **send_message**: Valida `receiverId` e `userId`, cria em MongoDB
- ✅ **Persistência**: `Message.create()` armazena em MongoDB Mongoose
- ✅ **Notificações**: Se receptor offline, envia FCM se `fcmToken` disponível
- ✅ **get_messages**: Agregação MongoDB com paginação (50 msgs, ordenadas)
- ✅ **read_messages**: Marca mensagens como lidas via `updateMany`
- ✅ **Online Status**: Emite `user_online`/`user_offline` globalmente

### ✅ MELHORIAS APLICADAS:
- ✅ Socket inicializado DEPOIS de criar server HTTP (commit 826db52)
- ✅ `flowOn(Dispatchers.IO)` em todos os socket flows
- ✅ Rate limiting no servidor (500ms entre mensagens)

### ⚠️ GAPS IDENTIFICADOS:

#### 1. **Tipagem JSON frágil no Android**
- **Problema**: `parseMessage()` acessa campos com `.getInt()`, `.getString()` sem null safety total
- **Recomendação**: Usar Gson/Moshi para deserializar JSON
```kotlin
// ATUAL:
id = json.getString("_id"),  // Pode falhar se campo não existe

// MELHOR:
data class MessageResponse(
  @SerializedName("_id") val id: String,
  val text: String,
  val senderId: Int
)
val message = Gson().fromJson(json.toString(), MessageResponse::class.java)
```

#### 2. **MongoDB _id vs id inconsistência**
- **Problema**: API retorna MongoDB `_id`, mas Android espera `id`
- **Impacto**: `parseMessage()` acessa `json.getString("_id")` mas deveria ser normalizado
- **Recomendação**: Serializar como `id` ao invés de `_id`
```typescript
// Em chat.controller.ts:
return res.status(200).json(
  messages.map(m => ({
    id: m._id,  // Renomear _id para id
    ...m
  }))
);
```

#### 3. **Confirmação de leitura fraca**
- **Problema**: `read_messages` event não é usado no Android
- **Recomendação**: Implementar visual de "lido" em MessageDto
```kotlin
data class MessageDto(
  // ...
  val read: Boolean = false,  // Novo campo
  val readAt: String? = null
)
```

#### 4. **Sem tratamento de erro de desconexão**
- **Problema**: Se socket cai, app não reconecta automaticamente
- **Recomendação**: Implementar retry com exponential backoff
```kotlin
fun reconnect() {
  if (!socket?.connected() == true) {
    socket?.disconnect()
    // Retry com delay
    Handler(Looper.getMainLooper()).postDelayed({ connect() }, 2000)
  }
}
```

---

## 3. ✅ FLUXO DE USUÁRIOS E BUSCA - IMPLEMENTADO COM GAPS

### Android (Cliente)
- ✅ **Meu Perfil**: `ProfileViewModel.getProfile()` chama `myProfile()`
- ✅ **Busca**: `ConversationsViewModel.observeSearchQuery()` com debounce de 300ms
- ✅ **Paginação Local**: `UserSearchBar` carrega 10 usuários, +10 ao scroll (commit 43684f8)
- ✅ **Seguidores/Seguindo**: Conta exibida via `ProfileUiState`
- ✅ **Follow/Unfollow**: `FollowRepository.follow()` chama API

### API (Servidor)
- ✅ **My Profile**: `myProfile()` retorna contadores via agregação Follow
- ✅ **Search**: `searchConnections()` agrupa recomendados vs outros
- ✅ **Recomendações**: Usuários que você segue + que te seguem aparecem primeiro
- ✅ **Filtro**: Search case-insensitive com Prisma `insensitive` mode

### ⚠️ GAPS IDENTIFICADOS:

#### 1. **Busca sem paginação no servidor**
- **Problema**: `searchConnections()` retorna TODOS os usuários (sem limite)
- **Impacto**: Apps com milhões de usuários ficarão lento
- **Recomendação**: Implementar paginação
```typescript
// Em users.controller.ts
async searchConnections(req: Request, res: Response) {
  const limit = 50;
  const page = Number(req.query.page) || 1;
  const skip = (page - 1) * limit;
  
  // Adicionar .take(limit).skip(skip)
}
```

#### 2. **Sem endpoint para buscar recomendações**
- **Problema**: Busca retorna todos, mas UI mostra "recomendados vs outros"
- **Recomendação**: Separar em 2 endpoints ou retornar com flag
```typescript
// Retornar com estrutura:
{
  recommended: [ /* usuários que você segue */ ],
  others: [ /* resto */ ]
}
```

#### 3. **Session expiry não redireciona no Android**
- **Problema**: `ProfileViewModel` seta `isSessionExpired = true` mas UI não usa
- **Recomendação**: Em `ProfileView`, detectar e navegar para login
```kotlin
if (uiState.isSessionExpired) {
  // Navegar para login
}
```

#### 4. **Sem validação de entrada no Android**
- **Problema**: Search query pode ser vazia ou muito longa
- **Recomendação**: Validar antes de chamar API
```kotlin
val query = searchQuery.value.trim()
if (query.length < 2) {
  _searchState.value = UserSearchUiState.Idle
  return
}
```

#### 5. **Falta endpoint de unfollow na API**
- **Problema**: Existe `unfollow()` no `FollowService`, mas não há rota HTTP
- **Recomendação**: Adicionar POST/DELETE `/api/v1/follows/unfollow`
```typescript
// Em follows.router.ts
router.delete('/:followedId', followController.unfollow);
```

---

## 4. ✅ ENVIRONMENT VARIABLES - PARCIALMENTE CONFIGURADO

### Variáveis Obrigatórias:
```env
# API
JWT_SECRET=                    # ✅ Usado para assinar tokens
JWT_EXPIRES_IN=1h              # ✅ Padrão 1h
PORT=3000                      # ✅ Padrão 3000
DATABASE_URL=                  # ✅ Prisma (PostgreSQL)
MONGODB_URI=                   # ✅ Mongoose (mensagens)

# Firebase
FCM_PROJECT_ID=                # ✅ Validado em firebase.client.ts
FCM_CLIENT_EMAIL=              # ✅ Validado
FCM_PRIVATE_KEY=               # ✅ Validado (escape \n)

# Android
BuildConfig.API_URL=           # ✅ Em build.gradle.kts
BuildConfig.FCM_SERVER_KEY=    # ⚠️ Não usado
```

### ⚠️ PROBLEMAS:

#### 1. **JWT_SECRET sem fallback seguro**
- **Problema**: `auth.service.ts` usa `process.env.JWT_SECRET ?? ""`
- **Impacto**: String vazia aceitará qualquer token inválido
- **Recomendação**: Lançar erro na inicialização
```typescript
const JWT_SECRET = process.env.JWT_SECRET;
if (!JWT_SECRET) throw new Error('JWT_SECRET não configurado');
```

#### 2. **Firebase private key com escape incorreto**
- **Problema**: Em `.env`, `\n` é literal, precisa de replace em runtime (✅ já feito)
- **Status**: ✅ Corrigido em `firebase.client.ts` com `replace(/\\n/g, '\n')`

#### 3. **MONGODB_URI não está tipado**
- **Problema**: Sem validação, pode estar undefined
- **Recomendação**: Usar `requireEnv()` similar ao Firebase
```typescript
const mongoUri = process.env.MONGODB_URI;
if (!mongoUri) throw new Error('MONGODB_URI não configurado');
```

---

## 5. 🔴 ERROS CRÍTICOS A CORRIGIR

### ❌ 1. Password retornada em Create User
**Arquivo**: `/api/src/controllers/auth.controller.ts` linha 24  
**Problema**: Campo `password` sendo retornado ao cliente  
**Risco**: Segurança (exposição de dados sensíveis)  
```typescript
// ATUAL (❌)
password: newUser.password

// CORRETO (✅)
// Remover essa linha
```

### ❌ 2. JWT_SECRET com fallback vazio
**Arquivo**: `/api/src/services/auth.service.ts` linha 40  
**Problema**: `process.env.JWT_SECRET ?? ""`  
**Risco**: Qualquer token será aceito se env não definido  
```typescript
// ATUAL (❌)
const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET ?? "", { ... })

// CORRETO (✅)
const JWT_SECRET = process.env.JWT_SECRET;
if (!JWT_SECRET) throw new Error('JWT_SECRET não definido');
const token = jwt.sign({ id: user.id }, JWT_SECRET, { ... })
```

### ⚠️ 3. _id vs id inconsistência
**Arquivo**: `/api/src/controllers/chat.controller.ts` e Android  
**Problema**: MongoDB retorna `_id`, Android espera `id`  
**Solução**: Normalizar na serialização
```typescript
// Em getHistory response
return res.status(200).json(
  messages.map(m => ({
    id: String(m._id),  // Renomear
    senderId: m.senderId,
    receiverId: m.receiverId,
    text: m.text,
    image: m.image,
    read: m.read,
    createdAt: m.createdAt
  }))
);
```

### ⚠️ 4. Falta endpoint de Unfollow
**Arquivo**: `/api/src/routers/follows.router.ts`  
**Problema**: Rota DELETE não existe  
**Solução**: Adicionar rota DELETE `/:followedId`

### ⚠️ 5. Session expiry não causa logout no Android
**Arquivo**: `/android/app/src/main/java/.../ProfileViewModel.kt`  
**Problema**: Seta `isSessionExpired = true` mas UI não navega  
**Solução**: Implementar observer para navegação em ProfileView

---

## 6. 🟡 MELHORIAS RECOMENDADAS

### Performance
- [ ] **Paginação no Search**: Adicionar `limit` e `skip` em `searchConnections()`
- [ ] **Cache de Perfil**: Implementar cache local em ProfileViewModel (1-5min)
- [ ] **Lazy Load Conversations**: Carregar primeiras 10, depois sob demanda

### Segurança
- [ ] **Rate Limiting Global**: Middleware no Express para limitar requisições por IP
- [ ] **HTTPS Only**: Forçar HTTPS em produção
- [ ] **CORS Apropriado**: Ao invés de `*`, especificar domínios da app

### Confiabilidade
- [ ] **Retry Logic**: Implementar exponential backoff para falhas de API
- [ ] **Offline First**: Sincronizar mensagens quando reconectar
- [ ] **Health Check**: Endpoint `/health` para verificar server status

### UX
- [ ] **Loading States**: Adicionar skeleton loaders nas listas
- [ ] **Empty States**: Mensagens claras quando sem resultados
- [ ] **Sync Status**: Indicador visual de "sincronizando mensagens"

### Code Quality
- [ ] **Remove console.log**: Linha 53 em `ProfileViewModel.kt` (`println("DEBUG: $response")`)
- [ ] **TODO Comments**: 1 comentário TODO em `data_extraction_rules.xml`
- [ ] **Type Safety**: JSON parsing em Socket usar Gson/Moshi

---

## 7. ✅ ARQUITETURA VALIDADA

### Clean Architecture - Bem Implementada ✅
```
Android:
├── domain/         (interfaces: AuthRepository, ChatRepository, etc)
├── data/           (implementações + DTOs + remote)
└── ui/             (ViewModels + Composables)

API:
├── domain/         (DTOs, tipos)
├── services/       (lógica de negócio)
├── repositories/   (queries Prisma/Mongoose)
├── controllers/    (handlers HTTP)
└── middlewares/    (auth, error)
```

### Dependency Injection - Bem Configurado ✅
- Android: Hilt com `@HiltViewModel`, `@Singleton`
- API: Singletons com `new Repository()`

### State Management - Bem Implementado ✅
- Android: `MutableStateFlow` + `.asStateFlow()` + `StateFlow<T>`
- Flows: `.distinctUntilChanged()`, `.debounce()`, `.flowOn(Dispatchers.IO)`

### Error Handling - Bem Implementado ✅
- Android: `Result<T>.onSuccess/.onFailure`
- API: Custom `ApiError` classes (BadRequestError, UnauthorizedError, etc)

---

## 8. 📊 RESUMO DA VARREDURA

| Aspecto | Status | Observações |
|---------|--------|------------|
| Compilação | ✅ 0 erros | TypeScript strict mode OK |
| Autenticação | ✅ Funcional | Falta refresh token |
| Chat/Socket.IO | ✅ Funcional | Falta tipagem JSON robusta |
| Usuários/Busca | ✅ Funcional | Falta paginação servidor |
| Arquitetura | ✅ Clean | Domain/Data/UI bem separado |
| DI/Hilt | ✅ Bem feito | Singletons apropriados |
| State Management | ✅ Robusto | Flows com IO dispatcher |
| Error Handling | ✅ Presente | Custom exceptions OK |
| Env Vars | ⚠️ Parcial | JWT_SECRET sem validation |
| Security | ⚠️ Crítico | Password em resposta POST |
| FCM Integration | ✅ OK | Inicialização corrigida |
| Testes | ❌ Nenhum | Sem testes unitários |

---

## 9. 🎯 PRÓXIMAS AÇÕES (Prioridade)

### 🔴 Crítico (Hoje)
1. [ ] Remover `password` da resposta de registro (linha 24 auth.controller.ts)
2. [ ] Validar `JWT_SECRET` no bootstrap
3. [ ] Normalizar `_id` → `id` em respostas MongoDB

### 🟡 Alto (Esta semana)
4. [ ] Implementar endpoint DELETE `/follows/:followedId`
5. [ ] Adicionar paginação em `searchConnections()`
6. [ ] Implementar refresh token
7. [ ] Adicionar logout automático em 401

### 🟢 Médio (Próximas 2 semanas)
8. [ ] Usar Gson para JSON parsing no Socket
9. [ ] Adicionar health check endpoint
10. [ ] Implementar cache local de perfil
11. [ ] Remover `println("DEBUG")` do código

---

**Relatório compilado por:** Code Audit Agent  
**Total de arquivos analisados:** 50+  
**Padrão de código:** ⭐⭐⭐⭐ (4/5)
