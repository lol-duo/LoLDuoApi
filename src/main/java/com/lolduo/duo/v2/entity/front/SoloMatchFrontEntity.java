package com.lolduo.duo.v2.entity.front;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "solo_match_front",indexes = {@Index(name = "front_multi_index",columnList ="win_rate desc, position, champion_id" )})
public class SoloMatchFrontEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "position")
    private String position;
    @Column(name = "champion_id")
    private Long championId;
    @Column(name = "main_rune")
    private Long mainRune;
    @Column(name = "win_rate")
    private Double winRate;
    @Column(name = "solo_match_id")
    private Long soloMatchId;
}
