package com.etna.myapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "format")
public class Eformat {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 120, nullable = false)
    private String source;

    @Column(length = 45, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="idVideo", nullable=false)
    private Evideo video;

    public Eformat() {
    }
}
