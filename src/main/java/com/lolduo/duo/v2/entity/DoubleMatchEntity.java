package com.lolduo.duo.v2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "double_match",indexes = {
        @Index(name="all_count_index",columnList = "all_count"),
        @Index(name="date_index",columnList = "date"),
        @Index(name="multi_index",columnList = "position1,position2, champion_id1,champion_id2,main_rune1,main_rune2",unique = true),
        @Index(name="double_match_index",columnList = "all_count,win_rate desc, position1, champion_id1,position2,champion_id2")})
public class DoubleMatchEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    private LocalDate date;

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

    @Column(name = "all_count")
    private Long allCount;
    @Column(name = "win_count")
    private Long winCount;
    @Column(name = "win_rate")
    private Double winRate;

    @Column(name = "comb_id1")
    private Long combId1;
    @Column(name = "comb_id2")
    private Long combId2;
}
