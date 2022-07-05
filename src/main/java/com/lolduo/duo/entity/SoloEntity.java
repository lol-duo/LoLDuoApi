package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "solo")
public class SoloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long chId;

    @Column
    private Boolean win;

    public SoloEntity(Long id, Long chId, Boolean win) {
        this.id = id;
        this.chId = chId;
        this.win = win;
    }
}
