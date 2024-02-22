package com.dopaming.dopaming.requestDto;

import com.dopaming.dopaming.domain.Category;
import com.dopaming.dopaming.domain.Region;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PamingDto {
    private String userId;
    private String paming_title;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String info;
    private boolean pub_priv;
    private Category category;
    private Region region;
    private String photoName;
}
