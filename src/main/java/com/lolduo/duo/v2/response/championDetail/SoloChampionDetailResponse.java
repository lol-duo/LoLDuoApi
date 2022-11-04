package com.lolduo.duo.v2.response.championDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SoloChampionDetailResponse {
    @ApiModelProperty(example = "15")
    String mainRuneImg;
    @ApiModelProperty(example = "15")
    String championImg;
    @ApiModelProperty(example = "15")
    String positionImg;
    @ApiModelProperty(example = "15")

    List<SoloDetailItem> soloDetailItemList =new ArrayList<>();

    public SoloChampionDetailResponse(String mainRuneImg, String championImg, String positionImg, String rankBadge, String winRate, List<SoloDetailItem> soloDetailItemList) {
        this.mainRuneImg = mainRuneImg;
        this.championImg = championImg;
        this.positionImg = positionImg;
        this.soloDetailItemList = soloDetailItemList;
    }
}
