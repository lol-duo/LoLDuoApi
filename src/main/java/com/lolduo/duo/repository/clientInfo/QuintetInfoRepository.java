package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.QuintetInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuintetInfoRepository extends JpaRepository<QuintetInfoEntity,Long>, ICombinationInfoRepository {
    @Query(value = "select * from quintet_info where json_contains(champion_id,?1) and json_contains(position,?2) order by win_count / all_count DESC limit 100",nativeQuery = true)
    Optional<List<QuintetInfoEntity>> findAllByChampionIdAndPositionDesc(String championId, String position);

    @Query(value = "select * from quintet_info where json_contains(champion_id,?1) and json_contains(position,?2) order by win_count / all_count ASC limit 100",nativeQuery = true)
    Optional<List<QuintetInfoEntity>> findAllByChampionIdAndPositionAsc(String championId, String position);

    @Query(value = "select * from quintet_info where json_contains(champion_id,?1) and json_contains(position,?2)",nativeQuery = true)
    Optional<QuintetInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
