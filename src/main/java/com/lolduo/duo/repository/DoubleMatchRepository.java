package com.lolduo.duo.repository;

import com.lolduo.duo.entity.DoubleMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoubleMatchRepository extends JpaRepository<DoubleMatchEntity, Long> {
    @Query(value = "select floor(sum(all_count)/20) from double_match",nativeQuery = true)
    Optional<Long> getAllCountSum();

    @Query(value = "select * from double_match where all_count > ?5 and ((position1 like ?1 and champion_id1 like ?2 and position2 like ?3 and champion_id2 like ?4) or (position1 like ?3 and champion_id1 like ?4 and position2 like ?1 and champion_id2 like ?2)) order by win_rate desc, position1, champion_id1,position2,champion_id2 limit 100",nativeQuery = true)
    List<DoubleMatchEntity> findAllByPositionAndChampionId(String position1, String champion1Id,String position2, String champion2Id, Long allCount);
}
