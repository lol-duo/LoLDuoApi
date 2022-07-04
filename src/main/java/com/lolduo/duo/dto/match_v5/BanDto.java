package com.lolduo.duo.dto.match_v5;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BanDto {
    private Long championId;
    private Long pickTurn;

    public BanDto(Long championId, Long pickTurn) {
        this.championId = championId;
        this.pickTurn = pickTurn;
    }
}
