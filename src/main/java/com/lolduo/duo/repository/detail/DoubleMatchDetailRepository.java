package com.lolduo.duo.repository.detail;

import com.lolduo.duo.entity.detail.DoubleMatchDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoubleMatchDetailRepository extends JpaRepository<DoubleMatchDetailEntity,Long> {
    @Query(value = "select floor(sum(all_count)/20) from double_match_detail",nativeQuery = true)
    Optional<Long> getAllCountSum();

    @Query(value = "select * from double_match_detail where all_count > ?3 and  champion_comb_id1 = ?1 and champion_comb_id2 = ?2 order by all_count, win_rate desc, champion_comb_id1, champion_comb_id2 limit 3",nativeQuery = true)
    List<DoubleMatchDetailEntity> findAllBySoloCombIdAndAllCount(Long champion_comb_id1,Long champion_comb_id2, Long allCount);
}
