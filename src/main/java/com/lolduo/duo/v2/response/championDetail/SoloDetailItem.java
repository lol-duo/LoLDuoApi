package com.lolduo.duo.v2.response.championDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SoloDetailItem {
    DetailSpell detailSpell;
    DetailRune detailRune;
    DetailItem detailItem;
    String rankBadge;
    String winRate;
    public SoloDetailItem(DetailSpell detailSpell, DetailRune detailRune, DetailItem detailItem, String rankBadge, String winRate) {
        this.detailSpell = detailSpell;
        this.detailRune = detailRune;
        this.detailItem = detailItem;
        this.rankBadge = rankBadge;
        this.winRate = winRate;
    }
}
