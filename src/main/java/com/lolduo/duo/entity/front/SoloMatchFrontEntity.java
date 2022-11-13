package com.lolduo.duo.entity.front;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "solo_match_front",indexes = {
        @Index(name ="win_rate_index", columnList = "win_rate desc"),
        @Index(name = "front_multi_index",columnList ="win_rate desc, position, champion_id" )})
public class SoloMatchFrontEntity {
    @Id
    @Column(name = "solo_match_id")
    private Long soloMatchId;

    @Column(name = "champion_name")
    private String championName;
    @Column(name = "champion_img_url")
    private String championImgUrl;
    @Column(name = "main_rune_img_url")
    private String mainRuneImgUrl;
    @Column(name = "position_img_url")
    private String positionImgUrl;

    @Column(name = "position")
    private String position;
    @Column(name = "champion_id")
    private Long championId;
    @Column(name = "main_rune")
    private Long mainRune;

    @Column(name = "win_rate")
    private Long winRate;
}
