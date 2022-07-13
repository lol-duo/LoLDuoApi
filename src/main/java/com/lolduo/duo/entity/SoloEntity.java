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

    @Column
    private String position;

    @Column
    private Long perkAll;
    @Column
    private Long item1;
    @Column
    private Long item2;
    @Column
    private Long item3;
    @Column
    private Long spellAll;

    public SoloEntity(Long id, Long chId, Boolean win, String position, Long perkAll, Long item1, Long item2, Long item3, Long spellAll) {
        this.id = id;
        this.chId = chId;
        this.win = win;
        this.position = position;
        this.perkAll = perkAll;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.spellAll = spellAll;
    }
}
