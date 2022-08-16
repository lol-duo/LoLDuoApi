package com.lolduo.duo.object.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CombiSearchDTO {

    @ApiModelProperty(example = "[{\"championId\": 1,\"position\": \"UTILITY\"}]")
    private List<ChampionInfoDTO> championInfoDTOList;
    @ApiModelProperty(example = "false")
    private Boolean winRateAsc;
    @ApiModelProperty(example = "null")
    private Boolean gameCountAsc;

    public CombiSearchDTO(List<ChampionInfoDTO> championInfoDTOList, boolean winRateAsc, boolean gameCountAsc) {
        this.championInfoDTOList = championInfoDTOList;
        this.winRateAsc = winRateAsc;
        this.gameCountAsc = gameCountAsc;
    }
}