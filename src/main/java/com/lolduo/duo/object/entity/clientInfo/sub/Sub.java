package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Sub implements Comparable<Sub> {
    private Long win;
    private Long allCount;

    public Sub(Long win, Long allCount) {
        this.win = win;
        this.allCount = allCount;
    }

    @Override
    public int compareTo(@NotNull Sub sub) {
        return ((double) this.win / this.allCount) <= (double) sub.getWin() / sub.getAllCount() ? -1 : 1;
    }
}
