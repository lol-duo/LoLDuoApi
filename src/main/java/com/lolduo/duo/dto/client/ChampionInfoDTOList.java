package com.lolduo.duo.dto.client;

import com.lolduo.duo.entity.ChampionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ChampionInfoDTOList implements Comparable<ChampionInfoDTOList>{
    private List<ClinetChampionInfoDTO> clinetChampionInfoDTOList;
    @ApiModelProperty(example = "58.7%")
    private String winRate;

    public ChampionInfoDTOList(List<ClinetChampionInfoDTO> clinetChampionInfoDTOList, String winRate) {
        this.clinetChampionInfoDTOList = clinetChampionInfoDTOList;
        this.winRate = winRate;
    }


    @Override
    public int compareTo(ChampionInfoDTOList championInfoDTOList) {
        int a = Double.valueOf(this.winRate.substring(0,this.winRate.length()-1)).intValue();
        int b= Double.valueOf(championInfoDTOList.getWinRate().substring(0,championInfoDTOList.getWinRate().length()-1)).intValue();
        if(b < a)
            return -1;
        else if(a==b)
            return 0;
        else
            return 1;
    }
}
