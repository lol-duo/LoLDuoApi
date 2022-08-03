package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.DuoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public interface DuoRepository extends JpaRepository<DuoEntity, Long> {
    @Query(value = "select * from duo where json_contains(champion,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<DuoEntity> findByChampionandpAndPosition(String championId, String position);
}
