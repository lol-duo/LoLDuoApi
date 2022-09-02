package com.lolduo.duo.repository.clientInfo.combiSummaryRepository;

import com.lolduo.duo.object.entity.clientInfo.combiSummary.ICombiSummaryEntity;

import java.util.List;

public interface ICombiSummaryRepository {
    List<? extends ICombiSummaryEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiSummaryEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiSummaryEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiSummaryEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
}
