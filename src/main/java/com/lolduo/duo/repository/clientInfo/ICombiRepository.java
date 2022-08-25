package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;

import java.util.List;
import java.util.Optional;

public interface ICombiRepository {
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    Optional<? extends ICombiEntity> findByChampionIdAndPosition(String championId, String position);
    //ChampionDetail2
    Optional<? extends ICombiEntity> findAllCountAndWinCountByChampionPosition(String position);
    Optional<? extends ICombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc(String position);
    Optional<? extends ICombiEntity> findByPerkAndMythItemAndPositionAndAllCountDesc(String position,Long allCount);
}
