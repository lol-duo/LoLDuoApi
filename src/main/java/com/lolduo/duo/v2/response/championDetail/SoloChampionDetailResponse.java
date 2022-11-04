package com.lolduo.duo.v2.response.championDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SoloChampionDetailResponse {
    @ApiModelProperty(example = "미스포츈")
    String championName;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/champion/MissFortune.svg")
    String championImg;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/mainRune/LethalTempoTemp.svg")
    String mainRuneImg;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/position/BOTTOM.svg")
    String positionImg;

    List<SoloDetailItem> soloDetailItemList =new ArrayList<>();


    public SoloChampionDetailResponse(String championName, String championImg, String mainRuneImg, String positionImg, List<SoloDetailItem> soloDetailItemList) {
        this.championName = championName;
        this.championImg = championImg;
        this.mainRuneImg = mainRuneImg;
        this.positionImg = positionImg;
        this.soloDetailItemList = soloDetailItemList;
    }
}
