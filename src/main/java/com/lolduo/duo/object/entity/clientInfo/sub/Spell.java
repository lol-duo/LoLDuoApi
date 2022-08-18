package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;

import java.util.Map;
import java.util.TreeSet;
@Getter
public class Spell extends Sub {
    private Map<Long, TreeSet<Long>> spellMap;

    public Spell(Map<Long, TreeSet<Long>> spellMap, Long win,Long allCount) {
        super(win,allCount);
        this.spellMap = spellMap;
    }
}
