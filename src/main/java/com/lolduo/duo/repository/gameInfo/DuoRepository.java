package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.DuoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface DuoRepository extends JpaRepository<DuoEntity, Long> {
    @Query(value = "SELECT * FROM duo i WHERE i.champion =?1 and i.position =?2",nativeQuery = true)
    List<SoloRepository> findAllByChampionAndPosition(TreeSet<Long> champion, Map<Long, String> position);
    @Query(value = "SELECT * FROM duo i WHERE i.champion =?1 and i.position =?2 and i.win =true",nativeQuery = true)
    List<SoloRepository> findAllByChampionAndPositionAndWinTrue(TreeSet<Long> champion,Map<Long, String> position);
}
