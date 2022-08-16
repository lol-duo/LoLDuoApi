package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Setter
public class Item extends Sub {
    private Map<Long, List<Long>> ItemMap;
    public Item(Map<Long, List<Long>> itemMap, Long win) {
        super(win);
        ItemMap = itemMap;
    }
}
