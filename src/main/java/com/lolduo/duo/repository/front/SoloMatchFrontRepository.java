package com.lolduo.duo.repository.front;

import com.lolduo.duo.entity.front.SoloMatchFrontEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SoloMatchFrontRepository extends JpaRepository<SoloMatchFrontEntity,Long> {
    @Query(value = "select * from solo_match_front where position like ?1 and champion_id like ?2 order by win_rate desc, position, champion_id limit 100",nativeQuery = true)
    List<SoloMatchFrontEntity> findAllByPositionAndChampionId(String position, String championId);
}
