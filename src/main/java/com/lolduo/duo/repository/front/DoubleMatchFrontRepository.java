package com.lolduo.duo.repository.front;

import com.lolduo.duo.entity.DoubleMatchEntity;
import com.lolduo.duo.entity.front.DoubleMatchFrontEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoubleMatchFrontRepository {
    @Query(value = "select * from double_match_front where ((position1 like ?1 and champion_id1 like ?2 and position2 like ?3 and champion_id2 like ?4) or (position1 like ?3 and champion_id1 like ?4 and position2 like ?1 and champion_id2 like ?2)) order by win_rate desc, position1, champion_id1,position2,champion_id2 limit 100",nativeQuery = true)
    List<DoubleMatchFrontEntity> findAllByPositionAndChampionId(String position1, String champion1Id, String position2, String champion2Id);
}
