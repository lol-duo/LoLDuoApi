package com.lolduo.duo.response.championDetail.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailRankWinRate {
    Long rankNumber;
    String winRate;

    public DetailRankWinRate(Long rankNumber, String winRate) {
        this.rankNumber = rankNumber;
        this.winRate = winRate;
    }
}
