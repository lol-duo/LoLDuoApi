package com.lolduo.duo.v2.repository;

import com.lolduo.duo.v2.entity.DoubleMatchEntity;
import com.lolduo.duo.v2.entity.SoloMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoubleMatchRepository extends JpaRepository<DoubleMatchEntity, Long> {
    @Query(value = "select floor(sum(all_count)/10) from double_match",nativeQuery = true)
    Optional<Long> getAllCountSum();
}
