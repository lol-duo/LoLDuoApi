package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.SoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SoloRepository extends JpaRepository<SoloEntity, Long> {
    List<SoloEntity> findAllByChampionAndPosition(Long champion,String position);
    List<SoloEntity> findAllByChampionAndPositionAndWinTrue(Long champion,String position);
}
