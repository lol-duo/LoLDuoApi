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
    String championImgUrl;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/mainRune/LethalTempoTemp.svg")
    String mainRuneImgUrl;
    @ApiModelProperty(example = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/position/BOTTOM.svg")
    String positionImgUrl;

    public DetailChampionComp(String championName, String championImgUrl, String mainRuneImgUrl, String positionImgUrl) {
        this.championName = championName;
        this.championImgUrl = championImgUrl;
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.positionImgUrl = positionImgUrl;
    }
}
