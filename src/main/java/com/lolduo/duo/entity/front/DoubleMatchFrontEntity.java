package com.lolduo.duo.entity.front;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "double_match_front",indexes = {
        @Index(name ="win_rate_index", columnList = "win_rate desc"),
        @Index(name="double_match_index",columnList = "win_rate desc, position1, champion_id1, position2, champion_id2")})
public class DoubleMatchFrontEntity{
    @Id
    @Column(name = "double_match_id")
    private Long doubleMatchId;

    @Column(name = "champion_name1")
    private String championName1;
    @Column(name = "champion_name2")
    private String championName2;
    @Column(name = "champion_img_url1")
    private String championImgUrl1;
    @Column(name = "champion_img_url2")
    private String championImgUrl2;
    @Column(name = "main_rune_img_url1")
    private String mainRuneImgUrl1;
    @Column(name = "main_rune_img_url2")
    private String mainRuneImgUrl2;
    @Column(name = "position_img_url1")
    private String positionImgUrl1;
    @Column(name = "position_img_url2")
    private String positionImgUrl2;

    @Column(name = "position1")
    private String position1;
    @Column(name = "position2")
    private String position2;
    @Column(name = "champion_id1")
    private Long championId1;
    @Column(name = "champion_id2")
    private Long championId2;
    @Column(name = "main_rune1")
    private Long mainRune1;
    @Column(name = "main_rune2")
    private Long mainRune2;

    @Column(name = "win_rate")
    private Long winRate;
}