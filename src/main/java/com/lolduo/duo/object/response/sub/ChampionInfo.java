package com.lolduo.duo.object.response.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChampionInfo {
    @ApiModelProperty(example = "애니")
    private String championName;

    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Annie.png")
    private String imgUrl;

    @ApiModelProperty(example = "UTILITY")
    private String position;

    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/line/UTILITY.png")
    private String positionUrl;

    public ChampionInfo(String championName, String imgUrl, String position, String positionUrl) {
        this.championName = championName;
        this.imgUrl = imgUrl;
        this.position = position;
        this.positionUrl = positionUrl;
    }
}
