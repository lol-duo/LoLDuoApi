package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.DoubleCombiEntity;
import com.lolduo.duo.object.entity.clientInfo.TripleCombiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripleCombiRepository extends JpaRepository<TripleCombiEntity,Long>, ICombiRepository {

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from triple_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count_sum / all_count_sum DESC limit 100",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from triple_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count_sum / all_count_sum ASC limit 100",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from triple_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum DESC limit 100",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from triple_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum ASC limit 100",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select * from triple_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count DESC limit 30",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from triple_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count ASC limit 30",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from triple_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count DESC limit 30",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from triple_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count ASC limit 30",nativeQuery = true)
    List<TripleCombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from triple_combi where json_contains(champion_id,?1) and json_contains(position,?2) limit 1",nativeQuery = true)
    Optional<TripleCombiEntity> findByChampionIdAndPosition(String championId, String position);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from triple_combi where json_contains(position,?1) group by position limit 1",nativeQuery = true)
    Optional<TripleCombiEntity> findAllCountAndWinCountByChampionPosition(String position);

    @Query(value = "select * from triple_combi where all_count >= ?2 and json_contains(position,?1) and perk_myth_item not like '%|0%' order by win_count / all_count DESC limit 1",nativeQuery = true)
    Optional<TripleCombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc(String position, Long minAllCount);

    @Query(value = "select * from double_combi where all_count >= ?2 and json_contains(position,?1) and perk_myth_item not like '%|0%' order by  all_count DESC limit 1",nativeQuery = true)
    Optional<TripleCombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc2(String position, Long minAllCount);
}