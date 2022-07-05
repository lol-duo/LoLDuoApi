package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MatchDto {
    private MetadataDto metadata;
    private InfoDto info;

    public MatchDto(MetadataDto metadataDto, InfoDto infoDto) {
        this.metadata = metadataDto;
        this.info = infoDto;
    }
}
