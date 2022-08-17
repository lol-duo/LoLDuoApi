package com.lolduo.duo.object.response;

import com.lolduo.duo.object.response.sub.ChampionInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ChampionInfoList implements Comparable<ChampionInfoList>{
    private List<ChampionInfoResponse> championInfoResponseList;
    @ApiModelProperty(example = "58.7%")
    private String winRate;

    @ApiModelProperty(example = "1,000게임")
    private String allCount;
    public ChampionInfoList(List<ChampionInfoResponse> championInfoResponseList, String winRate, String allCount) {
        this.championInfoResponseList = championInfoResponseList;
        this.winRate = winRate;
        this.allCount = allCount;
    }

    @Override
    public int compareTo(ChampionInfoList championInfoList) {
        int a = Double.valueOf(this.winRate.substring(0,this.winRate.length()-1)).intValue();
        int b= Double.valueOf(championInfoList.getWinRate().substring(0, championInfoList.getWinRate().length()-1)).intValue();
        if(b < a)
            return -1;
        else if(a==b)
            return 0;
        else
            return 1;
    }
}
