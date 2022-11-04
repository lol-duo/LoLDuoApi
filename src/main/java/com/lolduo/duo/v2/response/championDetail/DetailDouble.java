package com.lolduo.duo.v2.response.championDetail;

import com.lolduo.duo.v2.response.championDetail.sub.DetailInfo;
import com.lolduo.duo.v2.response.championDetail.sub.DetailRankWinRate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailDouble {
    DetailRankWinRate detailRankWinRate;
    DetailInfo detailInfo1;
    DetailInfo detailInfo2;

    public DetailDouble(DetailRankWinRate detailRankWinRate, DetailInfo detailInfo1, DetailInfo detailInfo2) {
        this.detailRankWinRate = detailRankWinRate;
        this.detailInfo1 = detailInfo1;
        this.detailInfo2 = detailInfo2;
    }
}
