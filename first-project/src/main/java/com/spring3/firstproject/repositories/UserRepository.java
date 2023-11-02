package com.spring3.firstproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring3.firstproject.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // criando método customizável para o Repository
    // :userName trata-se do parâmetro de argumento do método
    // aqui estamos lidando com objetos e não com as tabelas, por isso cammelCase
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    User findByUserName(@Param("userName") String userName);
}
