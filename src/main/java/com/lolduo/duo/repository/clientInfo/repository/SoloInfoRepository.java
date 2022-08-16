package com.lolduo.duo.repository.clientInfo.repository;

import com.lolduo.duo.object.entity.clientInfo.entity.SoloInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloInfoRepository extends JpaRepository<SoloInfoEntity,Long> {

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 and position = ?2 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionWinRateDesc(Long championId, String position);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 and position = ?2 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionWinRateAsc(Long championId, String position);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 and position = ?2 order by all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionGameCountDesc(Long championId, String position);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 and position = ?2 order by all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionGameCountAsc(Long championId, String position);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdWinRateDesc(Long championId);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdWinRateAsc(Long championId);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 order by all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdGameCountDesc(Long championId);

    @Query(value = "select * from solo_info where all_count >= 10 and champion_id = ?1 order by all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdGameCountAsc(Long championId);

    @Query(value = "select * from solo_info where all_count >= 10 and position = ?1 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionWinRateDesc(String position);

    @Query(value = "select * from solo_info where all_count >= 10 and position = ?1 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionWinRateAsc(String position);

    @Query(value = "select * from solo_info where all_count >= 10 and position = ?1 order by all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionGameCountDesc(String position);

    @Query(value = "select * from solo_info where all_count >= 10 and position = ?1 order by all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionGameCountAsc(String position);

    @Query(value = "select * from solo_info where all_count >= 10 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllWinRateDesc();

    @Query(value = "select * from solo_info where all_count >= 10 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllWinRateAsc();

    @Query(value = "select * from solo_info where all_count >= 10 order by all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllGameCountDesc();

    @Query(value = "select * from solo_info where all_count >= 10 order by all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllGameCountAsc();

    Optional<SoloInfoEntity> findByChampionIdAndPosition(Long championId, String position);
}
