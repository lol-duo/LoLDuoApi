package com.lolduo.duo.entity.front;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "double_match_front",indexes = {
        @Index(name="double_match_index",columnList = "win_rate desc, position1, champion_id1,position2,champion_id2")})
public class DoubleMatchFrontEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    private Double winRate;

    @Column(name = "comb_id1")
    private Long combId1;
    @Column(name = "comb_id2")
    private Long combId2;
}