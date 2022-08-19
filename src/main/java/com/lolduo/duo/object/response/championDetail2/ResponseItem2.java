package com.lolduo.duo.object.response.championDetail2;

import lombok.Getter;

import java.util.List;
@Getter
public class ResponseItem2 {
    String winRate;
    String AllCount;
    List<String> itemUrlList;

    public ResponseItem2(String winRate, String allCount, List<String> itemUrlList) {
        this.winRate = winRate;
        AllCount = allCount;
        this.itemUrlList = itemUrlList;
    }
}
