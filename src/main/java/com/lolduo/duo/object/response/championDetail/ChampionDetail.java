package com.lolduo.duo.object.response.championDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ChampionDetail {
    List<ResponsePerk> perkInfo;
    List<ResponseSpell> spellInfo;
    List<ResponseItem> itemInfo;

    public ChampionDetail(List<ResponsePerk> perkInfo, List<ResponseSpell> spellInfo, List<ResponseItem> itemInfo) {
        this.perkInfo = perkInfo;
        this.spellInfo = spellInfo;
        this.itemInfo = itemInfo;
    }
}
