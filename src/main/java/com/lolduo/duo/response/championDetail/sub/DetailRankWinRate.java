package com.lolduo.duo.response.championDetail.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailRankWinRate {
    String rankBadge;
    String winRate;

    public DetailRankWinRate(String rankBadge, String winRate) {
        this.rankBadge = rankBadge;
        this.winRate = winRate;
    }
}
