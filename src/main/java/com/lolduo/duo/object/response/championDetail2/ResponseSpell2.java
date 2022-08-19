package com.lolduo.duo.object.response.championDetail2;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseSpell2 {
    String winRate;
    String AllCount;
    List<String>  spellUrlList;

    public ResponseSpell2(String winRate, String allCount, List<String> spellUrlList) {
        this.winRate = winRate;
        AllCount = allCount;
        this.spellUrlList = spellUrlList;
    }
}
