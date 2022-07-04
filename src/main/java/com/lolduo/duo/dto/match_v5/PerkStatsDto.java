package com.lolduo.duo.dto.match_v5;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerkStatsDto {
    private Long defense;
    private Long flex;
    private Long offense;

    public PerkStatsDto(Long defense, Long flex, Long offense) {
        this.defense = defense;
        this.flex = flex;
        this.offense = offense;
    }
}
