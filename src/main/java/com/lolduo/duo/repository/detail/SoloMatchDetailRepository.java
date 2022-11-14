package com.lolduo.duo.repository.detail;


import com.lolduo.duo.entity.detail.SoloMatchDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloMatchDetailRepository extends JpaRepository<SoloMatchDetailEntity,Long> {
    @Query(value = "select floor(sum(all_count)/20) from solo_match_detail",nativeQuery = true)
    Optional<Long> getAllCountSum();

    @Query(value = "select * from solo_match_detail where all_count > ?2 and  solo_comb_id = ?1 order by win_rate desc limit 3",nativeQuery = true)
    List<SoloMatchDetailEntity> findAllBySoloCombIdAndAllCount(Long soloCombId,Long allCount);
}
