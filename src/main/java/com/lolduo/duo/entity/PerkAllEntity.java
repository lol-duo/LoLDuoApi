package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "perk_all")
public class PerkAllEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prune_id")
    private PerkEntity prune_id;

    @ManyToOne
    @JoinColumn(name = "prune1_id")
    private PerkEntity prune1_id;

    @ManyToOne
    @JoinColumn(name = "prune2_id")
    private PerkEntity prune2_id;
    @ManyToOne
    @JoinColumn(name = "prune3_id")
    private PerkEntity prune3_id;
    @ManyToOne
    @JoinColumn(name = "prune4_id")
    private PerkEntity prune4_id;
    @ManyToOne
    @JoinColumn(name = "srune_id")
    private PerkEntity srune_id;
    @ManyToOne
    @JoinColumn(name = "srune1_id")
    private PerkEntity srune1_id;
    @ManyToOne
    @JoinColumn(name = "srune2_id")
    private PerkEntity srune2_id;
    @Column
    private Long defense;
    @Column
    private Long flex;
    @Column
    private Long offense;

    public PerkAllEntity(Long id, PerkEntity prune_id, PerkEntity prune1_id, PerkEntity prune2_id, PerkEntity prune3_id, PerkEntity prune4_id, PerkEntity srune_id, PerkEntity srune1_id, PerkEntity srune2_id, Long defense, Long flex, Long offense) {
        this.id = id;
        this.prune_id = prune_id;
        this.prune1_id = prune1_id;
        this.prune2_id = prune2_id;
        this.prune3_id = prune3_id;
        this.prune4_id = prune4_id;
        this.srune_id = srune_id;
        this.srune1_id = srune1_id;
        this.srune2_id = srune2_id;
        this.defense = defense;
        this.flex = flex;
        this.offense = offense;
    }
}
