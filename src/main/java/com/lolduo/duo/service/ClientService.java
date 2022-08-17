package com.lolduo.duo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.dto.client.CombiSearchDTO;
import com.lolduo.duo.object.response.ChampionInfoList;
import com.lolduo.duo.object.response.championDetail.ChampionDetail;
import com.lolduo.duo.object.response.championDetail.ResponseItem;
import com.lolduo.duo.object.response.championDetail.ResponsePerk;
import com.lolduo.duo.object.response.championDetail.ResponseSpell;
import com.lolduo.duo.object.response.getChampionList.Champion;
import com.lolduo.duo.object.response.sub.ChampionInfoResponse;
import com.lolduo.duo.object.entity.initialInfo.ChampionEntity;
import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;
import com.lolduo.duo.repository.initialInfo.ChampionRepository;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.clientInfo.DoubleCombiRepository;
import com.lolduo.duo.repository.clientInfo.PentaCombiRepository;
import com.lolduo.duo.repository.clientInfo.SoloCombiRepository;
import com.lolduo.duo.repository.clientInfo.TripleCombiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final SoloCombiRepository soloCombiRepository;
    private final DoubleCombiRepository doubleCombiRepository;
    private final TripleCombiRepository tripleCombiRepository;
    private final PentaCombiRepository pentaCombiRepository;
    private final ChampionRepository championRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChampionDetailComponent championDetailComponent;


    public ResponseEntity<?> getChampionList(){
        List<ChampionEntity> championEntityList = new ArrayList<>(championRepository.findAll());
        List<Champion> championList = new ArrayList<>();
        for(ChampionEntity championEntity : championEntityList){
            String engName = championEntity.getImgUrl().substring(0,championEntity.getImgUrl().length()-4);
            Champion temp = new Champion(championEntity.getId(),championEntity.getName(),engName,
                    "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/"+championEntity.getImgUrl());
            championList.add(temp);
        }
        Collections.sort(championList);
        return new ResponseEntity<>(championList, HttpStatus.OK);
    }
    private ChampionInfoResponse championInfoDTO2Response(ChampionInfoDTO championInfoDTO){  // 챔피언 이름, 이미지 URL, 포지션 가져옴
        ChampionEntity champion = championRepository.findById(championInfoDTO.getChampionId()).orElse(new ChampionEntity(0L,"ALL","ALL.png"));
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/";
        String positionbaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/line/";
        return new ChampionInfoResponse(championInfoDTO.getChampionId(), champion.getName(), baseUrl+champion.getImgUrl(), championInfoDTO.getPosition(), positionbaseUrl + championInfoDTO.getPosition() + ".png");
    }
    public ResponseEntity<?> getChampionDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        log.info("getChampionDetail - 조합 상세 정보 확인.");
        championInfoDTOList.forEach(championInfoDTO ->
            log.info("getChampionDetail - champion : {}, position : {}", championInfoDTO.getChampionId(), championInfoDTO.getPosition())
        );

        if(championInfoDTOList == null) {
            log.info("요청정보 자체가 null입니다. 잘못된 요청입니다. 404 리턴");
            return new ResponseEntity<>("요청정보 자체가 null입니다. 잘못된 요청입니다.",HttpStatus.BAD_REQUEST);
        }
        for (ChampionInfoDTO championInfoDTO : championInfoDTOList) {
            if(championInfoDTO.getPosition()==null){
                log.info("포지션 정보가 null입니다. 잘못된 요청입니다. 404 리턴");
                return new ResponseEntity<>("포지션 정보가 null 입니다. 잘못된 요청입니다.",HttpStatus.BAD_REQUEST);

            }
            if (championInfoDTO.getPosition().equals("ALL") || championInfoDTO.getChampionId() == 0) {
                log.info("포지션에 ALL이나, 챔피언 ID에 0이 있습니다. 잘못된 요청입니다. 404 리턴");
                return new ResponseEntity<>("포지션에 ALL이나, 챔피언 ID에 0이 있습니다. 잘못된 요청입니다.",HttpStatus.BAD_REQUEST);
            }
        }
        log.info("getChampionDetail : perkInfo start");
        List<ResponsePerk> perkInfo = championDetailComponent.editPerkDetail(championDetailComponent.getPerkDetail(championInfoDTOList, true), championInfoDTOList);
        log.info("getChampionDetail : spellInfo start");
        List<ResponseSpell> spellInfo = championDetailComponent.editSpellDetail(championDetailComponent.getSpellDetail(championInfoDTOList), championInfoDTOList) ;
        log.info("getChampionDetail : itemInfo start");
        List<ResponseItem> itemInfo = championDetailComponent.editItemDetail(championDetailComponent.getItemDetail(championInfoDTOList, true), championInfoDTOList);

        log.info("정상처리, 200 리턴");
        return new ResponseEntity<>(new ChampionDetail(perkInfo,spellInfo,itemInfo),HttpStatus.OK);
    }
    public ResponseEntity<?> getChampionInfoList(CombiSearchDTO combiSearchDTO){
        log.info("getChampionInfoList - 챔피언 조합 검색. winRateAsc: {}, gameCountAsc: {}", combiSearchDTO.getWinRateAsc(), combiSearchDTO.getGameCountAsc());
        List<ChampionInfoList> result = new ArrayList<>();
        ICombiRepository combiRepository;
        int championCount = combiSearchDTO.getChampionInfoDTOList().size();

        combiRepository = getCombiRepository(championCount);
        if(combiRepository == null)
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

            List<? extends ICombiEntity> infoEntityList;
            if (combiSearchDTO.getWinRateAsc() != null) {
                if (combiSearchDTO.getWinRateAsc())
                    infoEntityList = combiRepository.findAllByChampionIdAndPositionWinRateAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList));
                else
                    infoEntityList = combiRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList));
            }
            else if (combiSearchDTO.getGameCountAsc() != null) {
                if (combiSearchDTO.getGameCountAsc())
                    infoEntityList = combiRepository.findAllByChampionIdAndPositionGameCountAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList));
                else
                    infoEntityList = combiRepository.findAllByChampionIdAndPositionGameCountDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList));
            }
            else
                infoEntityList = combiRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList));
             log.info("getChampionInfoList() - 매치 데이터 검색.\n지정된 championId = {}\n지정된 position = {}\n실제 검색 position = {}\n선택한 챔피언들에게 금지된 position = {}", objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(excludePositionList));
             log.info("getChampionInfoList() - 시간 측정 : DB 검색 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

            putCombiInfoToResult(result, infoEntityList, combiSearchDTO.getChampionInfoDTOList(), selectedPositionOrderMap, selectedChampionOrderMap, allChampAllPositionQueue);
            log.info("getChampionInfoList() - 시간 측정 : 검색 결과 정렬해 결과 리스트에 저장 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        } catch (JsonProcessingException e) {
            log.error("objectMapper writeValue error");
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.OK);
        }
        log.info("getChampionInfoList() - 시간 측정 : API 처리 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    private ICombiRepository getCombiRepository(int championCount) {
        if (championCount == 1) {
            log.info("getCombiRepository() - championCount : {}, 1명", championCount);
            return soloCombiRepository;
        }
        else if (championCount == 2) {
            log.info("getCombiRepository() - championCount : {}, 2명", championCount);
            return doubleCombiRepository;
        }
        else if (championCount == 3) {
            log.info("getCombiRepository() - championCount : {}, 3명", championCount);
            return tripleCombiRepository;
        }
        else if (championCount == 5) {
            log.info("getCombiRepository() - championCount : {}, 5명", championCount);
            return pentaCombiRepository;
        }
        else {
            log.info("getCombiRepository() - 요청 챔피언 개수가 올바르지 않습니다. (1, 2, 3, 5명만 가능)");
            return null;
        }
    }

    private void initChampAndPositionInfoObject(List<ChampionInfoDTO> championInfoDTOList, Map<Long, String> champPositionMap, List<String> excludePositionList, Map<Long, Long> selectedChampionOrderMap, Map<String, Long> selectedPositionOrderMap) {
        Long inputOrder = 0L;
        // 입력된 챔피언 각각에 대해 ?인지, 그리고 ALL포지션인지 확인하여 포지션, 챔피언 목록과 관련된객체를 채워 넣는다.
        for (ChampionInfoDTO championInfoDTO : championInfoDTOList) {
            if (championInfoDTO.getChampionId() == 0) {
                if (!championInfoDTO.getPosition().equals("ALL")) {
                    selectedPositionOrderMap.put(championInfoDTO.getPosition(), inputOrder);
                    excludePositionList.add(championInfoDTO.getPosition());
                }
            }
            else {
                selectedChampionOrderMap.put(championInfoDTO.getChampionId(), inputOrder);
                if (!championInfoDTO.getPosition().equals("ALL")) {
                    champPositionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
                    selectedPositionOrderMap.put(championInfoDTO.getPosition(), inputOrder);
                }
            }
            inputOrder++;
        }
        log.info("initChampAndPositionInfoObject - 챔피언/포지션 정보 저장 결과\nchampPositionMap: {}\nexcludePositionList: {}\nselectedChampionOrderMap: {}\nselectedPositionOrderMap: {}",
                champPositionMap, excludePositionList, selectedChampionOrderMap, selectedPositionOrderMap);
    }

    private void putCombiInfoToResult(List<ChampionInfoList> result, List<? extends ICombiEntity> infoEntityList, List<ChampionInfoDTO> championInfoDTOList, Map<String, Long> selectedPositionOrderMap, Map<Long, Long> selectedChampionOrderMap, Queue<ChampionInfoResponse> allChampAllPositionQueue) {
        if (infoEntityList != null && !infoEntityList.isEmpty()) {
            log.info("putCombiInfoToResult() - 조합 정보.");
            infoEntityList.forEach(infoEntity -> {
                log.info("putCombiInfoToResult() - 검색 결과 : championId = {}, position = {}, AllCount = {}, WinCount = {}", infoEntity.getChampionId().toString(), infoEntity.getPosition().toString(), infoEntity.getAllCount(), infoEntity.getWinCount());
                result.add(new ChampionInfoList(
                                createChampionInfoResponseList(infoEntity.getChampionId().size(), infoEntity, selectedPositionOrderMap, selectedChampionOrderMap, allChampAllPositionQueue),
                                String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount())),
                        String.valueOf(infoEntity.getAllCount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                        )
                );
            });
        }
        else {
            log.info("putCombiInfoToResult() - 조합 정보.\n해당하는 데이터 행이 존재하지 않습니다.");
            List<ChampionInfoResponse> championInfoResponseList = new ArrayList<>();
            championInfoDTOList.forEach(championInfoDTO ->
                    championInfoResponseList.add(championInfoDTO2Response(championInfoDTO))
            );
            result.add(new ChampionInfoList(championInfoResponseList, "데이터가 존재하지 않습니다.","0 게임"));
        }
    }

    private List<ChampionInfoResponse> createChampionInfoResponseList(int championCount, ICombiEntity infoEntity, Map<String, Long> selectedPositionOrderMap, Map<Long, Long> selectedChampionOrderMap, Queue<ChampionInfoResponse> allChampAllPositionQueue) {
        ChampionInfoResponse[] championInfoResponseArray = new ChampionInfoResponse[championCount];

        for (Map.Entry<Long, String> positionEntry : infoEntity.getPosition().entrySet()) {
            Long order = -1L;
            if (selectedPositionOrderMap.containsKey(positionEntry.getValue()))
                order = selectedPositionOrderMap.get(positionEntry.getValue());
            else if (selectedChampionOrderMap.containsKey(positionEntry.getKey()))
                order = selectedChampionOrderMap.get(positionEntry.getKey());

            if (order != -1L)
                championInfoResponseArray[order.intValue()] = championInfoDTO2Response(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue()));
            else
                allChampAllPositionQueue.offer(championInfoDTO2Response(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue())));
        }

        log.info("createChampionInfoResponseList() - 반환 리스트 원소 값");
        for (int i = 0; i < championInfoResponseArray.length; i++) {
            if (championInfoResponseArray[i] == null)
                championInfoResponseArray[i] = allChampAllPositionQueue.poll();

            try {
                log.info("createChampionInfoResponseList() - {}번째 원소 : {}={}", i + 1, championInfoResponseArray[i].getChampionName(), championInfoResponseArray[i].getPosition());
            }
            catch (NullPointerException e) {
                log.info("createChampionInfoResponseList() - 오류 발생. 챔피언 이름이 null입니다.");
            }
        }
        return Arrays.asList(championInfoResponseArray);
    }

}
