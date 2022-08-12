package com.lolduo.duo.object.entity.clientInfo.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
@Getter
@NoArgsConstructor
public class Sub implements Comparable<Sub>{

    private Long win;

    public void setWin(Long win) {
        this.win = win;
    }

    public Sub(Long win) {
        this.win = win;
    }

    @Override
    public int compareTo(@NotNull Sub sub) {
        return this.win <= sub.getWin() ? -1 : 1;
    }
}
