package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.object.entity.clientInfo.DoubleCombiEntity;
import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICombiRepository {
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList, int minAllCount);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGroupByPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList, int minAllCount);

    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionWinRateAsc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionGameCountAsc(String championId, String position, String positionList, String excludePositionList);

    Optional<? extends ICombiEntity> findByChampionIdAndPosition(String championId, String position);
    //ChampionDetail2
    Optional<? extends ICombiEntity> findAllCountAndWinCountByChampionPosition(String position);
    Optional<? extends ICombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc(String position, Long minAllCount);

    //all_count 내림차순으로 제일 경기 많은거 가져오는 것, 추후에 해결하면 지울 것, 임시 함수
     Optional<? extends ICombiEntity> findByPerkAndMythItemAndPositionAndWinRateDesc2(String position, Long minAllCount);
}
