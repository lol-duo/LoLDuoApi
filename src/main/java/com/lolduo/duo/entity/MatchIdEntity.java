package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "match_a")
public class MatchIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String matchId;

    public MatchIdEntity(Long id, String matchId) {
        this.id = id;
        this.matchId = matchId;
    }
}
