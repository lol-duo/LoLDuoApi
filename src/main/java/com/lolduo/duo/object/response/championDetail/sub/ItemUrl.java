package com.lolduo.duo.object.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemUrl {
    @ApiModelProperty(example = "1img,2img,3img")
    List<String> ItemUrlList;

    public ItemUrl(List<String> itemUrlList) {
        ItemUrlList = itemUrlList;
    }
}
