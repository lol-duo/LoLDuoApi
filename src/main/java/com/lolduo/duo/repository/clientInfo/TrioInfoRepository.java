package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.SoloInfoEntity;
import com.lolduo.duo.entity.clientInfo.TrioInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrioInfoRepository extends JpaRepository<TrioInfoEntity,Long> {
    @Query(value = "select * from trio_info where json_contains(champion_id,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<TrioInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
