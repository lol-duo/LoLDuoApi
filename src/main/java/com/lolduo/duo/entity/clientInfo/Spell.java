package com.lolduo.duo.entity.clientInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.TreeSet;
@Getter
@NoArgsConstructor
public class Spell {
    private Map<Long, TreeSet<Long>> spellMap;
    private Long win;

    public Spell(Map<Long, TreeSet<Long>> spellMap, Long win) {
        this.spellMap = spellMap;
        this.win = win;
    }
}
