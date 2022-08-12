package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
@Getter
@NoArgsConstructor
public class Perk extends Sub{
    private Map<Long, List<Long>> perkMap;

    public Perk(Map<Long, List<Long>> perkMap, Long win) {
        super(win);
        this.perkMap = perkMap;
    }
}
