package com.lolduo.duo.entity.clientInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Item {
    private Map<Long, List<Long>> ItemMap;
    private Long win;

    public Item(Map<Long, List<Long>> itemMap, Long win) {
        ItemMap = itemMap;
        this.win = win;
    }
}
