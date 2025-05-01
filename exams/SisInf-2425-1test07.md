# SisInf 2425 1º test

## Questão 1
A letra "I" no acrônimo ACID significa **Isolamento** (Isolation). Esta propriedade garante que transações concorrentes sejam executadas como se fossem sequenciais, sem interferência entre elas.

O protocolo Two-Phase Locking (2PL) relaciona-se com o isolamento através de suas duas fases:
1. **Fase de expansão**: Transações apenas adquirem bloqueios, mas não liberam nenhum.
2. **Fase de contração**: Transações apenas liberam bloqueios, mas não adquirem novos.

Este protocolo garante serialização de conflitos, assegurando o isolamento das transações.

## Questão 2
Analisando as afirmações sobre propriedades de escalonamentos cascadeless e strict:

(a) **False** - Nem todos escalonamentos serializáveis são cascadeless ou strict.

(b) **True** - Ambas propriedades não permitem que uma transação escreva em um item de dados que foi escrito por outra transação ainda não concluída.

(c) **False** - Ambas permitem conflitos Write/Write desde que a primeira transação já tenha sido commitada.

(d) **True** - Ambas protegem contra leituras sujas (dirty reads).

## Questão 3

### (a) Anomalia de registros fantasmas
A anomalia de registros fantasmas ocorre quando uma transação lê um conjunto de registros que satisfaz uma condição, e outra transação concorrente insere novos registros que também satisfariam essa condição. Se a primeira transação refizer a leitura, verá registros "fantasmas" que não estavam presentes inicialmente.

**Exemplo**:
```
T1: SELECT * FROM conta WHERE saldo > 1000; (retorna 2 registros)
T2: INSERT INTO conta VALUES (3333, 1500); COMMIT;
T1: SELECT * FROM conta WHERE saldo > 1000; (agora retorna 3 registros)
```

### (b) Condições para ocorrência com 2PL
O protocolo 2PL padrão não previne a anomalia de registros fantasmas porque:
- **INSERT**: Pode inserir novos registros que satisfaçam condições de seleção de outras transações
- **UPDATE**: Pode modificar registros para que passem a satisfazer condições de seleção de outras transações

Para prevenir, é necessário usar bloqueios de predicado ou níveis de isolamento mais altos como Serializable.

## Questão 4

### (a) Número e tipo de bloqueios após cada bloco

1. **BL-1A**: 0 bloqueios (apenas BEGIN TRANSACTION)
2. **BL-2A**: 0 bloqueios (apenas BEGIN TRANSACTION)
3. **BL-1B**: 1 bloqueio compartilhado (S) na conta com id=1111 (do SELECT)
4. **BL-2B**: 1 bloqueio exclusivo (X) na conta com id=2222 (do UPDATE)
5. **BL-1C**: 2 bloqueios compartilhados (S) - mantém o de 1111 e adquire novo em 2222

### (b) Possíveis anomalias

**Para T1**:
- **Leitura não repetível**: Se T2 modificar a conta 1111 entre os dois SELECTs de T1
- **Leitura suja**: Se T1 ler dados escritos por T2 antes do COMMIT de T2

**Para T2**:
- **Escrita perdida**: Se outra transação modificar a conta 2222 antes de T2 COMMIT
- **Bloqueio por tempo indeterminado**: Se T1 mantiver bloqueio S em 2222 impedindo T2 de adquirir X

## Questão 5: Função plpgsql

```sql
CREATE FUNCTION get_accounts(v money)
RETURNS SETOF account AS $$
DECLARE
    acc_record account%ROWTYPE;
BEGIN
    FOR acc_record IN SELECT * FROM account WHERE balance > v LOOP
        RETURN NEXT acc_record;
    END LOOP;
    RETURN;
END;
$$ LANGUAGE plpgsql;
```

## Questão 6: Stored Procedure

```sql
CREATE OR REPLACE PROCEDURE accounts_balance(min_balance money)
LANGUAGE plpgsql
AS $$
DECLARE
    acc_record account%ROWTYPE;
BEGIN
    FOR acc_record IN SELECT * FROM get_accounts_balance(min_balance) LOOP
        RAISE NOTICE 'Descrição: %, Saldo: %', acc_record.description, acc_record.balance;
    END LOOP;
END;
$$;
```

Observação: Corrigi o nome da função na questão 6 para `get_accounts_balance` conforme mencionado no enunciado, assumindo que é diferente da função `get_accounts` da questão 5.