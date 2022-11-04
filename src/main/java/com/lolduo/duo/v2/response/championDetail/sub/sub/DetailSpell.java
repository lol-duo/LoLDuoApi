package com.lolduo.duo.v2.response.championDetail.sub.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailSpell {
    String firstSpell;
    String secondSpell;

    public DetailSpell(String firstSpell, String secondSpell) {
        this.firstSpell = firstSpell;
        this.secondSpell = secondSpell;
    }
}
