package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.DoubleCombiEntity;
import com.lolduo.duo.object.entity.clientInfo.PentaCombiEntity;
import com.lolduo.duo.object.entity.clientInfo.TripleCombiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PentaCombiRepository extends JpaRepository<PentaCombiEntity,Long>, ICombiRepository {
    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from penta_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count_sum / all_count_sum DESC limit 100",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from penta_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count_sum / all_count_sum ASC limit 100",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from penta_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum DESC limit 100",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, win_count_sum as win_count, all_count_sum as all_count from (select *, sum(win_count) as win_count_sum, sum(all_count) as all_count_sum from penta_combi group by position having all_count_sum >= ?5) t1 where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count_sum ASC limit 100",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    @Query(value = "select * from penta_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count DESC limit 30",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from penta_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count ASC limit 30",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from penta_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count DESC limit 30",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from penta_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count ASC limit 30",nativeQuery = true)
    List<PentaCombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from penta_combi where json_contains(champion_id,?1) and json_contains(position,?2) limit 1",nativeQuery = true)
    Optional<PentaCombiEntity> findByChampionIdAndPosition(String championId, String position);
    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from penta_combi where json_contains(position,?1) group by position limit 1",nativeQuery = true)
    Optional<PentaCombiEntity> findAllCountAndWinCountByChampionPosition(String position);
    @Query(value = "select * from penta_combi where all_count >= ?2 and json_contains(position,?1) and perk_myth_item not like '%|0%' order by win_count / all_count DESC limit 1",nativeQuery = true)
    Optional<PentaCombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc(String position, Long minAllCount);

    @Query(value = "select * from double_combi where all_count >= ?2 and json_contains(position,?1) and perk_myth_item not like '%|0%' order by all_count DESC limit 1",nativeQuery = true)
    Optional<PentaCombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc2(String position, Long minAllCount);
}
