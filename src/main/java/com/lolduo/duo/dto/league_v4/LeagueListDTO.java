package com.lolduo.duo.dto.league_v4;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class LeagueListDTO {
    private String leagueId;
    private List<LeagueItemDTO> entries;
    private String tier;
    private String name;
    private String queue;

    public LeagueListDTO(String leagueId, List<LeagueItemDTO> entries, String tier, String name, String queue) {
        this.leagueId = leagueId;
        this.entries = entries;
        this.tier = tier;
        this.name = name;
        this.queue = queue;
    }
}
