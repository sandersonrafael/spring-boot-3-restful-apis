package com.spring3.firstproject.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring3.firstproject.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // criando operação personalizada -> trata-se do objeto Person e não da tabela person
    // esta operação para o verbo PATCH serve para desabilitar a person (enabled = false)

    /* ACID */
    // Atomicidade (Atomicity) -> uma transação é executada completamente ou não é executada de forma alguma. Se uma parte de uma transação falhar, a transação inteira é revertida para o estado anterior
    // Consistência (Consistency) -> a transação deve respeitar todas as restrições de integridade, como chaves primárias, chaves estrangeiras e outras regras definidas no esquema do banco de dados
    // Isolamento (Isolation) -> garante que as transações em execução simultaneamente não interfiram umas com as outras. Isso significa que uma transação em execução não deve ser visível para outras transações até que ela seja concluída (ou "commit")
    // Durabilidade (Durability) -> A durabilidade garante que, uma vez que uma transação tenha sido confirmada (commit), suas alterações persistirão mesmo em caso de falha do sistema, como falhas de hardware ou energia. Informações armazenadas permanentemente e não podem ser perdidas

    // deve ser usado para sinalizar que trata-se de uma operação personalizada, não do próprio repositório
    // e que essa operação realiza modificações diretas na base de dados, devendo-se ter atenção ao ACID das transações
    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);

    // Não é necessário usar o Modifying porque faz apenas consultas, sem alterações
    // Busca por nome com código SQL
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPersonsByName(@Param("firstName") String firstName, Pageable pageable);
}
