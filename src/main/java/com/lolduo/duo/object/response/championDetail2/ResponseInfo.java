package com.lolduo.duo.object.response.championDetail2;

import lombok.Getter;

import java.util.List;
@Getter
public class ResponseInfo {
    Long championId;
    String championPosition;
    String championPositionUrl;
    String championImgUrl;
    List<String> keyStoneListUrl;
    String keyItemUrl;
    List<ResponsePerk2> perkList;
    List<ResponseItem2> itemList;
    List<ResponseSpell2> spellList;

    public ResponseInfo(Long championId, String championPosition, String championPositionUrl, String championImgUrl, List<String> keyStoneListUrl, String keyItemUrl, List<ResponsePerk2> perkList, List<ResponseItem2> itemList, List<ResponseSpell2> spellList) {
        this.championId = championId;
        this.championPosition = championPosition;
        this.championPositionUrl = championPositionUrl;
        this.championImgUrl = championImgUrl;
        this.keyStoneListUrl = keyStoneListUrl;
        this.keyItemUrl = keyItemUrl;
        this.perkList = perkList;
        this.itemList = itemList;
        this.spellList = spellList;
    }
}
