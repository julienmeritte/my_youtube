package com.etna.myapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "video")
public class Evideo {

    @Id
    @GeneratedValue
    private Long idVideo;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 45, nullable = false)
    private String namePath;

    @Column(nullable = true)
    private int duration;

    @Column(length = 120, nullable = false)
    private String source;

    @ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private Eusers user;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private Integer view;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "video")
    private List<Ecomment> comments;

    @OneToMany(mappedBy = "video")
    private List<Eformat> formats;

    public Evideo() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        created_at = LocalDateTime.now();
        view = 0;
    }
}
