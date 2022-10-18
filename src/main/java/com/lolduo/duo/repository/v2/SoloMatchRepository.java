package com.lolduo.duo.repository.v2;

import com.lolduo.duo.object.entity.v2.SoloMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloMatchRepository extends JpaRepository<SoloMatchEntity, Long> {

    @Query(value = "select floor(sum(all_count)/10) from solo_match",nativeQuery = true)
    Optional<Long> getAllCountSum();
    @Query(value = "select * from solo_match where all_count > ?3 and  position like ?1 and champion_id like ?2 order by win_count / all_count DESC limit 100",nativeQuery = true)
    List<SoloMatchEntity> findAllByPositionAndChampionId(String position, String championId,Long allCount);
}
