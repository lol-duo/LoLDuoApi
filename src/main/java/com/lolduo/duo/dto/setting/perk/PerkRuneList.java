package com.lolduo.duo.dto.setting.perk;

import jdk.jfr.Name;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerkRuneList {
    private List<PerkRune> runes;

    public PerkRuneList(List<PerkRune> runes) {
        this.runes = runes;
    }
}
