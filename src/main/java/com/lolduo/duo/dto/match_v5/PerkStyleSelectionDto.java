package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerkStyleSelectionDto {
    private Long perk;
    private Long var1;
    private Long var2;
    private Long var3;

    public PerkStyleSelectionDto(Long perk, Long var1, Long var2, Long var3) {
        this.perk = perk;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
}
