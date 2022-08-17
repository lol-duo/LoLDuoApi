package com.lolduo.duo.object.entity.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.sub.Item;
import com.lolduo.duo.object.entity.clientInfo.sub.Perk;
import com.lolduo.duo.object.entity.clientInfo.sub.Spell;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface ICombiEntity {
    Long getId();
    TreeSet<Long> getChampionId();
    Map<Long, String> getPosition();
    Long getAllCount();
    Long getWinCount();
    List<Perk> getPerkList();
    List<Spell> getSpellList();
    List<Item> getItemList();

    void setAllCount(Long allCount);
    void setWinCount(Long winCount);
}
