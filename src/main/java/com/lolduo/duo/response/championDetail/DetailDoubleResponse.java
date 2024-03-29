package com.lolduo.duo.response.championDetail;

import com.lolduo.duo.response.championDetail.sub.DetailChampionComp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DetailDoubleResponse {
    DetailChampionComp detailChampionComp1;
    DetailChampionComp detailChampionComp2;
    List<DetailDouble> detailDoubleList = new ArrayList<>();

    public DetailDoubleResponse(DetailChampionComp detailChampionComp1, DetailChampionComp detailChampionComp2, List<DetailDouble> detailDoubleList) {
        this.detailChampionComp1 = detailChampionComp1;
        this.detailChampionComp2 = detailChampionComp2;
        this.detailDoubleList = detailDoubleList;
    }
}
