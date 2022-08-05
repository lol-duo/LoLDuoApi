package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.ICombinationInfoEntity;
import java.util.Optional;

public interface ICombinationInfoRepository {
    Optional<? extends ICombinationInfoEntity> findByChampionIdAndPosition(String championId, String position);
}
