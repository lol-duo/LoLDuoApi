package com.lolduo.duo.v2.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SoloResponseV2 {
    @ApiModelProperty(example = "15")
    private Long id;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.png")
    private String rankChangeImgUrl;
    @ApiModelProperty(example = "+1")
    private String rankChangeNumber;
    @ApiModelProperty(example = "C8AA6E")
    private String rankChangeColor;
    @ApiModelProperty(example = "티모")
    private String championName;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.png")
    private String championImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.png")
    private String mainRuneImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.png")
    private String positionImgUrl;
    @ApiModelProperty(example = "67.2%")
    private String winRate;
    @ApiModelProperty(example = "1")
    private Long rankNumber ;

    public SoloResponseV2(Long id, String rankChangeImgUrl, String rankChangeNumber, String rankChangeColor, String championName, String championImgUrl, String mainRuneImgUrl, String positionImgUrl, String winRate, Long rankNumber) {
        this.id = id;
        this.rankChangeImgUrl = rankChangeImgUrl;
        this.rankChangeNumber = rankChangeNumber;
        this.rankChangeColor = rankChangeColor;
        this.championName = championName;
        this.championImgUrl = championImgUrl;
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.positionImgUrl = positionImgUrl;
        this.winRate = winRate;
        this.rankNumber = rankNumber;
    }
}
