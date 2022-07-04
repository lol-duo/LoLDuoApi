package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MatchDto {
    private MetadataDto metadataDto;
    private InfoDto infoDto;

    public MatchDto(MetadataDto metadataDto, InfoDto infoDto) {
        this.metadataDto = metadataDto;
        this.infoDto = infoDto;
    }
}
