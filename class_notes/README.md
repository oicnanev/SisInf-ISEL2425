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

---

## 03. Introduction to Transaction Processing - Conflicts and Anomalies - 24Fev2025 

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


--- 

## 04. Intro to Transaction Processing - Pessimistic / Optimistic Concurrency - 10 e 13 Março 2025

### Pessimistic Concurrency

#### Handling Isolation Levels

- As we have seen the standard demands 3 levels of isolation
- It's responsibility of the DMS to ensure their application
    * How do they achieve this? Concurrency control protocols!

#### Concurrency Control

- Pessimistic protocols:
    * Locking protocols / Two-Phase locking
- Optimistic protocols
    * Timestamp-based
    * Multiversion
    * Validation
- Recall the importance of **granularity**, a data item could be a record, index, table, etc

#### Locking

- A concept familiar to programming and operating systems:
    * We need to prevent access to data item! Lock it!
- Locks may be of different types and granulations

#### Locks

- Generally, all data items have a lock associated
- Locks, may be:
    * **Binary**: Simple lock, unlock
    	* **To restrictive**
    * **Shared / Exclusive (Read / Write)**: allows multiple read, but a single write
    	* 3 operations / states: read_lock(ed), write_lock(ed) and unlock(ed)
- Transactions waiting for a lock are kept in a queue
- Locks may also be converted 

**Binary Locks**

```plaintext
lock_item(X):
B: if LOCK(X) == 0				(item is unlocked)
		then LOCK(X) <- 1		(lock the item)
	else
		begin
		wait 					(until LOCK(X) = 0 and the lock
								manager wakes up the transaction)
		go to B
		end.
unlock_item(X):
	LOCK(X) <- 0				(unlock the item)
	if any transaction are waiting
		then wakeup one of the waiting transactions
```

**Read / Write Locks**

```plaintext
read_lock(X)
B:	if LOCK(X) == "unlocked"
		then begin LOCK(X) <- "read-locked"
			no_of_reads(X) <- 1
			end
	else if LOCK(X) == "read-locked"
		then no_of_reads(X) <- no_of_reads(X) + 1
	else begin
		wait (until LOCK(X) == "unlocked" 
			and the lock manager wakes up the transaction)
		go to B
		end

write_lock(X)
B: if LOCK(X) == "unlocked"
		then LOCK(X) <- "write-locked"
	else begin
		wait (until LOCK(X) == "unlocked"
			and the lock manager wakes up the transaction)
		go to B
		end

unlock(X)
	if LOCK(X) == "write-locked"
		then begin LOCK(X) <- "unlocked"
			wake up one of the waiting transactions if any
			end
	else if LOCK(X) == "read-locked"
		then begin
			no_of_reads <- no_of_reads -1
			if no_of_reads == 0
				then begin LOCK(X) <- "unlocked"
					wake up one of the waiting transactions if any
					end
			end
```


#### Two-Phase Locking

- We want to divide lock management into two-phases:
	- **Expanding**: all lock acquisitions and upgrades
	- **Shrinking**: all lock releases and downgrades
- If every transaction follows the two-phase locking (2PL) protocol, then it can be said that the schedule is serializable
- We get new issues however:
	- Deadlock
	- Starvation

|  T1  |  T2  |
| ---- | ---- |
| read_lock(Y) | read_lock(X) |
| read_item(Y) | read_item(X) |
| unlock(Y) | unlock(X) |
| write_lock(X) | write_lock(Y) |
| read_item(X) | read_item(Y) |
| X := X + Y | Y := X + Y |
| write_item(X) | write_item(Y) |
| unlock(X) | unlock(Y) |


|  T1  |  T2  |
| ---- | ---- |
| read_lock(Y) |  |
| read_item(Y) |  |
| unlock(Y) |  |
|  | read_lock(X) |
|  | read_item(X) |
|  | unlock(X) |
|  | write_lock(Y) |
|  | read_item(Y) |
|  | Y := X + Y |
|  | write_item(Y) |
|  | unlock(Y) |
| write_lock(X) |  |
| read_item(X) |  |
| X := X + Y ||
| write_item(X) |  |
| unlock(X) |  |

#### Deadlocks

- At least two transactions are "stuck" waiting for locks held by each other:

|  T1  |  T2  |
| ---- | ---- |
| read_lock(Y) |  |
| read_item(Y) |  |
|    | read_lock(X) |
|    | read_item(X) |
| write_lock(X) |   |
|    | write_lock(Y) |

**Deadlock Prevention Protocols**:

- Not used in practice, inefficient:
    * Total/global ordering of data
    * Conservative 2PL
- Give a timestamp (may be a global sequential ids) to each:
    * **Wait-die** (Old waits, young dies)
        + Older transactions may wait for younger
        + Younger transactions -> rollback and restart with the same timestramp
	- **Wound-wait** (Young waits, old kills young)
		- Younger transactions may wait for longer
		- If old would wait for young -> rollback young and restart with the same timestamp
- **Waiting protocols**:
    * **No-wait**: if any lock may not be obtained -> rollback the requesting transaction
    * **Caution-waiting**: if T2 tries to acquire a lock held by T1 check whether T1 is also waiting , if yes rollback T2
    * **Timeout**: if the wait exceeds a threshold -> rollback

**Deadlock detection**:

- Wait-for-Graph are again useful
- When should we check?
    * Stopping the world to check is expensive
    * Checking with every command is also expensive
- Results in **victim selection**:
    * Choosing which transaction should be rolledback

#### Starvation

- If a **waiting protocol** is not balanced and appropriately managed, a transaction may become indefinity stuck awaiting locks
- Both **Wait-die** and **Wound-wait** can prevent starvation:
    * But a younger transaction may be more important!
- We must have fair waiting scheme:
	- Always serve transactions in "order of arrival", or
	- Have priorities but increase the priority of long waiting transactions, and
	- Increase the priority of previously rolled back transactions
	 		 

#### Two-phase locking

- So far we have talked about basic 2PL
- We also have:
    * **Conservative 2PL**: A transaction must declare all locks and acquire them, or it gets none (prevent deadlocks)
    * **Strict 2PL**: all write locks are kept until commit/abort
    * **Rigorous 2PL**: all locks are kept


### Optimistic Concurrency

#### But what is Pessimistic / Optimistic?

- Multiple views:
    * 1 (IBM view):
        + **Pessimistic** -> with locks
        + **Optimistic** -> no locks
	* 2 (that we mainly follow):
		- **Pessimistic** -> interference (wait) during transaction
		- **Optimistic** -> no interference (wait) until commit
- There are hybrid solutions and implementations that do not fit either:
    * PostgreSQl (in practice) allows transactions to keep going even if it knows a rollback will be done
    * IBM uses it's own definition of optimistic (view multiple views 1)
    * Elmasri often does not classify as optimistic/pessimistic

#### Timestamping protocols

- Strict checking and control of locks is inefficicent, particularly **if conflits are not expected to happen**
- If concurrent access to the same data item is uncommon, approaches like timestamping may achieve serializability with lower overhead
- Recall, we have already talked about timestamps to handle deadlocks and starvation
    * So are they still present? It depends...

#### Timestamps

- Each transaction must have a **unique** mark that provides order
    * For a transaction **T** this is **TS(T)**
    * Note: this may be the transaction id
- Actual clocks are't reliable in distributed systems (nor unique)!
	- So, a global counter may be used and reset periodically
	- This is the basic idea behind Lamport clock
- For non-distributed systems using just an actual timestamp may be enough, if no timestamp can be repeated
	- Same theory used to generate some unique IDs (UUID v7 - part timetamp part random)

**Timestamp Ordering Algotithm**

- Conflicting operations are ordered by the **TS(T)** of each transaction, this ensures the same order as one serial schedule
	- The schedule is conflict equivalent and therefore serializable!
- For unique **TS(T)** values the serial schedule has been chosen
- We have:
	- **read TS** -> **most recent** transaction that read a data item
	- **write TS** -> **most recent** transaction that wrote a data item 

**Basic TOA**:

- A transaction **T** can **only** change items with timestamps **older** than itself
    * Older means: write_TS > TS(T) and read_TS > TS(T)
    * If the check fails -> rollback T
        + All transactions that depend on T must also be rolled back (and so on... we have a cascading rollback)
- A transaction **T** can **only** read items **older** than itself
	- Older means: write_TS > TS(T)
	- If check fails -> same as above
- Repeat rolled back transactions with a **new** timestamp
- Update write_TS and read_TS (or keep read_TS!) as needed
- Basic TOA implies conflict serializability with no deadlocks, but with starvation and cascading rollbacks

| T1 -> TS(T1) = 1 | T2 -> TS(T2) = 2 |
| ---------------- | ---------------- |
| read(Y) |    |
|    | read(Y) |
|    | write(Y) |
| read(X) |     |
|    | read(X) |
|    | write(X) |

| Item | read_TS | write_TS |
| ---- | ------- | -------- |
| X | 2 | 2 |
| Y | 2 | 2 |

**Strict TOA**

- Any transaction that reads an item older than itself must wait until the transaction responsible for changing **write_TS** ends
- Bring back "locking" but not deadlocks
- Implies a strict and serializable schedule

**Thnoma's Write Rule**

- Changes the scheme of basic TOA by allowing "blind" writes
- If a more recent write exists ignore "our" write
- Not conflict serializable but has less write induced rollbacks

#### Multiversion Protocols

- Another approach
    * Keep multiple versions of data items and serve them
    * Each write operation creates a new version
    * Transactions may read older versions
- Causes less rollbacks at the cost of higher storage!
- Two examples:
    * Timestamped-based
    * 2PL-based

**Timestamped-based example**

- Each version has a read_TS and a write_TS
- For a transaction T:
    * write_item finds the most recent (by write_TS) version:
        + If read_TS > TS(T) or write_TS > TS(T) -> rollback
        + Otherwise create a new version with read_TS = TS(T) and write_TS = TS(T)
    + read_item finds a version such that it has max(write_TS) among the set of versions that satisfy write_TS <= TS(T):
    	+ Then, on the most recent version, update read_TS to max_(TS(T), current_read_TS)

**2PL-based (locking) example**

- Extends 2PL with a **certify lock** mode
- Two versions may be held for each item: a committed and an **optional** uncommitted version:
    * Other transactions read the uncommited version while a transaction T hnolds the write lock
    * T then upgrades all lock to **certify** ion  order to commit
- Allows read to proceed concurrent to writing operations, but may result in high waiting times to commit!
     
#### Optimistic Concurrency (Validation / certification)

- Drop **any** pre-checking to reduce overhead
- Operations are done against copies of the data items, before commit it is validated if the operations would violate serializability
    * if not -> apply the local state to the database
    * if yes -> rollback and try again
- Only efficient if there is little conflict among transactions in practice (an optimistic view)
- Has 3 phases:
    * **Read phase**: reads from commited data, but writes to local copies
    * **Validation phase**: checks for serializability and conflicts with committed and validating transactions
    * **Write phase**: applies updates or discards local state
- To aid validation write and read sets are kept associated with timestamps
- For T2 not to interfere with T1 on of these conditions must hold true: 
    * Transaction T1 completes its *write phase* before T2 starts its *read phase*
    * T2 starts its *write phase* **after** T1 completes its **write phase**, and the *read_sets* have no items in common
    * Both the *read_sets* and *write_sets* have no items in common and T1 completes its *read phase* **before** T2 completes its *read_phase*

#### Savepoints / Partial Rollbacks

- A savepoint is a "moment" where the state of a transaction was saved and may be  rolled back to, without rolling the entire transaction
- So, we have *subtransactions*

```sql
BEGIN;
	INSERT INTO table1 VALUES (1);
	SAVEPOINT my_savepoint;
	INSERT INTO table1 VALUES (2);
	SAVEPOINT my_savepoint;
	INSERT INTO table1 VALUES (3);

	-- rollback to the second savepoint
	ROLLBACK TO SAVEPOINT my_savepoint;
	SELECT * FROM table;

	-- release the second savepoint
	RELEASE SAVEPOINT my_savepoint;

	-- rollback to the first savbepoint
	ROLLBACK TO SAVEPOINT my_savepoint;
	SELECT * FROM table1;
COMMIT;
``` 

- Upon failure we may now go back to the savepoints!

![Savepoints](./img/17.png)

#### Delayed Integrity Checks

- The **SET CONSTRAINTS** command allow to control when integrity checks are done:	
    * **IMMEDIATE**: as usual, checked for each operation
    * **DEFERED**: wait until commit
- Useful when operations may break constraints momentarily (alongside savepoints)

---