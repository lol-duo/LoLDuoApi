package com.lolduo.duo.response.mainPage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SoloResponseV2 {
    @ApiModelProperty(example = "15")
    private Long id;
    @ApiModelProperty(example = "0")
    private Long rankChangeNumber;
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

    public SoloResponseV2(Long id, Long rankChangeNumber, String championName, String championImgUrl, String mainRuneImgUrl, String positionImgUrl, String winRate, Long rankNumber) {
        this.id = id;
        this.rankChangeNumber = rankChangeNumber;
        this.championName = championName;
        this.championImgUrl = championImgUrl;
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.positionImgUrl = positionImgUrl;
        this.winRate = winRate;
        this.rankNumber = rankNumber;
    }
}
