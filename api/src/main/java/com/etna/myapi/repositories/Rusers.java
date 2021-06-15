package com.etna.myapi.repositories;

import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Rusers extends JpaRepository<Eusers, Long> {

    Eusers findByUsername(String username);

    Eusers findByIdUser(Long idUser);

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE pseudo = ?1", nativeQuery = true)
    List<Eusers> findAllByPseudo(String pseudo);

}
