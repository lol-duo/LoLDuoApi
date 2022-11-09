package com.lolduo.duo.v2.repository.detail;


import com.lolduo.duo.v2.entity.detail.SoloMatchDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloMatchDetailRepository extends JpaRepository<SoloMatchDetailEntity,Long> {
    @Query(value = "select floor(sum(all_count)/20) from solo_match_detail",nativeQuery = true)
    Optional<Long> getAllCountSum();

    @Query(value = "select * from solo_match_detail where all_count > ?2 and  solo_comb_id = ?1 order by all_count, win_rate desc,solo_comb_id limit 3",nativeQuery = true)
    List<SoloMatchDetailEntity> findAllBySoloCombIdAndAllCount(Long soloCombId,Long allCount);
}
