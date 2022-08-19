package com.lolduo.duo.object.response.championDetail2;

import lombok.Getter;

import java.util.List;
@Getter
public class ChampionDetail2 {
    String winRate;
    String allCount;
    String thisWinRate;
    String thisAllCount;
    List<ResponseInfo> InfoList;

    public ChampionDetail2(String winRate, String allCount, String thisWinRate, String thisAllCount, List<ResponseInfo> infoList) {
        this.winRate = winRate;
        this.allCount = allCount;
        this.thisWinRate = thisWinRate;
        this.thisAllCount = thisAllCount;
        InfoList = infoList;
    }
}