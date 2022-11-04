package com.lolduo.duo.v2.response.championDetail.sub;

import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailItem;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailRune;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailSpell;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailInfo {
    DetailSpell detailSpell;
    DetailRune detailRune;
    DetailItem detailItem;

    public DetailInfo(DetailSpell detailSpell, DetailRune detailRune, DetailItem detailItem) {
        this.detailSpell = detailSpell;
        this.detailRune = detailRune;
        this.detailItem = detailItem;
    }
}
