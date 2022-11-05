package com.lolduo.duo.v2.response.championDetail;

import com.lolduo.duo.v2.response.championDetail.sub.DetailChampionComp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DetailSoloResponse {
    DetailChampionComp detailChampionComp;
    List<DetailSolo> detailSoloList = new ArrayList<>();

    public DetailSoloResponse(DetailChampionComp detailChampionComp, List<DetailSolo> detailSoloList) {
        this.detailChampionComp = detailChampionComp;
        this.detailSoloList = detailSoloList;
    }
}
