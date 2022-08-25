package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.DoubleCombiEntity;
import com.lolduo.duo.object.entity.clientInfo.SoloCombiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoubleCombiRepository extends JpaRepository<DoubleCombiEntity,Long>, ICombiRepository {

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from double_combi where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) group by position having all_count >= 10 order by sum(win_count) / sum(all_count) DESC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from double_combi where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) group by position having all_count >= 10 order by sum(win_count) / sum(all_count) ASC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from double_combi where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) group by position having all_count >= 10 order by sum(all_count) DESC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from double_combi where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) group by position having all_count >= 10 order by sum(all_count) ASC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from double_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count DESC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from double_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count ASC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from double_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count DESC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from double_combi where all_count >= 10 and json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by all_count ASC limit 30",nativeQuery = true)
    List<DoubleCombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from double_combi where json_contains(champion_id,?1) and json_contains(position,?2) limit 1",nativeQuery = true)
    Optional<DoubleCombiEntity> findByChampionIdAndPosition(String championId, String position);

    @Query(value = "select id, position, champion_id, item_list, perk_list, spell_list, perk_myth_item, sum(win_count) as win_count, sum(all_count) as all_count from double_combi where json_contains(position,?1) group by position limit 1",nativeQuery = true)
    Optional<DoubleCombiEntity> findAllCountAndWinCountByChampionPosition(String position);

    @Query(value = "select * from double_combi where json_contains(position,?1) and perk_myth_item not like '%|0%' order by win_count / all_count DESC limit 1",nativeQuery = true)
    Optional<DoubleCombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc(String position);

    @Query(value = "select * from double_combi where json_contains(position,?1) and perk_myth_item not like '%|0%' and all_count > ?2 order by win_count / all_count DESC limit 1",nativeQuery = true)
    Optional<DoubleCombiEntity> findByPerkAndMythItemAndPositionAndAllCountDesc(String position,Long allCount);
}
