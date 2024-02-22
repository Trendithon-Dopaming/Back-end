package com.dopaming.dopaming.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@Entity
public class Steps {

    @Id
    @Column(name = "step_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Pamings pamings;

    @Column
    private boolean success;

    @Column
    private int step;

    @Column
    private String content;

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
