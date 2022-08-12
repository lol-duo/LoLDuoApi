package com.lolduo.duo.object.response.championDetail;

import com.lolduo.duo.object.response.championDetail.sub.SpellUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ResponseSpell {
    private List<SpellUrl> spellList;

    @ApiModelProperty(example = "1,000게임")
    private String allCount;
    @ApiModelProperty(example = "22.22%")
    private String winRate;



    public ResponseSpell(List<SpellUrl> spellList, String allCount, String winRate) {
        this.spellList = spellList;
        this.winRate = winRate;
        this.allCount = allCount;
    }
}
