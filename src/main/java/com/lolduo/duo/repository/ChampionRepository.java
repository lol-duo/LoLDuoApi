package com.lolduo.duo.repository;

import com.lolduo.duo.object.entity.ChampionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionRepository extends JpaRepository<ChampionEntity, Long> {
}
