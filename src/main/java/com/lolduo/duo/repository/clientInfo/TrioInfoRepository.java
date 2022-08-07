package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.TrioInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrioInfoRepository extends JpaRepository<TrioInfoEntity,Long>, ICombinationInfoRepository {
    @Query(value = "select * from trio_info where json_contains(champion_id,?1) and json_contains(position,?2) order by win_count / all_count DESC limit 100",nativeQuery = true)
    Optional<List<TrioInfoEntity>> findAllByChampionIdAndPositionDesc(String championId, String position);

    @Query(value = "select * from trio_info where json_contains(champion_id,?1) and json_contains(position,?2) order by win_count / all_count ASC limit 100",nativeQuery = true)
    Optional<List<TrioInfoEntity>> findAllByChampionIdAndPositionAsc(String championId, String position);

    @Query(value = "select * from trio_info where json_contains(champion_id,?1) and json_contains(position,?2)",nativeQuery = true)
    Optional<TrioInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
