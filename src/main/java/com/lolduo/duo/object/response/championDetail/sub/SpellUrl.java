package com.lolduo.duo.object.response.championDetail.sub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SpellUrl {
    @ApiModelProperty(example = "1img,2img,3img")
    private List<String> spellUrlList;

    public SpellUrl(List<String> spellUrlList) {
        this.spellUrlList = spellUrlList;
    }
}
