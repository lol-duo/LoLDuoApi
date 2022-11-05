package com.lolduo.duo.v2.response.championDetail;

import com.lolduo.duo.v2.response.championDetail.sub.DetailInfo;
import com.lolduo.duo.v2.response.championDetail.sub.DetailRankWinRate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailSolo {
    DetailRankWinRate detailRankWinRate;
    DetailInfo detailInfo;

    public DetailSolo(DetailRankWinRate detailRankWinRate, DetailInfo detailInfo) {
        this.detailRankWinRate = detailRankWinRate;
        this.detailInfo = detailInfo;
    }
}
