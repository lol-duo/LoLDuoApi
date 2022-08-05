package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.ICombinationInfoEntity;
import com.lolduo.duo.entity.clientInfo.QuintetInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuintetInfoRepository extends JpaRepository<QuintetInfoEntity,Long>, ICombinationInfoRepository {
    @Query(value = "select * from quintet_info where json_contains(champion_id,?1) and json_contains(position,?2)",nativeQuery = true)
    Optional<QuintetInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
