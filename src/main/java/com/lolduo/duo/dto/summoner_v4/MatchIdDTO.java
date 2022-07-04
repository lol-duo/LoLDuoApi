package com.lolduo.duo.dto.summoner_v4;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MatchIdDTO {
    private List<String> matchIdList;

    public MatchIdDTO(List<String> matchIdList) {
        this.matchIdList = matchIdList;
    }
}
