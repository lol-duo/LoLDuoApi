package com.lolduo.duo.object.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class PerkUrl {
    @ApiModelProperty(example = "[img1,img2,img3]")
    private List<String> perkUrlList;

    public PerkUrl(List<String> perkUrlList) {
        this.perkUrlList = perkUrlList;
    }
}
