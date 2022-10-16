package com.lolduo.duo.object.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CombiSearchV2DTO {
    @ApiModelProperty(example = "ALL")
    private String position;

    @ApiModelProperty(example = "")
    private String championName;
}
