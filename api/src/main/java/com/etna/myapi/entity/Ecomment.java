package com.etna.myapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Ecomment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 45, nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private Eusers user;

    @ManyToOne
    @JoinColumn(name="idVideo", nullable=false)
    private Evideo video;

    public Ecomment() {

    }
}
