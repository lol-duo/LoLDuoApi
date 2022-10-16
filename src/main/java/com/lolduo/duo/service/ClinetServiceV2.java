package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lolduo.duo.object.dto.client.CombiSearchDTO;
import com.lolduo.duo.object.dto.client.CombiSearchV2DTO;
import com.lolduo.duo.object.entity.clientInfo.combiSummary.ICombiSummaryEntity;
import com.lolduo.duo.object.response.ChampionInfoList;
import com.lolduo.duo.object.response.sub.ChampionInfoResponse;
import com.lolduo.duo.object.response.v2.SoloResponseV2;
import com.lolduo.duo.repository.clientInfo.combiSummaryRepository.ICombiSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class ClinetServiceV2 {

    public ResponseEntity<?> getSoloChampionInfoList(CombiSearchV2DTO combiSearchV2DTO){
        log.info("v2/getChampionInfoList - 챔피언 조합 검색. championName : {}, position : {}",combiSearchV2DTO.getChampionName(),combiSearchV2DTO.getPosition());
        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        if(combiSearchV2DTO == null || combiSearchV2DTO.getChampionName() == null || combiSearchV2DTO.getPosition() == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.OK);
        }
        String position = combiSearchV2DTO.getPosition();
        String champion =
        return new ResponseEntity<>( HttpStatus.OK);
    }

    public ResponseEntity<?> getChampionInfoList(CombiSearchDTO combiSearchDTO){
        List<ChampionInfoList> result = new ArrayList<>();
        ICombiSummaryRepository combiSummaryRepository;
        int championCount = combiSearchDTO.getChampionInfoDTOList().size();

        combiSummaryRepository = getCombiSummaryRepository(championCount);
        if(combiSummaryRepository == null)
            return new ResponseEntity<>("요청한 챔피언 개수가 올바르지 않습니다. (1, 2, 3, 5만 가능)",HttpStatus.BAD_REQUEST);

        Map<Long, String> champPositionMap = new HashMap<>();
        List<String> excludePositionList = new ArrayList<>(5);

        Map<Long, Long> selectedChampionOrderMap = new HashMap<>();
        Map<String, Long> selectedPositionOrderMap = new HashMap<>();
        Queue<ChampionInfoResponse> allChampAllPositionQueue = new LinkedList<>();

        log.info("getChampionInfoList() - 시간 측정 : 선택 포지션/챔피언 정보 관련 객체 생성 시작  {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        initChampAndPositionInfoObject(combiSearchDTO.getChampionInfoDTOList(), champPositionMap, excludePositionList, selectedChampionOrderMap, selectedPositionOrderMap);
        log.info("getChampionInfoList() - 시간 측정 : 선택 포지션/챔피언 정보 관련 객체 생성 끝  {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

        try {
            log.info("getChampionInfoList() - 시간 측정 : DB 검색 시작 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

            List<? extends ICombiSummaryEntity> combiSummaryEntityList;
            int minAllCountForSearch;
            if (championCount == 1 || championCount == 2)
                minAllCountForSearch = 100;
            else if (championCount == 3)
                minAllCountForSearch = 20;
            else
                minAllCountForSearch = 3;

            if (combiSearchDTO.getWinRateAsc() != null) {
                if (combiSearchDTO.getWinRateAsc())
                    combiSummaryEntityList = combiSummaryRepository.findAllByChampionIdAndPositionWinRateAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList), minAllCountForSearch);
                else
                    combiSummaryEntityList = combiSummaryRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList), minAllCountForSearch);
            }
            else if (combiSearchDTO.getGameCountAsc() != null) {
                if (combiSearchDTO.getGameCountAsc())
                    combiSummaryEntityList = combiSummaryRepository.findAllByChampionIdAndPositionGameCountAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList), minAllCountForSearch);
                else
                    combiSummaryEntityList = combiSummaryRepository.findAllByChampionIdAndPositionGameCountDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList), minAllCountForSearch);
            }
            else
                combiSummaryEntityList = combiSummaryRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList), minAllCountForSearch);
            log.info("getChampionInfoList() - 매치 데이터 검색.\n지정된 championId = {}\n지정된 position = {}\n실제 검색 position = {}\n선택한 챔피언들에게 금지된 position = {}", objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(excludePositionList));
            log.info("getChampionInfoList() - 시간 측정 : DB 검색 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

            putCombiInfoToResult(result, combiSummaryEntityList, combiSearchDTO.getChampionInfoDTOList(), selectedPositionOrderMap, selectedChampionOrderMap, allChampAllPositionQueue);
            log.info("getChampionInfoList() - 시간 측정 : 검색 결과 정렬해 결과 리스트에 저장 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        } catch (JsonProcessingException e) {
            log.error("objectMapper writeValue error");
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.OK);
        }
        log.info("getChampionInfoList() - 시간 측정 : API 처리 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
