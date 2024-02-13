package com.dopaming.dopaming.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue
    private Long user_id;

    @Column
    private String user_name;

    @Column
    private String user_email;

    @Column
    private String password;
}
