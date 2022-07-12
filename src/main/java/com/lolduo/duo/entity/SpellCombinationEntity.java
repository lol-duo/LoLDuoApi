package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "spell_all")
public class SpellCombinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_spell")
    private SpellEntity firstSpell;
    @ManyToOne
    @JoinColumn(name = "second_spell")
    private SpellEntity secondSpell;

    public SpellCombinationEntity(Long id, SpellEntity firstSpell, SpellEntity secondSpell) {
        this.id = id;
        this.firstSpell = firstSpell;
        this.secondSpell = secondSpell;
    }
}
