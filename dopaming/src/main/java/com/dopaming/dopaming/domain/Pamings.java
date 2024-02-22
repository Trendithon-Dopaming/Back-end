package com.dopaming.dopaming.domain;

import com.dopaming.dopaming.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@Entity
public class Pamings extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paming_title;

    @Column
    private LocalDateTime start_date;

    @Column
    private LocalDateTime end_date;

    @Column
    private String info;

    @Column
    private boolean pub_priv;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column
    private String photo_name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "pamings", cascade = {PERSIST, REMOVE})
    private List<Steps> steps = new ArrayList<>();

    public void setPaming_title(String paming_title){
        this.paming_title = paming_title;
    }
    public void setStart_date(LocalDateTime start_date){
        this.start_date = start_date;
    }
    public void setEnd_date(LocalDateTime end_date){
        this.end_date = end_date;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setPub_priv(boolean pub_priv) {
        this.pub_priv = pub_priv;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setRegion(Region region) {
        this.region = region;
    }
    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
    public void setUsers(Users users) {
        this.users = users;
    }
}
