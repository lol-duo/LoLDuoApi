package com.lolduo.duo.repository;

import com.lolduo.duo.entity.MatchIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchIdRepository extends JpaRepository<MatchIdEntity, Long> {
    List<MatchIdEntity> findAllByStartTime(String startTime);
}
