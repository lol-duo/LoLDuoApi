package com.lolduo.duo.object.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class SpellUrl {
    @ApiModelProperty(example = "[img1,img2]")
    private List<String> spellUrlList;

    public SpellUrl(List<String> spellUrlList) {
        this.spellUrlList = spellUrlList;
    }
}
