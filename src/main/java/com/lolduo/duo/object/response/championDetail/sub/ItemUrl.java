package com.lolduo.duo.object.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ItemUrl {
    @ApiModelProperty(example = "[img1,꺽쇠.png,img2,꺽쇠.png,img3]")
    private List<String> itemUrlList;

    public ItemUrl(List<String> perkUrlList) {
        this.itemUrlList = perkUrlList;
    }
}
