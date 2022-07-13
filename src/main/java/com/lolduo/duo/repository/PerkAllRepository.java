package com.lolduo.duo.repository;

import com.lolduo.duo.entity.PerkAllEntity;
import com.lolduo.duo.entity.PerkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerkAllRepository extends JpaRepository<PerkAllEntity, Long> {

    @Query("select pa from PerkAllEntity pa where pa.pruneId = ?1 and pa.prune1Id = ?2 and pa.prune2Id = ?3 and pa.prune3Id = ?4 and pa.prune4Id = ?5 and pa.sruneId = ?6 and pa.srune1Id = ?7 and pa.srune2Id = ?8 and pa.defense = ?9 and pa.flex = ?10 and pa.offense = ?11")
    PerkAllEntity findByAll(PerkEntity pruneId, PerkEntity prune1Id, PerkEntity prune2Id, PerkEntity prune3Id, PerkEntity prune4Id, PerkEntity sruneId, PerkEntity srune1Id, PerkEntity srune2Id, Long defense, Long flex, Long offense);
}
