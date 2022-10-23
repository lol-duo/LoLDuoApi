package com.lolduo.duo.v2.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoubleResponseV2 {
    @ApiModelProperty(example = "15")
    private Long id;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.svg")
    private String rankChangeImgUrl;
    @ApiModelProperty(example = "+1")
    private String rankChangeNumber;
    @ApiModelProperty(example = "C8AA6E")
    private String rankChangeColor;
    @ApiModelProperty(example = "1")
    private Long rankNumber ;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/logo/Group.svg")
    private String rankNumberIcon ;
    @ApiModelProperty(example = "C8AA6E")
    private String rankNumberColor;


    @ApiModelProperty(example = "티모")
    private String champion1Name;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.svg")
    private String champion1ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg")
    private String mainRune1ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.svg")
    private String position1ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/icon/listImage.svg")
    private String listImage1;


    @ApiModelProperty(example = "직스")
    private String champion2Name;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Ziggs.svg")
    private String champion2ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg")
    private String mainRune2ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/BOTTOM.svg")
    private String position2ImgUrl;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/icon/listImage.svg")
    private String listImage2;

    @ApiModelProperty(example = "67.2%")
    private String winRate;

    public DoubleResponseV2(Long id, String rankChangeImgUrl, String rankChangeNumber, String rankChangeColor, Long rankNumber, String rankNumberIcon, String rankNumberColor, String champion1Name, String champion1ImgUrl, String mainRune1ImgUrl, String position1ImgUrl, String listImage1, String champion2Name, String champion2ImgUrl, String mainRune2ImgUrl, String position2ImgUrl, String listImage2, String winRate) {
        this.id = id;
        this.rankChangeImgUrl = rankChangeImgUrl;
        this.rankChangeNumber = rankChangeNumber;
        this.rankChangeColor = rankChangeColor;
        this.rankNumber = rankNumber;
        this.rankNumberIcon = rankNumberIcon;
        this.rankNumberColor = rankNumberColor;

        this.champion1Name = champion1Name;
        this.champion1ImgUrl = champion1ImgUrl;
        this.mainRune1ImgUrl = mainRune1ImgUrl;
        this.position1ImgUrl = position1ImgUrl;
        this.listImage1 = listImage1;

        this.champion2Name = champion2Name;
        this.champion2ImgUrl = champion2ImgUrl;
        this.mainRune2ImgUrl = mainRune2ImgUrl;
        this.position2ImgUrl = position2ImgUrl;
        this.listImage2 = listImage2;

        this.winRate = winRate;
    }
}
