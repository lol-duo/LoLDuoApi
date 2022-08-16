package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.ICombinationInfoEntity;

import java.util.List;
import java.util.Optional;

public interface ICombinationInfoRepository {
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends ICombinationInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
