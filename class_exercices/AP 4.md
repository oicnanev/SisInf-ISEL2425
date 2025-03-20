# AP 4

### **1. Conceitos Fundamentais**

#### **1.1. Explicar o MVCC (Multi-Version Concurrency Control) do PostgreSQL**
O **MVCC** (Multi-Version Concurrency Control) é um mecanismo de controle de concorrência que permite que várias transações acessem o banco de dados simultaneamente sem bloquear outras transações. Em vez de bloquear linhas ou tabelas, o PostgreSQL cria versões diferentes dos dados para cada transação. Isso permite que leituras e escritas ocorram simultaneamente, sem conflitos.

- **Como funciona**: Cada transação vê uma "imagem" consistente dos dados no momento em que a transação começou. Quando uma transação atualiza uma linha, uma nova versão da linha é criada, e a versão antiga é mantida até que não seja mais necessária (por exemplo, quando todas as transações que poderiam ver a versão antiga são concluídas).
- **Vantagens**: Reduz a contenção entre transações, permitindo alta concorrência e melhor desempenho em ambientes com muitas leituras e escritas simultâneas.

#### **1.2. Como o MVCC suporta os níveis de isolamento do padrão SQL?**
O PostgreSQL suporta os níveis de isolamento definidos pelo padrão SQL: **READ UNCOMMITTED**, **READ COMMITTED**, **REPEATABLE READ**, e **SERIALIZABLE**. O MVCC permite que o PostgreSQL implemente esses níveis de isolamento de forma eficiente:

- **READ UNCOMMITTED**: No PostgreSQL, este nível é tratado como **READ COMMITTED**, pois o MVCC não permite leitura de dados não confirmados.
- **READ COMMITTED**: Cada consulta vê apenas os dados confirmados no momento em que a consulta é executada.
- **REPEATABLE READ**: Garante que, se uma transação ler os mesmos dados várias vezes, verá o mesmo valor, mesmo que outras transações tenham alterado os dados.
- **SERIALIZABLE**: O nível mais estrito, que garante que as transações sejam executadas como se fossem serializadas (uma após a outra), evitando fenômenos como leituras fantasmas.

#### **1.3. Introduzir diferentes níveis de bloqueio (FOR UPDATE, FOR NO KEY UPDATE, FOR SHARE, FOR NO KEY SHARE)**
O PostgreSQL oferece diferentes níveis de bloqueio para controlar o acesso concorrente aos dados:

- **FOR UPDATE**: Bloqueia as linhas selecionadas para atualização, impedindo que outras transações as modifiquem ou bloqueiem para atualização.
- **FOR NO KEY UPDATE**: Similar ao **FOR UPDATE**, mas não bloqueia chaves estrangeiras que referenciam as linhas bloqueadas.
- **FOR SHARE**: Bloqueia as linhas selecionadas para leitura, impedindo que outras transações as atualizem ou excluam, mas permitindo leituras concorrentes.
- **FOR NO KEY SHARE**: Similar ao **FOR SHARE**, mas não bloqueia chaves estrangeiras.

**Exemplos de cenários**:
- **FOR UPDATE**: Útil quando você precisa garantir que uma linha não seja alterada por outra transação enquanto você a atualiza (por exemplo, em uma transferência bancária).
- **FOR SHARE**: Útil quando você precisa garantir que uma linha não seja alterada, mas outras transações ainda podem lê-la (por exemplo, ao verificar o saldo de uma conta).

#### **1.4. Diferenças entre o PostgreSQL e o padrão SQL no controle de concorrência**
O PostgreSQL segue o padrão SQL, mas com algumas diferenças:
- **READ UNCOMMITTED**: No PostgreSQL, este nível é tratado como **READ COMMITTED**, pois o MVCC não permite leitura de dados não confirmados.
- **REPEATABLE READ**: No PostgreSQL, este nível evita leituras fantasmas, o que não é garantido pelo padrão SQL.
- **SERIALIZABLE**: O PostgreSQL usa um mecanismo de detecção de conflitos para garantir a serialização, enquanto o padrão SQL não especifica como isso deve ser implementado.

---

### **2. Exploração Livre**

Nesta seção, você deve explorar os exemplos fornecidos no arquivo ZIP e responder às perguntas levantadas. Aqui estão algumas orientações gerais:

- **Siga as instruções**: Cada pasta contém exemplos práticos. Execute os comandos SQL e observe o comportamento do PostgreSQL.
- **Formule suas ideias**: Tente entender por que certos comportamentos ocorrem e como o MVCC e os bloqueios afetam o resultado das transações.
- **Responda às perguntas**: Alguns arquivos contêm perguntas específicas. Use o conhecimento adquirido para responder a elas.

---

### **3. Reflexão e Conclusão**

#### **3.1. Por que o MVCC do PostgreSQL é diferente do padrão SQL?**
O MVCC do PostgreSQL é diferente porque ele usa um mecanismo de versionamento de dados para permitir alta concorrência, enquanto o padrão SQL não especifica como o controle de concorrência deve ser implementado. O PostgreSQL prioriza a eficiência e a escalabilidade, especialmente em ambientes com muitas transações simultâneas.

#### **3.2. Como o controle de transações do PostgreSQL afeta aplicações do mundo real?**
O controle de transações do PostgreSQL permite que aplicações do mundo real lidem com alta concorrência e consistência dos dados. Por exemplo, em um sistema bancário, o MVCC e os bloqueios garantem que transferências simultâneas não causem inconsistências, como saldos negativos ou perda de dados.

#### **3.3. Cenário de transferência bancária**

1. **Como garantir que duas transferências concorrentes não ultrapassem o saldo da mesma conta?**
   - Use **FOR UPDATE** para bloquear o saldo da conta do remetente durante a transferência. Isso garante que apenas uma transação possa alterar o saldo de cada vez, evitando overdrafts.

2. **FOR UPDATE ou FOR NO KEY UPDATE seria mais apropriado para bloquear o saldo do remetente?**
   - **FOR UPDATE** é mais apropriado, pois você deseja garantir que o saldo da conta não seja alterado por outras transações durante a transferência.

3. **Quais deadlocks potenciais podem surgir se dois usuários transferirem dinheiro entre si ao mesmo tempo?**
   - Se duas transações bloquearem as contas em ordens diferentes (por exemplo, Transação 1 bloqueia Conta A e depois tenta bloquear Conta B, enquanto Transação 2 bloqueia Conta B e depois tenta bloquear Conta A), um deadlock pode ocorrer. Para evitar isso, sempre bloqueie as contas na mesma ordem.

