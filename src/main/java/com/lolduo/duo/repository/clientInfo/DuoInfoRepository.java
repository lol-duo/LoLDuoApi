package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.DuoInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DuoInfoRepository extends JpaRepository<DuoInfoEntity,Long> {

    @Query(value = "select * from duo_info where json_contains(champion_id,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<DuoInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
