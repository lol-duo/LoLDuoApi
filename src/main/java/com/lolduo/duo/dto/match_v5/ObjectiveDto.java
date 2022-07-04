package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ObjectiveDto {
    private Boolean first;
    private Long kills;

    public ObjectiveDto(Boolean first, Long kills) {
        this.first = first;
        this.kills = kills;
    }
}
