package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ObjectivesDto {
    private ObjectiveDto baron;
    private ObjectiveDto champion;
    private ObjectiveDto dragon;
    private ObjectiveDto inhibitor;
    private ObjectiveDto riftHerald;
    private ObjectiveDto tower;

    public ObjectivesDto(ObjectiveDto baron, ObjectiveDto champion, ObjectiveDto dragon, ObjectiveDto inhibitor, ObjectiveDto riftHerald, ObjectiveDto tower) {
        this.baron = baron;
        this.champion = champion;
        this.dragon = dragon;
        this.inhibitor = inhibitor;
        this.riftHerald = riftHerald;
        this.tower = tower;
    }
}
