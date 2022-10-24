package com.lolduo.duo.v2.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChampionResponse {
    private String championName;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Ziggs.svg")
    private String championImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg")
    private String mainRuneImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/BOTTOM.svg")
    private String positionImgUrl;

    public ChampionResponse(String championName, String championImgUrl, String mainRuneImgUrl, String positionImgUrl) {
        this.championName = championName;
        this.championImgUrl = championImgUrl;
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.positionImgUrl = positionImgUrl;
    }
}
