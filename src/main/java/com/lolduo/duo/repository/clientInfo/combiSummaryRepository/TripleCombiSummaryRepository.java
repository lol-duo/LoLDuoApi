package com.lolduo.duo.repository.clientInfo.combiSummaryRepository;

import com.lolduo.duo.object.entity.clientInfo.combiSummary.TripleCombiSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripleCombiSummaryRepository extends JpaRepository<TripleCombiSummaryEntity,Long>, ICombiSummaryRepository {

    @Query(value = "select id, position, champion_id, win_count_sum, all_count_sum, win_rate from triple_combi_summary where all_count_sum >= ?5 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_rate DESC limit 100",nativeQuery = true)
    List<TripleCombiSummaryEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, win_count_sum, all_count_sum, win_rate from triple_combi_summary where all_count_sum >= ?5 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_rate ASC limit 100",nativeQuery = true)
    List<TripleCombiSummaryEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, win_count_sum, all_count_sum, win_rate from triple_combi_summary where all_count_sum >= ?5 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum DESC limit 100",nativeQuery = true)
    List<TripleCombiSummaryEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, win_count_sum, all_count_sum, win_rate from triple_combi_summary where all_count_sum >= ?5 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum ASC limit 100",nativeQuery = true)
    List<TripleCombiSummaryEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
}