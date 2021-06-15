package com.etna.myapi.repositories;

import com.etna.myapi.entity.Evideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Rvideo extends JpaRepository<Evideo, Long> {

    @Query( value = "SELECT * FROM video WHERE id_user = ?1", nativeQuery = true)
    List<Evideo> findAllByUserId(Long idUser);
}
