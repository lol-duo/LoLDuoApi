package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerksDto {
    private PerkStatsDto statPerks;
    private List<PerkStyleDto> styles;

    public PerksDto(PerkStatsDto statPerks, List<PerkStyleDto> styles) {
        this.statPerks = statPerks;
        this.styles = styles;
    }
}
