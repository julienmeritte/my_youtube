package com.etna.myapi.repositories;

import com.etna.myapi.entity.Ecomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Rcomment extends JpaRepository<Ecomment, Long> {

    @Query( value = "SELECT * FROM comment WHERE id_video = ?1", nativeQuery = true)
    List<Ecomment> findAllByVideoId(Long idVideo);
}
