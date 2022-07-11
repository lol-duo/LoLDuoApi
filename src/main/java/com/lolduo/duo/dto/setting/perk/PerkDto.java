package com.lolduo.duo.dto.setting.perk;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerkDto {
    private Long id;
    private String key;
    private String icon;
    private String name;
    private List<PerkRuneList> slots;

    public PerkDto(Long id, String key, String icon, String name, List<PerkRuneList> slots) {
        this.id = id;
        this.key = key;
        this.icon = icon;
        this.name = name;
        this.slots = slots;
    }
}
