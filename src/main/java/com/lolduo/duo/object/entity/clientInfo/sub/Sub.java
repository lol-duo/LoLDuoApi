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
        if(this.allCount < sub.getAllCount())
            return -1;
        else if(this.allCount == sub.getAllCount()) {
            return ((double) this.win / this.allCount) <= (double) sub.getWin() / sub.getAllCount() ? -1 : 1;
        }
        else{
            return 1;
        }
    }
}
