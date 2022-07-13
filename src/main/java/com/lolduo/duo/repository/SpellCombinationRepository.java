package com.lolduo.duo.repository;

import com.lolduo.duo.entity.SpellCombinationEntity;
import com.lolduo.duo.entity.SpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpellCombinationRepository extends JpaRepository<SpellCombinationEntity, Long> {
    @Query("select sc.id from SpellCombinationEntity sc where (sc.firstSpell = ?1 and sc.secondSpell = ?2) or (sc.firstSpell = ?2 and sc.secondSpell =?1)")
    Long findbycom(SpellEntity firstSpell, SpellEntity secondSpell);
}
