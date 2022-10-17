package com.lolduo.duo.object.response.v2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SoloResponseV2 {
    @ApiModelProperty(example = "15")
    private Long combiId;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.png")
    private String rankChangeImgUrl;
    @ApiModelProperty(example = "+1")
    private String rankChangeNumber;
    @ApiModelProperty(example = "C8AA6E")
    private String rankNumberColor;
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

    public SoloResponseV2(Long combiId,String rankChangeImgUrl, String rankChangeNumber, String rankNumberColor, String championName, String championImgUrl, String mainRuneImgUrl, String positionImgUrl, String winRate) {
        this.combiId = combiId;
        this.rankChangeImgUrl = rankChangeImgUrl;
        this.rankChangeNumber = rankChangeNumber;
        this.rankNumberColor = rankNumberColor;
        this.championName = championName;
        this.championImgUrl = championImgUrl;
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.positionImgUrl = positionImgUrl;
        this.winRate = winRate;
    }
}
