package com.lolduo.duo.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailChampionComp {
    @ApiModelProperty(example = "미스포츈")
    String championName;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/champion/MissFortune.svg")
    String championImg;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/mainRune/LethalTempoTemp.svg")
    String mainRuneImg;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/position/BOTTOM.svg")
    String positionImg;

    public DetailChampionComp(String championName, String championImg, String mainRuneImg, String positionImg) {
        this.championName = championName;
        this.championImg = championImg;
        this.mainRuneImg = mainRuneImg;
        this.positionImg = positionImg;
    }
}
