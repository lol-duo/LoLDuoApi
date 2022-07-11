package com.lolduo.duo.dto.setting.perk;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerkRune {
    private Long id;
    private String key;
    private String icon;
    private String name;
    private String shortDesc;
    private String longDesc;

    public PerkRune(Long id, String key, String icon, String name, String shortDesc, String longDesc) {
        this.id = id;
        this.key = key;
        this.icon = icon;
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
