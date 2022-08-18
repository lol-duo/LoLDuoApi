package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Item extends Sub  {
    private Map<Long, List<Long>> ItemMap;
    public Item(Map<Long, List<Long>> itemMap, Long win, Long allCount) {
        super(win, allCount);
        ItemMap = itemMap;
    }
}
