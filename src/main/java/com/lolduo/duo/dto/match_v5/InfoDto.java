package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InfoDto {
    private Long gameCreation;
    private Long gameDuration;
    private Long gameEndTimestamp;
    private Long gameId;
    private String gameMode;
    private String gameName;
    private Long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private Long mapId;
    private List<ParticipantDto> participants;
    private String platformId;
    private Long queueId;
    private List<TeamDto> teams;
    private String tournamentCode;

    public InfoDto(Long gameCreation, Long gameDuration, Long gameEndTimestamp, Long gameId, String gameMode, String gameName, Long gameStartTimestamp, String gameType, String gameVersion, Long mapId, List<ParticipantDto> participants, String platformId, Long queueId, List<TeamDto> teams, String tournamentCode) {
        this.gameCreation = gameCreation;
        this.gameDuration = gameDuration;
        this.gameEndTimestamp = gameEndTimestamp;
        this.gameId = gameId;
        this.gameMode = gameMode;
        this.gameName = gameName;
        this.gameStartTimestamp = gameStartTimestamp;
        this.gameType = gameType;
        this.gameVersion = gameVersion;
        this.mapId = mapId;
        this.participants = participants;
        this.platformId = platformId;
        this.queueId = queueId;
        this.teams = teams;
        this.tournamentCode = tournamentCode;
    }
}
