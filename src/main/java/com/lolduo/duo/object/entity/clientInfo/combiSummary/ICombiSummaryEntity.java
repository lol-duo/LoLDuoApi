package com.lolduo.duo.object.entity.clientInfo.combiSummary;

import com.lolduo.duo.object.entity.clientInfo.sub.Item;
import com.lolduo.duo.object.entity.clientInfo.sub.Perk;
import com.lolduo.duo.object.entity.clientInfo.sub.Spell;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface ICombiSummaryEntity {
    Long getId();
    TreeSet<Long> getChampionId();
    Map<Long, String> getPosition();
    Long getAllCountSum();
    Long getWinCountSum();
    Double getWinRate();
}
