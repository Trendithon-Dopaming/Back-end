package com.dopaming.dopaming.requestDto;

import com.dopaming.dopaming.domain.Category;
import com.dopaming.dopaming.domain.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PamingNewDto {
    private String paming_title;

    private LocalDateTime start_date;
    private LocalDateTime end_date;

    private String info;

    private boolean pub_priv;

    private Region region;

    private Category category;
}
