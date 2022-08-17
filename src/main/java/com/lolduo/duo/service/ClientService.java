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
import com.lolduo.duo.object.response.sub.ChampionInfo;
import com.lolduo.duo.object.entity.ChampionEntity;
import com.lolduo.duo.object.entity.clientInfo.ICombinationInfoEntity;
import com.lolduo.duo.object.entity.clientInfo.entity.SoloInfoEntity;
import com.lolduo.duo.repository.ChampionRepository;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.clientInfo.repository.DuoInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.QuintetInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.SoloInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.TrioInfoRepository;
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
    private final SoloInfoRepository soloInfoRepository;
    private final DuoInfoRepository duoInfoRepository;
    private final TrioInfoRepository trioInfoRepository;
    private final QuintetInfoRepository quintetInfoRepository;
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
    private ChampionInfo championInfo2ClientChampionInfo(ChampionInfoDTO championInfoDTO){  // 챔피언 이름, 이미지 URL, 포지션 가져옴
        log.info(championRepository.findAll().size()+" 사이즈가 0 인 경우, riotService에서 setChampion 실행 아직 안된 상태");
        ChampionEntity champion = championRepository.findById(championInfoDTO.getChampionId()).orElse(new ChampionEntity(0L,"ALL","ALL.png"));
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/";
        String positionbaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/line/";
        return new ChampionInfo(championInfoDTO.getChampionId(), champion.getName(), baseUrl+champion.getImgUrl(), championInfoDTO.getPosition(), positionbaseUrl + championInfoDTO.getPosition() + ".png");
    }
    public ResponseEntity<?> getChampionDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        log.info("getChampionDetail - 조합 상세 정보 확인.");
        championInfoDTOList.forEach(championInfoDTO -> {
            log.info("getChampionDetail - champion : {}, position : {}", championInfoDTO.getChampionId(), championInfoDTO.getPosition());
        });

        if(championInfoDTOList==null) {
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
        Long allCount = championDetailComponent.getAllCount(championInfoDTOList);
        log.info("getChampionDetail : perkInfo start");
        List<ResponsePerk> perkInfo = championDetailComponent.editPerkDetail(championDetailComponent.getPerkDetail(championInfoDTOList, true), championInfoDTOList, allCount);
        log.info("getChampionDetail : spellInfo start");
        List<ResponseSpell> spellInfo=championDetailComponent.editSpellDetail(championDetailComponent.getSpellDetail(championInfoDTOList), championInfoDTOList, allCount) ;
        log.info("getChampionDetail : itemInfo start");
        List<ResponseItem> itemInfo = championDetailComponent.editItemDetail(championDetailComponent.getItemDetail(championInfoDTOList, true), championInfoDTOList, allCount);

        log.info("정상처리, 200 리턴");
        return new ResponseEntity<>(new ChampionDetail(perkInfo,spellInfo,itemInfo),HttpStatus.OK);
    }
    public ResponseEntity<?> getChampionInfoList(CombiSearchDTO combiSearchDTO){
        // 주석은 모듈화를 위해 임시로 추가한 것. 이후 삭제해야 함.
        log.info("getChampionInfoList - 챔피언 조합 검색. winRateAsc: {}, gameCountAsc: {}", combiSearchDTO.getWinRateAsc(), combiSearchDTO.getGameCountAsc());
        List<ChampionInfoList> result = new ArrayList<>();
        ICombinationInfoRepository infoRepository;
        int championCount = combiSearchDTO.getChampionInfoDTOList().size();

        // 챔피언 수에 해당하는 리포지토리를 가져오는 부분
        infoRepository = getInfoRepository(championCount);
        if(infoRepository == null && championCount != 1)
            return new ResponseEntity<>("요청한 챔피언 개수가 올바르지 않습니다. (1, 2, 3, 5만 가능)",HttpStatus.BAD_REQUEST);

        // 챔피언 수가 1일 때
        if (championCount == 1) {
            ChampionInfoDTO championInfoDTO = combiSearchDTO.getChampionInfoDTOList().get(0);
            List<SoloInfoEntity> infoEntityList;

            log.info("getChampionInfoList() - 매치 데이터 검색.\n검색 championId = {}\n검색 position = {}", championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            // 리포지토리에서 조합 정보를 검색하는 부분
            log.info("getChampionInfoList() - 시간 측정 : DB 검색 시작 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
            if (championInfoDTO.getChampionId() == 0) { // ?일 때
                if (!championInfoDTO.getPosition().equals("ALL")) { // ALL이 아닐 때

                    if (combiSearchDTO.getWinRateAsc() != null) {
                        if (combiSearchDTO.getWinRateAsc())
                            infoEntityList = soloInfoRepository.findAllByPositionWinRateAsc(championInfoDTO.getPosition()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByPositionWinRateDesc(championInfoDTO.getPosition()).orElse(null);
                    }
                    else if (combiSearchDTO.getGameCountAsc() != null) {
                        if (combiSearchDTO.getGameCountAsc())
                            infoEntityList = soloInfoRepository.findAllByPositionGameCountAsc(championInfoDTO.getPosition()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByPositionGameCountDesc(championInfoDTO.getPosition()).orElse(null);
                    }
                    else
                        infoEntityList = soloInfoRepository.findAllByPositionWinRateDesc(championInfoDTO.getPosition()).orElse(null);
                }
                else {

                    if (combiSearchDTO.getWinRateAsc() != null) {
                        if (combiSearchDTO.getWinRateAsc())
                            infoEntityList = soloInfoRepository.findAllWinRateAsc().orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllWinRateDesc().orElse(null);
                    }
                    else if (combiSearchDTO.getGameCountAsc() != null) {
                        if (combiSearchDTO.getGameCountAsc())
                            infoEntityList = soloInfoRepository.findAllGameCountAsc().orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllGameCountDesc().orElse(null);
                    }
                    else
                        infoEntityList = soloInfoRepository.findAllWinRateDesc().orElse(null);
                }
            }
            else { // ?이 아닐 때
                if (!championInfoDTO.getPosition().equals("ALL")) { // ALL이 아닐 때

                    if (combiSearchDTO.getWinRateAsc() != null) {
                        if (combiSearchDTO.getWinRateAsc())
                            infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionWinRateAsc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionWinRateDesc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                    }
                    else if (combiSearchDTO.getGameCountAsc() != null) {
                        if (combiSearchDTO.getGameCountAsc())
                            infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionGameCountAsc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionGameCountDesc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                    }
                    else
                        infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionWinRateDesc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                }
                else {

                    if (combiSearchDTO.getWinRateAsc() != null) {
                        if (combiSearchDTO.getWinRateAsc())
                            infoEntityList = soloInfoRepository.findAllByChampionIdWinRateAsc(championInfoDTO.getChampionId()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByChampionIdWinRateDesc(championInfoDTO.getChampionId()).orElse(null);
                    }
                    else if (combiSearchDTO.getGameCountAsc() != null) {
                        if (combiSearchDTO.getGameCountAsc())
                            infoEntityList = soloInfoRepository.findAllByChampionIdGameCountAsc(championInfoDTO.getChampionId()).orElse(null);
                        else
                            infoEntityList = soloInfoRepository.findAllByChampionIdGameCountDesc(championInfoDTO.getChampionId()).orElse(null);
                    }
                    else
                        infoEntityList = soloInfoRepository.findAllByChampionIdWinRateDesc(championInfoDTO.getChampionId()).orElse(null);
                }
            }
            log.info("getChampionInfoList() - 시간 측정 : DB 검색 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

            // 조합 정보가 존재한다면 ClientChampionInfo의 List를 만들고, 승률을 계산해 반환한다.
            if (infoEntityList != null && !infoEntityList.isEmpty()) {
                log.info("getChampionInfoList() - 검색 결과.");
                infoEntityList.forEach( infoEntity -> {
                    log.info("championId = {}, position = {}, AllCount = {}, WinCount = {}",
                            infoEntity.getChampionId(), infoEntity.getPosition(), infoEntity.getAllCount(), infoEntity.getWinCount());

                    List<ChampionInfo> clientChampionInfoList = new ArrayList<>(1);
                    clientChampionInfoList.add(championInfo2ClientChampionInfo(new ChampionInfoDTO(infoEntity.getChampionId(), infoEntity.getPosition())));
                    result.add(new ChampionInfoList(clientChampionInfoList, String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount())),
                            String.valueOf(infoEntity.getAllCount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"));
                });
                log.info("getChampionInfoList() - 시간 측정 : championInfo로 변환 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
            }

            // 조합 정보가 존재하지 않는다면 입력 값을 그대로 ClientChampionInfo로 변환한 List만 반환한다.
            else {
                log.info("getChampionInfoList() - 검색 결과.\n해당하는 데이터 행이 존재하지 않습니다.");
                List<ChampionInfo> clientChampionInfoList = new ArrayList<ChampionInfo>(1);
                clientChampionInfoList.add(championInfo2ClientChampionInfo(championInfoDTO));
                result.add(new ChampionInfoList(clientChampionInfoList, "데이터가 존재하지 않습니다.","0 게임"));
            }
        }
        //챔피언 수가 2 이상일 때
        else {
            Map<Long, String> champPositionMap = new HashMap<Long, String>();
            List<String> excludePositionList = new ArrayList<>(5);

            Map<Long, Long> selectedChampionOrderMap = new HashMap<>();
            Map<String, Long> selectedPositionOrderMap = new HashMap<>();
            Queue<ChampionInfo> allQueue = new LinkedList<ChampionInfo>();

            // 입력된 챔피언 각각에 대해 ?인지, 그리고 ALL포지션인지 확인하여 포지션, 챔피언 목록과 관련된 객체를 채워 넣는다.
            log.info("getChampionInfoList() - 시간 측정 : 선택 포지션/챔피언 정보 관련 객체 생성 시작  {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
            setChampAndPositionInfo(combiSearchDTO.getChampionInfoDTOList(), champPositionMap, excludePositionList, selectedChampionOrderMap, selectedPositionOrderMap);
            log.info("getChampionInfoList() - 시간 측정 : 선택 포지션/챔피언 정보 관련 객체 생성 끝  {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

            try {
                // DB에서 조합 정보를 검색한다.
                log.info("getChampionInfoList() - 시간 측정 : DB 검색 시작 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

                List<? extends ICombinationInfoEntity> infoEntityList;
                if (combiSearchDTO.getWinRateAsc() != null) {
                    if (combiSearchDTO.getWinRateAsc())
                        infoEntityList = infoRepository.findAllByChampionIdAndPositionWinRateAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);
                    else
                        infoEntityList = infoRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);
                }
                else if (combiSearchDTO.getGameCountAsc() != null) {
                    if (combiSearchDTO.getGameCountAsc())
                        infoEntityList = infoRepository.findAllByChampionIdAndPositionGameCountAsc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);
                    else
                        infoEntityList = infoRepository.findAllByChampionIdAndPositionGameCountDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);
                }
                else
                    infoEntityList = infoRepository.findAllByChampionIdAndPositionWinRateDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);

                log.info("getChampionInfoList() - 매치 데이터 검색.\n지정된 championId = {}\n지정된 position = {}\n실제 검색 position = {}\n선택한 챔피언들에게 금지된 position = {}",
                        objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(champPositionMap), objectMapper.writeValueAsString(excludePositionList));
                log.info("getChampionInfoList() - 시간 측정 : DB 검색 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));

                // 나온 검색 결과를 반환할 형태로 변환한 후 결과 리스트에 넣어준다.
                putCombinationInfoToResult(result, infoEntityList, combiSearchDTO.getChampionInfoDTOList(), selectedPositionOrderMap, selectedChampionOrderMap, allQueue);
                log.info("getChampionInfoList() - 시간 측정 : 검색 결과 정렬해 결과 리스트에 저장 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
                return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.OK);
            }
        }
        log.info("getChampionInfoList() - 시간 측정 : API 처리 끝 {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    private ICombinationInfoRepository getInfoRepository(int championCount) {
        if (championCount == 1) {
            log.info("getInfoRepository() - championCount : {}, 1명", championCount);
            return null;
        }
        else if (championCount == 2) {
            log.info("getInfoRepository() - championCount : {}, 2명", championCount);
            return duoInfoRepository;
        }
        else if (championCount == 3) {
            log.info("getInfoRepository() - championCount : {}, 3명", championCount);
            return trioInfoRepository;
        }
        else if (championCount == 5) {
            log.info("getInfoRepository() - championCount : {}, 5명", championCount);
            return quintetInfoRepository;
        }
        else {
            log.info("getInfoRepository() - 요청 문제 발생");
            return null;
        }
    }

    private void setChampAndPositionInfo(List<ChampionInfoDTO> championInfoDTOList, Map<Long, String> champPositionMap, List<String> excludePositionList, Map<Long, Long> selectedChampionOrderMap, Map<String, Long> selectedPositionOrderMap) {
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
        log.info("setChampAndPositionInfo - 챔피언/포지션 정보 저장 결과\nchampPositionMap: {}\nexcludePositionList: {}\nselectedChampionOrderMap: {}\nselectedPositionOrderMap: {}",
                champPositionMap, excludePositionList, selectedChampionOrderMap, selectedPositionOrderMap);
    }

    private void putCombinationInfoToResult(List<ChampionInfoList> result, List<? extends ICombinationInfoEntity> infoEntityList, List<ChampionInfoDTO> championInfoDTOList, Map<String, Long> selectedPositionOrderMap, Map<Long, Long> selectedChampionOrderMap, Queue<ChampionInfo> allQueue) {
        if (infoEntityList != null && !infoEntityList.isEmpty()) {
            log.info("putCombinationInfoToResult() - 조합 정보.");
            infoEntityList.forEach(infoEntity -> {
                log.info("putCombinationInfoToResult() - 검색 결과 : championId = {}, position = {}, AllCount = {}, WinCount = {}", infoEntity.getChampionId().toString(), infoEntity.getPosition().toString(), infoEntity.getAllCount(), infoEntity.getWinCount());
                result.add(new ChampionInfoList(
                                createClientChampionInfoDTOList(infoEntity.getChampionId().size(), infoEntity, selectedPositionOrderMap, selectedChampionOrderMap, allQueue),
                                String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount())),
                        String.valueOf(infoEntity.getAllCount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                        )
                );
            });
        }
        else {
            log.info("putCombinationInfoToResult() - 조합 정보.\n해당하는 데이터 행이 존재하지 않습니다.");
            List<ChampionInfo> clientChampionInfoList = new ArrayList<ChampionInfo>();
            championInfoDTOList.forEach(championInfoDTO ->
                    clientChampionInfoList.add(championInfo2ClientChampionInfo(championInfoDTO))
            );
            result.add(new ChampionInfoList(clientChampionInfoList, "데이터가 존재하지 않습니다.","0 게임"));
        }
    }

    private List<ChampionInfo> createClientChampionInfoDTOList(int championCount, ICombinationInfoEntity infoEntity, Map<String, Long> selectedPositionOrderMap, Map<Long, Long> selectedChampionOrderMap, Queue<ChampionInfo> allQueue) {
        ChampionInfo[] championInfoArray = new ChampionInfo[championCount];

        for (Map.Entry<Long, String> positionEntry : infoEntity.getPosition().entrySet()) {
            Long order = -1L;
            if (selectedPositionOrderMap.containsKey(positionEntry.getValue()))
                order = selectedPositionOrderMap.get(positionEntry.getValue());
            else if (selectedChampionOrderMap.containsKey(positionEntry.getKey()))
                order = selectedChampionOrderMap.get(positionEntry.getKey());

            if (order != -1L)
                championInfoArray[order.intValue()] = championInfo2ClientChampionInfo(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue()));
            else
                allQueue.offer(championInfo2ClientChampionInfo(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue())));
        }

        log.info("createClientChampionInfoDTOList() - 반환 리스트 원소 값");
        for (int i = 0; i < championInfoArray.length; i++) {
            if (championInfoArray[i] == null)
                championInfoArray[i] = allQueue.poll();

            log.info("createClientChampionInfoDTOList() - {}번째 원소 : {}={}", i + 1, championInfoArray[i].getChampionName(), championInfoArray[i].getPosition());
        }
        return Arrays.asList(championInfoArray);
    }

}
