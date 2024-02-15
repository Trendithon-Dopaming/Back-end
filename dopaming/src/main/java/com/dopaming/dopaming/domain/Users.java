package com.dopaming.dopaming.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@NoArgsConstructor
@Entity
@Getter
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String user_name;

    @Column
    private String user_email;

    @Column
    private String password;

    @OneToMany(mappedBy = "users", cascade = {REMOVE})
    private List<Pamings> pamings = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = {REMOVE})
    private List<PamingSaves> pamingSaves = new ArrayList<>();


    public Users(String user_name, String user_email, String password) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.password = password;
    }
}
