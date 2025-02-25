# Class Notes

## 01. Presentation and Sylabus - 17FEV2025

### Goal

- SQL language at an advanced level including SQL/PSM (stored procedures, triggers and functions)
- Base de Dados ativas
- Understand indexing mechanisms available in SGBD and design optimization strategies
- Understand and use transactional mechanisms with ACID properties realizing how they can be used to deal with cuncurrency
- Build data access layers, guaranteeing transactional control. JPA
- Develop critical thinking
- Write technical reports with discussion of different solutions, comparative analysis and critical sense

### Programme (summary)

- **M1: Transactional Processing**
    * 1. Transactional processing concepts
    * 2. Support for transactional processing in DBMSs
- **M2: Dynamic databases**
    * 3. SQL extension for programming (SQL/PSM)
    * 4. Stored procedures, functions and triggers
- **M3: Indexing and Implementation Plans**
    * 5. Know the different types of indexing structures
    * 6. Performance reports on sql commands and execution plans
- **M4: API for Data Access**
    * 7. JPA - Jakarta Persistence
    * 8. Building data access layers

### Assessment Rules

Theoretical component (CT), individual and face-to-face

- 1st test during the semester and 2nd test in the 1st
season (CT1), or
- exam (CT2);
- CT = max(CT1, CT2), Minimum grade: 10 marks.

Practical component (CP)

-  5 individual laboratory assignments carried out in class
(CR) and;
-  one project (demonstrated in class by the whole group)
and its report (CP).

**Final Grade (CF)** = 0.6 CT + 0.25 CR + 0.15 CP

Hits: 

- The student must obtain a mark of more than 8.00 and
an average of more than 9.50 in 3 of the 5 laboratory
assignments in order to get a grade;
- CT and CP are pedagogically fundamental;
- CP can be graded differently for each student;
- There is no retaking of a test during the appeal period;
- Late submissions contribute 0 (zero) to this submission;
- Groups of 3 students, with elements only from one class.

> The project delivery must include:
> - a **report in PDF**, and
> - project code (independent of the development environment).

You can download [here](https://github.com/matpato/reportisel) the Template for writing the report.
They must follow one of the versions proposed. It must not be
modified and may be penalised.

### Aditional Notes

1. The existence of situations of (self-)plagiarism in practical
work will lead to the cancellation of all work involved and
immediate fail the course unit;
2. Only work whose authors coincide with the constitution
of the groups on Moodle will be accepted;
3. Any withdrawals must be communicated to the class
teacher;
4. Submissions of work without a report will not be
considered and will be given a grade of 0 (zero);
5. The quality of the report influences the grade of the
assignment (approximately 22.5%);
6. The use of artificial intelligence tools is permitted,
provided it is indicated and used critically.

### Work environment

- Option 1: docker for postgreSQl 15.x + Java 21, any OS
- Option 2: Any OS with installed PostgreSQl 15.x + Java 21

> Only restriction: All code delivered must be executed independently of the development environment, in **command line**.
> Students **must provide the execution instructions**, assuming the existence of the JVM 21 as the only prerequisite.

### Main Bibliography

- **Fundamentals of Database System** (7th Ed) - R. Elmasri, Shamkant B. Navathe - Pearson Education (2016) - ISBN: 0-13-397077-9
- **Patterns of Enterprise Application Architecture** (5th Ed) - Martin Fowler - Addison-Wesley (2003) - ISBN: 0-321-12742-0
- **Dive Into Design Patterns** (1st Ed) - Alexander Shvets (2003) - [online](https://refactoring.guru/design-patterns)

## 02. M1 - Transactional Processing - Database Transactional Processing Concepts and Theory - 20FEV2025 e 24FEV2025

### Concurrency

- Most DBMSs are multi-user systems
- Multiple operations may act upon the same data simultaneously, this is know as multiprogramming

### Transactions

- A logical unit of database operation (eg. INSERT, DELETE, UPDATE, etc) with the goal of ensuring:
    * **atomacity** (estamos a trabalhar com uma linha ou uma tabela inteira? Tem impacto nas optimizações que vamos fazer)
    * **consistency**

- May be:
    * **read-only transactions**: does not alter any data
    * **read-write transactions**: at least one operation that alters at least one data item

- **Data granularity** - referes to the size of a data item
- Every transaction should have a unique ID 

### Transactions ACID

- **Atomacity**: all operations are applied or none is
- **Conscistency**: must take a database from one consistent state to another
- **Isolation**: should not interfere with other operations
- **Durability**: "confirmed" changes may not be lost

### Transactions - Read / Write

Without loss of generalization:

- **Read** -> read_item(X);
- **Write** -> write_item(X); (this can be a delete!)

![Lost update](./img/01.png)

- a) lost update problem
- b) temporary update problem
- c) incorrect summary problem


### Scheduling

- Interleaved execution of operations
- Gives us the "illusion" of concurrency
- Notation:
    * start_transaction -> b
    * write_item -> w(X)
    * read_item -> r(X)
    * commit -> c
    * abort / rollback -> a
    * end -> e

### Scheduling - Serial Execution

- Forget interleaving, simply execute then commit/abort eacgh transaction. But, it is slow...

![Serial schedule](./img/02.png)

### Scheduling - Conflicts

- When there are conflicts?
    * Two operations belongs to different transactions
    * Access the same data item
    * At least one is write

- **Read-Write Conflict**: a value is read then changed before use
- **Write-Write Conflict**: a write operation is overwritten by another 

![Conflicts](./img/03.png)

- **Sa: r1(X); r2(X); w1(X); r1(Y); w2(X); w1(Y);**
	- 1st conflict - w1(X); w2(X); - write-write
	- 2nd conflict - r1(X); w2(X); - read-write
	- 3rd conflict - r2(X); w1(X); - read-write

### Scheduling - Recovery

- Conflicts are **recoverable** if a transaction T never has to be rolled back after commit, otherwise it is **non-recoverable**
    * so, it can't depend on another uncommitted transaction 
 
![Recovery](./img/04.png)

Nenhuma transacção deve depender de algo que ainda não foi committed. Só se podem ler valores que já foram committed.

### Scheduling - Cascadeless

- **Cascading rollback**: when uncommitted transactions must be rolled back due to another transaction. **This is stil recoverable**

![Cascadeless](./img/05.png)

- If there are no reads come from uncommitted transactions it is **cascadeless**

### Scheduling - Strict

- **Strict schedules**: forbids read/write of an item until the last transaction that wrote to it is committed

![Strict](./img/06.png)

### Transaction States

![State Transaction Diagram](./img/07.png)

### Log File

- Registry of (almost) all operations:
    * [start_transaction, T]
    * [write_item T, X, old_value, new_value]
    * [read_item, T, X]
    * [commit, T]
    * [abort, T]

- Sequential, append-only (used in on many distributed system databases - ex. Apache Kafka)
- Este ficheiro não é absoluto, por vezes apaga os logs antigos

### Scheduling - Correctness

- A schedule is **correct** if it preserves, the **consistency** and **isolation** of trasactions:
    * Then, **all serial schedules are correct** (but slow...)
- We want a nonsreial scehdule that is equivalent to a serlai schedule:
    * A **serailizable schedule**
- If serial schedules are correct, then a seiralizable schedule must also be correct

### Scheduling - Serial Equivalent

- Two schedules are equivalent when:
    * 1. **Result equivalence**: if they produce the same results. (**Problematic**: schedules may only be equivalent for specific values and operations....)
    * 2. **Conflict equivalence**: if the order of conflicting operations is the same
    * 3. **View equivalence**: if each read sees the same write result (view)
- There are even others, but we focus on conflict equivalence

### Scheduling - Conflict Equivalent

- Recall, operations conflicts if: (1) belong to different transactions, (2) access the same item, and (3) at least one is write
- The order matters and can lead to different results!
- We preserve the order of conflicting operations but reorder non conflicting ones
    * The resulting schedule is **conflict serializable**

![Conflict serializable - equivalent](./img/08.png)

![Conflict serializable - non-equivalent](./img/09.png)

### Scheduling - Wait-for-Graphs

- Also know as **precedence graphs** or **serialization graphs**
    * A **direct graph**: the flow between nods have a direction
    * It is **acyclic** if there are no loops/cycles
- If the order of conflicting operation matters, let's identify how they are chained!
- If we can build a **Direct Acyclic graph** then the schedule is serializable

![Serializable testing](./img/10.png)

![Wait-for-Graphs](./img/11.png)

### Conflict Types and the Anomalies they lead to

- **Write-Write (WW) conflict**
    * Overwriting uncommitted data
- **Read-Write (RW) conflict**
    * Dirty read
    * Incorrect summary
    * Nonrepeatable read
    * Lost update
    * Phantom read
### WW - Overwriting Uncommitted Data

- Essentially there is only one type of write/write conflict
- Similar to Lost Update issue
    * But we don't require that there is a previous read (**blind writes**)

![WW conflict](./img/12.png)

### RW - Lost Update

- The value that T2 wrotes comes from a previous read, that is no longer up to date
- **Blind writes don't belong to this anomaly type**

![RW - Lost Update](./img/13.png)

### RW - Dirty Read (Temporary Update)

- A transaction fails after performing a write
- All others that read that data item will have an inconsistent view

![RW - Dirty Read](./img/14.png)

### RW - Incorrect Summary

- Like a **dirty read** (happens even if T1 doesn't fail)
- An aggregation is interleaved with other operations
- Sometimes this not an issue
    * Eg. dashboard metrics may have soft consistency guarantees

![RW - Incorrect Summary](./img/15.png)

### RW - Nonrepeatable/Unrepeatable Read

- Both reads will have distinct values, which ones was right?

![RW - Nonrepeatable/Unrepeatable Read](./img/16.png)

### RW - Phantom Read/Insert

- A data item that was read can no longer be found, it's a **phanton record**
    * This term may also be used to refer to phantom inserts
- This may happen during searches:
    * Select all records that...
    * Followed by editing some of them

### In practice

- Achieving serializability is an hard problem in practice:
    * So we use protocols to achieve the desired effects
- If we know some transactions don't require full isolation, we may "break" isolation increase performance
    * we may also control the **access mode**
        + **Read-only** vs **Read-Write**

### Transaction in SQL

- START TRANSACTION
- SET TRANSACTION
- SET CONSTRAINTS
- SAVEPOINT (mini transactions)
- RELEASE SAVEPOINT
- COMMIT
- ROLLBACK

### Isolation Levels

- Serializable here is not the same as we seen so far!
- Specific to ensuring protection to these three types of conflicts only!

**Isolation Level - Types of Violation**

| Isolation Level | Dirty Read | Nonrepeatable Read | Phantom |
| --------------- | ---------- | ------------------ | ------- |
|READ UNCOMMITTED | Yes        | Yes                | Yes     |
| READ COMMITTED  | No | Yes | Yes |
| REPEATABLE READ | No | No | Yes |
| SERIALIZABLE    | No | No | No |

- **PostgreSQL** segue o READ COMMITED e não o SERIALIZABLE do ISO SQL99
- Em PostgreSQL, basta usar REPEATABLE READ para não termos Phantom
- PostgreSQL também pode usar SERIALIZABLE, mas faz outras funções








