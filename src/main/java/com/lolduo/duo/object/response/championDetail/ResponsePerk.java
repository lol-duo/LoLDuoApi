package com.lolduo.duo.object.response.championDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ResponsePerk {
    private List<PerkUrl> perkList;

    @ApiModelProperty(example = "22.22%")
    private String winRate;

    @ApiModelProperty(example = "1,000게임")
    private String allCount;

    public ResponsePerk(List<PerkUrl> perkList,  String allCount,String winRate) {
        this.perkList = perkList;
        this.winRate = winRate;
        this.allCount = allCount;
    }
}
