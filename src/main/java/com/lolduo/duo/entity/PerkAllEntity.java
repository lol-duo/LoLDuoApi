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
    private PerkEntity pruneId;

    @ManyToOne
    @JoinColumn(name = "prune1_id")
    private PerkEntity prune1Id;

    @ManyToOne
    @JoinColumn(name = "prune2_id")
    private PerkEntity prune2Id;
    @ManyToOne
    @JoinColumn(name = "prune3_id")
    private PerkEntity prune3Id;
    @ManyToOne
    @JoinColumn(name = "prune4_id")
    private PerkEntity prune4Id;
    @ManyToOne
    @JoinColumn(name = "srune_id")
    private PerkEntity sruneId;
    @ManyToOne
    @JoinColumn(name = "srune1_id")
    private PerkEntity srune1Id;
    @ManyToOne
    @JoinColumn(name = "srune2_id")
    private PerkEntity srune2Id;
    @Column
    private Long defense;
    @Column
    private Long flex;
    @Column
    private Long offense;

    public PerkAllEntity(Long id, PerkEntity pruneId, PerkEntity prune1Id, PerkEntity prune2Id, PerkEntity prune3Id, PerkEntity prune4Id, PerkEntity sruneId, PerkEntity srune1Id, PerkEntity srune2Id, Long defense, Long flex, Long offense) {
        this.id = id;
        this.pruneId = pruneId;
        this.prune1Id = prune1Id;
        this.prune2Id = prune2Id;
        this.prune3Id = prune3Id;
        this.prune4Id = prune4Id;
        this.sruneId = sruneId;
        this.srune1Id = srune1Id;
        this.srune2Id = srune2Id;
        this.defense = defense;
        this.flex = flex;
        this.offense = offense;
    }
}
