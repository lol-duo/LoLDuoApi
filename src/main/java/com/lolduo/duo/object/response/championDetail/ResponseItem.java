package com.lolduo.duo.object.response.championDetail;

import com.lolduo.duo.object.response.championDetail.sub.ItemUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseItem {
    List<ItemUrl> itemList;
    @ApiModelProperty(example = "1,000게임")
    private String allCount;
    @ApiModelProperty(example = "22.22%")
    private String winRate;

    public ResponseItem(List<ItemUrl> itemList, String allCount, String winRate) {
        this.itemList = itemList;
        this.allCount = allCount;
        this.winRate = winRate;
    }
}
