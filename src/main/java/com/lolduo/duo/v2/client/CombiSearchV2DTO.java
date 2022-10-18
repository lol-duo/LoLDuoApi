package com.lolduo.duo.v2.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CombiSearchV2DTO {
    @ApiModelProperty(example = "0")
    private Long championId;
    @ApiModelProperty(example = "ALL")
    private String position;

    public CombiSearchV2DTO(Long championId,String position) {
        this.championId = championId;
        this.position = position;
    }
}
