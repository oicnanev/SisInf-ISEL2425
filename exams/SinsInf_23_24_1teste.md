# Resolução do 1º Teste de Sistemas de Informação

## 4.1 Grafo de dependências e serializabilidade

**Grafo de dependências (wait-for):**
- T3 lê A antes de T1 escrever A → T3 → T1
- T3 lê B antes de T2 escrever B → T3 → T2
- T2 lê B antes de T3 escrever B → T2 → T3

**Análise:** Há um ciclo T2 → T3 → T2 no grafo de dependências. Portanto, o escalonamento **não é serializável**.

## 4.2 Possibilidade de rollback encadeado

Sim, existe possibilidade de rollback encadeado (non-cascadeless) porque:
- T3 leu A e B antes de T1 e T2 modificarem esses itens respectivamente
- Se T1 ou T2 fizerem rollback, T3 teria que ser desfeita também (pois leu dados não confirmados)
- Este cenário caracteriza um escalonamento não cascadeless (permite leituras sujas)

## 3. Anomalias em transações

(a) **False** - Dirty read ocorre quando se lê dados não confirmados, o que não acontece aqui (T2 já fez commit)
(b) **False** - Existe uma anomalia de non-repeatable read
(c) **False** - Lost update ocorreria se T2 sobrescrevesse uma modificação de T1, o que não é o caso
(d) **True** - T1 lê A duas vezes com valores diferentes (non-repeatable read)

## 4. Transações T1 e T2

i) **Valor em T1.13:** 1000 (lê saldo da conta 1111)
ii) **Valor em T1.14:** Pode ser 2000 ou 1500, dependendo do timing:
   - Se T2 já tiver feito commit: 1500
   - Se T2 ainda não tiver feito commit: 2000
iii) **Efeito de T1.12:** Define o nível de isolamento como READ UNCOMMITTED, permitindo:
   - Ler dados não confirmados (dirty reads)
   - Não garante consistência durante a transação

## 5. Cursores em PL/pgSQL

(a) **True** - Cursors devem ser abertos explicitamente com OPEN
(b) **True** - São usados para processar resultados linha por linha
(c) **True** - São variáveis que permitem acesso programático a conjuntos de resultados
(d) **False** - Cursores são apenas para leitura, não podem deletar tuplos diretamente

## 6. Correção da função add

**Erros:**
1. `IMMUTABLE` é incompatível com a sintaxe usada (deveria ser `RETURNS varchar` na declaração)
2. Falta a palavra-chave `AS` antes do bloco de código

**Correção:**
```sql
CREATE OR REPLACE FUNCTION add(a integer, b integer DEFAULT 0)
RETURNS varchar
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN (a + b)::varchar;
END;
$$;
```

## 7. Execução da função add

1. `select add(1,1);`
   - Sucesso: Retorna "2"

2. `select add(null,null);`
   - Falha: NULL + NULL resulta em NULL, que não pode ser convertido para varchar
   - Correção: `COALESCE(a,0) + COALESCE(b,0)`

3. `select add(b:=1,a:=3);`
   - Sucesso: Retorna "4" (3+1)

4. `select add(b:=1);`
   - Falha: Parâmetro 'a' não tem valor padrão na chamada
   - Correção: `select add(0,1);` ou definir DEFAULT para 'a'

5. `select add(a:=1);`
   - Sucesso: Retorna "1" (1+0, usando default para b)

6. `select add(a:=1)*3;`
   - Falha: Não é possível multiplicar strings
   - Correção: `select add(a:=1)::integer * 3;` ou modificar a função para retornar integer
