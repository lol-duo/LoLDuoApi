package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeSet;
@Getter
@NoArgsConstructor
@Setter
public class Spell extends Sub  {
    private Map<Long, TreeSet<Long>> spellMap;

    public Spell(Map<Long, TreeSet<Long>> spellMap, Long win) {
        super(win);
        this.spellMap = spellMap;
    }
}
