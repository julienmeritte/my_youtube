package com.etna.myapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "user")
public class Eusers implements Serializable {

    @Id
    @GeneratedValue
    private Long idUser;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String pseudo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "user")
    private List<Evideo> videos;

    @OneToMany(mappedBy = "user")
    private List<Ecomment> comments;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Erole> roles;

    public Eusers() {
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        created_at = LocalDateTime.now();
    }
}
