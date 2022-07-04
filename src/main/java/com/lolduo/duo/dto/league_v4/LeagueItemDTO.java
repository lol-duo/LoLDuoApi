package com.lolduo.duo.dto.league_v4;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LeagueItemDTO {
    private Boolean freshBlood;
    private Long wins;
    private String summerName;
    private MiniSeriesDTO miniSeries;
    private Boolean inactive;
    private Boolean verteran;
    private Boolean hotStreak;
    private String rank;
    private Long leaguePoints;
    private Long losses;
    private String summonerId;

    public LeagueItemDTO(Boolean freshBlood, Long wins, String summerName, MiniSeriesDTO miniSeries, Boolean inactive, Boolean verteran, Boolean hotStreak, String rank, Long leaguePoints, Long losses, String summonerId) {
        this.freshBlood = freshBlood;
        this.wins = wins;
        this.summerName = summerName;
        this.miniSeries = miniSeries;
        this.inactive = inactive;
        this.verteran = verteran;
        this.hotStreak = hotStreak;
        this.rank = rank;
        this.leaguePoints = leaguePoints;
        this.losses = losses;
        this.summonerId = summonerId;
    }
}
