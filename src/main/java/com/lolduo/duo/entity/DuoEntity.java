package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "duo")
public class DuoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean win;
    @Column
    private Long chId1;



    @Column
    private String position1;

    @Column
    private Long perkAll1;
    @Column
    private Long item1_1;
    @Column
    private Long item2_1;
    @Column
    private Long item3_1;
    @Column
    private Long spellAll1;

    @Column
    private Long chId2;



    @Column
    private String position2;

    @Column
    private Long perkAll2;
    @Column
    private Long item1_2;
    @Column
    private Long item2_2;
    @Column
    private Long item3_2;
    @Column
    private Long spellAll2;

    public DuoEntity(Long id, Boolean win, Long chId1, String position1, Long perkAll1, Long item1_1, Long item2_1, Long item3_1, Long spellAll1, Long chId2, String position2, Long perkAll2, Long item1_2, Long item2_2, Long item3_2, Long spellAll2) {
        this.id = id;
        this.win = win;
        this.chId1 = chId1;
        this.position1 = position1;
        this.perkAll1 = perkAll1;
        this.item1_1 = item1_1;
        this.item2_1 = item2_1;
        this.item3_1 = item3_1;
        this.spellAll1 = spellAll1;
        this.chId2 = chId2;
        this.position2 = position2;
        this.perkAll2 = perkAll2;
        this.item1_2 = item1_2;
        this.item2_2 = item2_2;
        this.item3_2 = item3_2;
        this.spellAll2 = spellAll2;
    }
}
