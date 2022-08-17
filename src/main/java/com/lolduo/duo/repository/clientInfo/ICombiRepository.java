package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;

import java.util.List;
import java.util.Optional;

public interface ICombiRepository {
    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends ICombiEntity> findByChampionIdAndPosition(String championId, String position);
}
