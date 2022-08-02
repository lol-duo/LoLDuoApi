package com.lolduo.duo.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class ClinetChampionInfoDTO {
    @ApiModelProperty(example = "가렌")
    private String championName;

    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Garen.png")
    private String imgUrl;

    @ApiModelProperty(example = "TOP")
    private String position;

    public ClinetChampionInfoDTO(String championName, String imgUrl, String position) {
        this.championName = championName;
        this.imgUrl = imgUrl;
        this.position = position;
    }
}
