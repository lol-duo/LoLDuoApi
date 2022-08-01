package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.SoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SoloRepository extends JpaRepository<SoloEntity, Long> {
    //@Query(value = "SELECT * FROM solo i WHERE i.champion =?1 and i.position =?2",nativeQuery = true)
    List<SoloEntity> findAllByChampionAndPosition(Long champion,String position);

    //@Query(value = "SELECT * FROM solo i WHERE i.champion =?1 and i.position =?2 and i.win =true",nativeQuery = true)
    List<SoloEntity> findAllByChampionAndPositionAndWinTrue(Long champion,String position);
}
