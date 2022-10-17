package com.lolduo.duo.object.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CombiSearchV2DTO {
    @ApiModelProperty(example = "ALL")
    private String position;

    @ApiModelProperty(example = "0")
    private Long championId;

    public CombiSearchV2DTO(String position, Long championId) {
        this.position = position;
        this.championId = championId;
    }
}
