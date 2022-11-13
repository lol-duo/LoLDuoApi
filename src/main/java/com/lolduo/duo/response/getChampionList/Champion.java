package com.lolduo.duo.response.getChampionList;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class Champion implements Comparable<Champion>{
    @ApiModelProperty(example = "86")
    private Long id;
    @ApiModelProperty(example = "가렌")
    private String name;
    @ApiModelProperty(example = "Garen")
    private String engName;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Garen.png")
    private String imgUrl;

    public Champion(Long id, String name, String engName, String imgUrl) {
        this.id = id;
        this.name = name;
        this.engName = engName;
        this.imgUrl = imgUrl;
    }
    @Override
    public int compareTo(Champion champion) {
        return this.name.compareTo(champion.name);
    }
}
