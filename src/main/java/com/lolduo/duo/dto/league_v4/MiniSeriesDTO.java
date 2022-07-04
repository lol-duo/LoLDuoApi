package com.lolduo.duo.dto.league_v4;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MiniSeriesDTO {
    private Long losses;
    private String progress;
    private Long target;
    private Long wins;

    public MiniSeriesDTO(Long losses, String progress, Long target, Long wins) {
        this.losses = losses;
        this.progress = progress;
        this.target = target;
        this.wins = wins;
    }
}
