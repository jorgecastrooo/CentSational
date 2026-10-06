# CentSational: Plano do Projeto

App Android de finanças pessoais, **100% offline**, com leitura do QR das faturas portuguesas.

**Stack:** Kotlin · Jetpack Compose · Room · Hilt · MVVM com camadas (Clean Architecture)

---

## Índice

1. [Objetivo](#1-objetivo)
2. [Funcionalidades](#2-funcionalidades)
3. [Arquitetura](#3-arquitetura)
4. [Estrutura de pastas](#4-estrutura-de-pastas)
5. [Base de dados](#5-base-de-dados)
6. [Fases de desenvolvimento](#6-fases-de-desenvolvimento)
7. [Como trabalhar cada funcionalidade](#7-como-trabalhar-cada-funcionalidade)
8. [Convenções](#8-convenções)
9. [Decisões técnicas](#9-decisões-técnicas)
10. [Glossário](#10-glossário)

---

## 1. Objetivo

Uma app para registar receitas e despesas, controlar o orçamento mensal e ver para onde vai o dinheiro. O diferenciador é ler o **QR code das faturas portuguesas** (NIF, data, total) e sugerir a categoria automaticamente.

Objetivos do projeto:

- Ser uma app profissional e utilizável no dia a dia
- Mostrar no portfolio uma arquitetura por camadas, testes e boas práticas
- Servir para aprender Android nativo com Kotlin

---

## 2. Funcionalidades

### Essenciais (MVP)

- Registar, editar e apagar transações (receita ou despesa)
- Categorias com ícone e cor
- Orçamento mensal com alertas aos 80% e 100%
- Dashboard: saldo, gastos do mês, gráfico circular por categoria
- Histórico com filtros por mês e categoria

### Diferenciadoras

- **Scanner de QR de faturas PT** com leitura de NIF, data e total
- **Auto-categorização** pelo NIF do emissor (aprende com o uso)

### Extras (só depois do essencial estar sólido)

- Previsão de gastos até ao fim do mês
- Despesas recorrentes (renda, subscrições)
- Insights ("gastaste +20% em restauração")
- Exportar CSV
- Bloqueio com biometria ou PIN

---

## 3. Arquitetura

```
┌───────────────────────────────────────────────┐
│  UI        Screen (Compose)                   │  desenha
│            ViewModel                          │  guarda o estado, recebe ações
├───────────────────────────────────────────────┤
│  DOMAIN    Modelos, interfaces dos            │  o que a app É
│            repositórios, use cases            │  (Kotlin puro, sem Android)
├───────────────────────────────────────────────┤
│  DATA      Entity, DAO, AppDatabase,          │  como se GUARDA
│            mappers, repositórios (Impl)       │
└───────────────────────────────────────────────┘
        DI (Hilt) cria e liga tudo por fora
```

**Regra de ouro:** cada camada só conhece a de baixo. O ecrã nunca fala com o Room.

### Fluxo de dados

```
ESCREVER   Screen → ViewModel → Repositório → mapper → DAO → Room

LER        Room → Flow → DAO → mapper → Repositório → ViewModel (StateFlow) → Screen
```

- **Eventos sobem:** clique → ViewModel → repositório
- **Dados descem:** Room → repositório → ViewModel → ecrã

### O que cada peça faz

| Peça | Responsabilidade |
|---|---|
| **Screen** | Desenha o estado e envia os cliques ao ViewModel |
| **ViewModel** | Guarda o estado do ecrã e expõe ações. Sobrevive a rotações |
| **Repositório (interface)** | Lista do que a app pode fazer com os dados (vive no domain) |
| **Repositório (Impl)** | Escreve o código: chama o DAO e usa os mappers (vive no data) |
| **DAO** | Operações na tabela (`@Insert`, `@Query`...). O Room gera o código |
| **Entity** | A tabela, com tipos que o SQLite entende (`Long`, `String`) |
| **Modelo (domain)** | O dado "limpo", com tipos úteis (`LocalDate`, enum) |
| **Mapper** | Traduz Entity ↔ Modelo |
| **Hilt** | Cria os objetos e entrega-os a quem os pede no construtor |

### Hilt: quem cria o quê

| Situação | Anotação |
|---|---|
| Classe tua, que podes marcar | `@Inject constructor` |
| Classe que não podes marcar (Room) | `@Provides` num módulo |
| Interface → implementação | `@Binds` num módulo |
| Uma só instância na app | `@Singleton` |
| ViewModel | `@HiltViewModel` + `hiltViewModel()` no ecrã |

Cadeia de criação:

```
Context → AppDatabase → DAO → RepositórioImpl → ViewModel → Screen
```

---

## 4. Estrutura de pastas

Legenda: ✅ feito · 🟡 parcial · ⬜ por fazer

```
com.example.centsational
├── CentSationalApp.kt                  ✅  @HiltAndroidApp
├── MainActivity.kt                     ✅  @AndroidEntryPoint
│
├── di/
│   ├── DatabaseModule.kt               ✅  cria AppDatabase e DAOs
│   └── RepositoryModule.kt             ✅  liga interface → Impl
│
├── domain/
│   ├── model/
│   │   ├── Transaction.kt              ✅
│   │   ├── Category.kt                 ✅
│   │   ├── TransactionType.kt          ✅
│   │   └── Budget.kt                   ⬜
│   ├── repository/
│   │   ├── TransactionRepository.kt    ✅
│   │   ├── CategoryRepository.kt       ✅
│   │   └── BudgetRepository.kt         ⬜
│   ├── usecase/                        ⬜  GetMonthlySummaryUseCase, ...
│   └── qr/
│       └── InvoiceQrParser.kt          ⬜  Kotlin puro, fácil de testar
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt              ✅
│   │   ├── dao/
│   │   │   ├── TransactionDao.kt       ✅
│   │   │   ├── CategoryDao.kt          ✅
│   │   │   └── BudgetDao.kt            ⬜
│   │   └── entity/
│   │       ├── TransactionEntity.kt    ✅
│   │       ├── CategoryEntity.kt       ✅
│   │       └── BudgetEntity.kt         ⬜
│   ├── mapper/
│   │   └── Mappers.kt                  ✅
│   └── repository/
│       ├── TransactionRepositoryImpl.kt ✅
│       ├── CategoryRepositoryImpl.kt    ✅
│       └── BudgetRepositoryImpl.kt      ⬜
│
└── ui/
    ├── theme/                          ✅  gerado pelo Android Studio
    ├── navigation/                     ⬜  NavHost + barra inferior
    ├── transactions/                   🟡  lista (falta apagar e editar)
    ├── addedit/                        ⬜  formulário de transação
    ├── categories/                     ⬜
    ├── dashboard/                      ⬜  saldo + gráfico circular
    ├── budget/                         ⬜
    ├── scanner/                        ⬜  câmara + QR
    ├── settings/                       ⬜
    └── components/                     ⬜  componentes reutilizáveis
```

---

## 5. Base de dados

### Diagrama de relações

```
categories 1 ───< transactions
categories 1 ───< budgets
categories 1 ───< recurring_transactions
```

### Tabelas atuais

#### `categories`

| Coluna | Tipo | Nota |
|---|---|---|
| `id` | Long, PK, autoGenerate | |
| `name` | String | |
| `icon` | String | nome do ícone |
| `colorHex` | Long | ex.: `0xFFE57373` |

#### `transactions`

| Coluna | Tipo | Nota |
|---|---|---|
| `id` | Long, PK, autoGenerate | |
| `amountCents` | Long | sempre positivo; o `type` decide o sinal |
| `type` | String | `INCOME` ou `EXPENSE` |
| `categoryId` | Long?, FK → `categories.id` | `SET_NULL` ao apagar a categoria |
| `dateEpochDay` | Long | tem índice |
| `note` | String | |
| `merchantNif` | String? | vem do QR; `null` em despesas manuais |

Índices: `categoryId`, `dateEpochDay`.

### Tabelas futuras

#### `budgets` (Fase 4)

| Coluna | Tipo | Nota |
|---|---|---|
| `id` | Long, PK | |
| `categoryId` | Long?, FK | `null` = orçamento geral do mês |
| `yearMonth` | String | ex.: `"2026-10"` |
| `limitCents` | Long | |

#### `recurring_transactions` (Fase 6)

| Coluna | Tipo | Nota |
|---|---|---|
| `id` | Long, PK | |
| `amountCents`, `type`, `categoryId`, `note` | | iguais às transações |
| `dayOfMonth` | Int | dia em que se repete |
| `active` | Boolean | |

### Conversões entre Entity e Domain

| Campo | Entity (Room) | Domain (app) |
|---|---|---|
| Data | `dateEpochDay: Long` | `date: LocalDate` |
| Tipo | `type: String` | `type: TransactionType` (enum) |

```kotlin
// ler:     LocalDate.ofEpochDay(dateEpochDay)   e   TransactionType.valueOf(type)
// guardar: date.toEpochDay()                    e   type.name
```

### Auto-categorização (sem tabela nova)

Ao ler um QR, procura-se a última transação com o mesmo NIF e sugere-se a mesma categoria:

```sql
SELECT categoryId FROM transactions
WHERE merchantNif = :nif
ORDER BY dateEpochDay DESC
LIMIT 1
```

### Alterar tabelas durante o desenvolvimento

Se mudares uma entity, a app crasha com *"Room cannot verify the data integrity"*. Solução: **desinstalar a app** do telemóvel e correr outra vez. Só depois de publicada é que são precisas migrações a sério. O schema é exportado para `app/schemas/` e deve ir para o Git.

---

## 6. Fases de desenvolvimento

| Fase | Objetivo | Peças novas | Estado |
|---|---|---|---|
| **0** | Base técnica | Hilt, Room, camadas, repositórios | ✅ |
| **1** | Transações completas | Formulário, apagar, navegação, formatação de € | ⬜ |
| **2** | Categorias | Ecrã de gestão | ⬜ |
| **3** | Dashboard | Use case, gráfico circular, seletor de mês | ⬜ |
| **4** | Orçamento e alertas | Tabela `budgets`, barra de progresso | ⬜ |
| **5** | Scanner QR | Parser, câmara, auto-categorização | ⬜ |
| **6** | Extras inteligentes | Previsão, recorrentes, insights, CSV | ⬜ |
| **7** | Segurança e polimento | Biometria, dark mode, animações | ⬜ |
| **8** | Qualidade e portfolio | Testes, CI, README, release | ⬜ |

### Fase 0: Base técnica ✅

- [x] Projeto criado (Empty Activity, Compose) e repositório no GitHub
- [x] Hilt configurado (`@HiltAndroidApp`, `@AndroidEntryPoint`)
- [x] Modelos do domain, entities, DAOs, `AppDatabase`, mappers
- [x] Repositórios (interface + Impl) e módulos Hilt
- [x] ViewModel e ecrã de teste a funcionar de ponta a ponta

---

### Fase 1: Transações completas (próxima)

- [ ] **Seed das categorias:** a tabela está vazia. Criar por omissão Alimentação, Transportes, Casa, Lazer, Saúde, Outros (`RoomDatabase.Callback.onCreate` no `DatabaseModule`)
- [ ] Dependência `navigation-compose` e `NavHost` com dois ecrãs (lista e formulário)
- [ ] **Formulário** em `ui/addedit/`: valor, tipo (receita/despesa), categoria, data, nota
- [ ] `AddEditViewModel` com validação (valor maior que 0) que chama `repo.add`
- [ ] **Lista melhorada:** valor formatado em € (`NumberFormat`), verde/vermelho por tipo, agrupada por dia
- [ ] **Apagar** (deslizar ou botão) e **editar** (tocar no item)
- [ ] Remover o botão de teste

**Pronto quando:** crias, editas e apagas transações, e a lista atualiza sozinha.

---

### Fase 2: Categorias

- [ ] Acrescentar `update` e `delete` ao `CategoryRepository`
- [ ] Ecrã com a lista de categorias
- [ ] Criar, editar e apagar categorias (escolha de cor e ícone)
- [ ] Usar as categorias reais no formulário de transação

**Pronto quando:** consegues gerir as categorias e usá-las nas transações.

---

### Fase 3: Dashboard

- [ ] `GetMonthlySummaryUseCase`: receitas, despesas, saldo e total por categoria
- [ ] Seletor de mês (anterior e seguinte)
- [ ] Cartões com saldo, receitas e despesas
- [ ] Gráfico circular com `Canvas` (só entram despesas)
- [ ] Barra de navegação inferior (Dashboard, Transações, Categorias)

É aqui que o `TransactionType` passa a ser usado na lógica:

```kotlin
val saldo = lista.sumOf {
    if (it.type == TransactionType.INCOME) it.amountCents else -it.amountCents
}
```

**Pronto quando:** o dashboard mostra os números e o gráfico certos para cada mês.

---

### Fase 4: Orçamento e alertas

Tabela nova, por isso repete-se a cadeia completa:

```
BudgetEntity → BudgetDao → AppDatabase → mapper → Budget (domain)
→ BudgetRepository + Impl → @Binds → ViewModel → Screen
```

- [ ] Definir limite mensal geral e por categoria
- [ ] Barra de progresso (gasto vs limite)
- [ ] Aviso aos 80% e aos 100%
- [ ] Notificação local (opcional)

**Pronto quando:** ao ultrapassar o limite, a app avisa.

---

### Fase 5: Scanner QR (o diferenciador)

1. [ ] **`InvoiceQrParser`** em `domain/qr` (Kotlin puro). O QR vem como `A:NIF*B:...*F:AAAAMMDD*...*O:total*...`, separado por `*`
2. [ ] **Testes unitários do parser** com QRs de exemplo
3. [ ] Câmara com CameraX e ML Kit (ou Google Code Scanner, mais simples)
4. [ ] Ecrã de confirmação: mostra os dados lidos, sugere categoria pelo NIF, o utilizador confirma e guarda
5. [ ] Auto-categorização pela última transação do mesmo NIF

Campos úteis do QR:

| Campo | Significado |
|---|---|
| `A` | NIF do emitente |
| `F` | Data (AAAAMMDD) |
| `O` | Total com IVA |
| `H` | ATCUD |

**Privacidade:** os dados ficam só no telemóvel.

**Pronto quando:** apontas a câmara a uma fatura real e a transação fica preenchida.

---

### Fase 6: Extras inteligentes

- [ ] Previsão de gastos até ao fim do mês (projeção linear pelo ritmo atual)
- [ ] Despesas recorrentes com WorkManager
- [ ] Insights ("+20% em restauração face ao mês passado")
- [ ] Exportar CSV

---

### Fase 7: Segurança e polimento

- [ ] Bloqueio com `BiometricPrompt` (com fallback para o PIN do telemóvel)
- [ ] Bloquear ao abrir e ao voltar do background após alguns segundos
- [ ] `FLAG_SECURE` para não aparecer em capturas de ecrã
- [ ] Ativar/desativar o bloqueio nas definições (DataStore)
- [ ] Dark mode, *empty states*, estados de erro e loading
- [ ] Animações subtis (gráfico, barra de orçamento)
- [ ] Strings em PT e EN
- [ ] Ícone adaptativo e splash screen
- [ ] Opcional: SQLCipher para encriptar a base de dados

---

### Fase 8: Qualidade e portfolio

- [ ] Testes unitários: parser do QR, use cases, ViewModels
- [ ] Testes dos DAOs (Room)
- [ ] 1 ou 2 testes de UI em Compose
- [ ] GitHub Actions: build e testes em cada push
- [ ] `ktlint` ou `detekt`
- [ ] R8/minify ativo no release
- [ ] README com descrição, screenshots, GIF de demo e diagrama da arquitetura
- [ ] APK assinado em GitHub Releases
- [ ] Licença MIT
- [ ] Secção "Decisões técnicas" no README

---

## 7. Como trabalhar cada funcionalidade

Faz **de cima para baixo**: começa pelo que o utilizador vê e só crias as camadas de baixo quando fizerem falta.

```
1. Desenha o Screen (com dados falsos, se for preciso)
2. Cria o ViewModel (estado + ações)
3. Faltam dados ou funções?  → acrescenta ao repositório (interface + Impl)
4. É uma tabela nova?        → entity, DAO, mapper, @Binds
5. Commit
```

### O que NÃO se repete

`AppDatabase`, Hilt, `MainActivity`, gradle. Para um repositório novo só se acrescenta uma linha ao `RepositoryModule`.

### O que se repete (só com tabela nova)

```
Entity → DAO → AppDatabase → mapper → modelo → interface → Impl → @Binds → ViewModel → Screen
```

Das funcionalidades planeadas, só o **orçamento** e as **recorrentes** precisam disto. As outras reutilizam o que já existe.

---

## 8. Convenções

- Dinheiro sempre em **cêntimos (`Long`)**, nunca `Double`
- Datas: `LocalDate` no domain, `Long` (epoch day) na base de dados
- Um ViewModel por ecrã
- O ecrã nunca importa nada de `data/`
- Funções `suspend` para escritas; `Flow` para leituras que devem atualizar sozinhas
- `@Insert`, `@Update`, `@Delete` em vez de `REPLACE` (o `REPLACE` apaga e reinsere, e dispara o `SET_NULL`)
- Commits pequenos e com prefixo:

| Prefixo | Quando |
|---|---|
| `feat:` | funcionalidade nova |
| `fix:` | correção de bug |
| `refactor:` | reorganizar sem mudar comportamento |
| `docs:` | documentação |
| `test:` | testes |
| `chore:` | configuração, dependências |

Exemplos: `feat: add transaction form`, `fix: register Application class in manifest`.

---

## 9. Decisões técnicas

| Decisão | Porquê |
|---|---|
| **Kotlin + Compose** | Stack atual do Android nativo |
| **Room** | Base de dados local oficial, valida as queries ao compilar e devolve `Flow` |
| **Entity separada do modelo** | Isola a forma da tabela do resto da app (tipos diferentes: `Long` vs `LocalDate`) |
| **Interface de repositório** | Permite trocar a implementação e testar sem Room |
| **Hilt** | Cria e liga os objetos sem factories escritas à mão |
| **`StateFlow` no ViewModel** | O ecrã tem sempre um valor e atualiza quando muda |
| **Cêntimos em `Long`** | Evita erros de arredondamento do `Double` |
| **Offline, sem login** | Os dados ficam no telemóvel; a proteção é um bloqueio local (biometria/PIN) |
| **KSP em vez de kapt** | Mais rápido e é o recomendado |

---

## 10. Glossário

| Termo | Significado |
|---|---|
| **Entity** | Classe que representa uma tabela do Room |
| **DAO** | *Data Access Object*: interface com as operações na tabela |
| **Repositório** | Intermédio entre o ViewModel e a fonte de dados |
| **Mapper** | Função que converte um objeto de um tipo noutro |
| **ViewModel** | Guarda o estado do ecrã e sobrevive a rotações |
| **Flow** | Fluxo de valores; volta a emitir quando a tabela muda |
| **StateFlow** | `Flow` com valor atual, ideal para o Compose observar |
| **`suspend`** | Função que pode pausar sem bloquear (equivalente a `async`) |
| **Coroutine** | Tarefa em segundo plano (`viewModelScope.launch`) |
| **Hilt** | Sistema de injeção de dependências |
| **`@Inject constructor`** | "Sabes criar esta classe; ela precisa disto" |
| **`@Provides`** | Receita para criar algo que o Hilt não consegue sozinho |
| **`@Binds`** | Liga uma interface à sua implementação |
| **`@Singleton`** | Só existe uma instância na app toda |
| **KSP** | Gerador de código usado pelo Room e pelo Hilt no build |
| **ATCUD** | Código único do documento nas faturas portuguesas |
