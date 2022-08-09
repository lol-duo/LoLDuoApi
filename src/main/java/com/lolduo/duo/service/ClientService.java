package com.lolduo.duo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoListDTO;
import com.lolduo.duo.dto.client.ClientChampionInfoDTO;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.entity.clientInfo.ICombinationInfoEntity;
import com.lolduo.duo.entity.clientInfo.SoloInfoEntity;
import com.lolduo.duo.repository.ChampionRepository;
import com.lolduo.duo.repository.clientInfo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


    public ResponseEntity<?> getChampionList(){
        List<ChampionEntity> championEntityList = new ArrayList<>(championRepository.findAll());
        for(ChampionEntity champion : championEntityList){
            champion.setImgUrl("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/"+champion.getImgUrl());
        }
        Collections.sort(championEntityList);
        return new ResponseEntity<>(championEntityList, HttpStatus.OK);
    }

    private ClientChampionInfoDTO championInfo2ClientChampionInfo(ChampionInfoDTO championInfoDTO){  // 챔피언 이름, 이미지 URL, 포지션 가져옴
        log.info(championRepository.findAll().size()+" 사이즈가 0 인 경우, ritoService에서 setChampion 실행 아직 안된 상태");
        ChampionEntity champion = championRepository.findById(championInfoDTO.getChampionId()).orElse(new ChampionEntity(0L,"A","A.png"));
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/";
        String positionbaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/line/";
        return new ClientChampionInfoDTO(champion.getName() ,baseUrl+champion.getImgUrl(),championInfoDTO.getPosition(),positionbaseUrl +championInfoDTO.getPosition()+".png");
    }

    public ResponseEntity<?> getChampionInfoList(ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ChampionInfoListDTO> result = new ArrayList<>();
        ICombinationInfoRepository infoRepository;
        int championCount = championInfoDTOList.size();

        // 챔피언 수에 해당하는 리포지토리를 가져오는 부분
        if (championCount == 1) {
            log.info("getChampionInfoList() - championCount : {}, 1명", championCount);
            infoRepository = null;
        }
        else if (championCount == 2) {
            log.info("getChampionInfoList() - championCount : {}, 2명", championCount);
            infoRepository = duoInfoRepository;
        }
        else if (championCount == 3) {
            log.info("getChampionInfoList() - championCount : {}, 3명", championCount);
            infoRepository = trioInfoRepository;
        }
        else if (championCount == 5) {
            log.info("getChampionInfoList() - championCount : {}, 5명", championCount);
            infoRepository = quintetInfoRepository;
        }
        else {
            log.info("getChampionList 요청 문제 발생");
            return new ResponseEntity<>("요청한 챔피언 개수가 올바르지 않습니다. (1, 2, 3, 5만 가능)",HttpStatus.BAD_REQUEST);
        }

        // 챔피언 수가 1일 때
        if (championCount == 1) {
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);
            List<SoloInfoEntity> infoEntityList;

            log.info("getChampionInfoList() - 매치 데이터 검색.\n검색 championId = {}\n검색 position = {}", championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            // 리포지토리에서 조합 정보를 검색하는 부분
            if (championInfoDTO.getChampionId() == 0) { // ?일 때
                if (!championInfoDTO.getPosition().equals("ALL")) // ALL이 아닐 때
                    infoEntityList = soloInfoRepository.findAllByPositionDesc(championInfoDTO.getPosition()).orElse(null);
                else
                    infoEntityList = soloInfoRepository.findAllDesc().orElse(null);
            }
            else { // ?이 아닐 때
                if (!championInfoDTO.getPosition().equals("ALL")) // ALL이 아닐 때
                    infoEntityList = soloInfoRepository.findAllByChampionIdAndPositionDesc(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
                else
                    infoEntityList = soloInfoRepository.findAllByChampionIdDesc(championInfoDTO.getChampionId()).orElse(null);
            }

            // 조합 정보가 존재한다면 ClientChampionInfo의 List를 만들고, 승률을 계산해 반환한다.
            if (infoEntityList != null && !infoEntityList.isEmpty()) {
                log.info("getChampionInfoList() - 검색 결과.");
                infoEntityList.forEach( infoEntity -> {
                    log.info("championId = {}, position = {}, AllCount = {}, WinCount = {}",
                            infoEntity.getChampionId(), infoEntity.getPosition(), infoEntity.getAllCount(), infoEntity.getWinCount());

                    List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<>(1);
                    clientChampionInfoList.add(championInfo2ClientChampionInfo(new ChampionInfoDTO(infoEntity.getChampionId(), infoEntity.getPosition())));
                    result.add(new ChampionInfoListDTO(clientChampionInfoList, String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount()))));
                });
            }
            else {
                // 조합 정보가 존재하지 않는다면 입력 값을 그대로 ClientChampionInfo로 변환한 List만 반환한다.
                log.info("getChampionInfoList() - 검색 결과.\n해당하는 데이터 행이 존재하지 않습니다.");
                List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<ClientChampionInfoDTO>(1);
                clientChampionInfoList.add(championInfo2ClientChampionInfo(championInfoDTO));
                result.add(new ChampionInfoListDTO(clientChampionInfoList, "데이터가 존재하지 않습니다."));
            }
        }
        else {
            //챔피언 수가 2 이상일 때
            Map<Long, String> positionMap = new HashMap<Long, String>();
            List<String> excludePositionList = new ArrayList<>(5);

            Long inputOrder = 0L;
            Map<Long, Long> selectedChampionOrderMap = new HashMap<>();
            Map<String, Long> selectedPositionOrderMap = new HashMap<>();
            Queue<ClientChampionInfoDTO> allQueue = new LinkedList<ClientChampionInfoDTO>();

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
                        positionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
                        selectedPositionOrderMap.put(championInfoDTO.getPosition(), inputOrder);
                    }
                }
                inputOrder++;
            }

            try {
                List<? extends ICombinationInfoEntity> infoEntityList = infoRepository
                        .findAllByChampionIdAndPositionDesc(objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(positionMap), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(excludePositionList)).orElse(null);
                log.info("getChampionInfoList() - 매치 데이터 검색.\n지정된 championId = {}\n지정된 position = {}\n실제 검색 position = {}\n선택한 챔피언들에게 금지된 position = {}",
                        objectMapper.writeValueAsString(selectedChampionOrderMap.keySet()), objectMapper.writeValueAsString(selectedPositionOrderMap.keySet()), objectMapper.writeValueAsString(positionMap), objectMapper.writeValueAsString(excludePositionList));

                if (infoEntityList != null && !infoEntityList.isEmpty()) {
                    log.info("getChampionInfoList() - 검색 결과.");
                    infoEntityList.forEach(infoEntity -> {
                        log.info("championId = {}, position = {}, AllCount = {}, WinCount = {}",
                                infoEntity.getChampionId().toString(), infoEntity.getPosition().toString(), infoEntity.getAllCount(), infoEntity.getWinCount());

                        ClientChampionInfoDTO[] clientChampionInfoDTOArray = new ClientChampionInfoDTO[championCount];
                        for (Map.Entry<Long, String> positionEntry : infoEntity.getPosition().entrySet()) {
                            Long order = -1L;
                            if (selectedPositionOrderMap.containsKey(positionEntry.getValue()))
                                order = selectedPositionOrderMap.get(positionEntry.getValue());
                            else if (selectedChampionOrderMap.containsKey(positionEntry.getKey()))
                                order = selectedChampionOrderMap.get(positionEntry.getKey());

                            if (order != -1L)
                                clientChampionInfoDTOArray[order.intValue()] = championInfo2ClientChampionInfo(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue()));
                            else
                                allQueue.offer(championInfo2ClientChampionInfo(new ChampionInfoDTO(positionEntry.getKey(), positionEntry.getValue())));
                        }

                        for (int i = 0; i < clientChampionInfoDTOArray.length; i++) {
                            if (clientChampionInfoDTOArray[i] == null)
                                clientChampionInfoDTOArray[i] = allQueue.poll();
                        }

                        result.add(new ChampionInfoListDTO(Arrays.asList(clientChampionInfoDTOArray), String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount()))));
                    });
                }
                else {
                    log.info("getChampionInfoList() - 검색 결과.\n해당하는 데이터 행이 존재하지 않습니다.");
                    List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<ClientChampionInfoDTO>();
                    championInfoDTOList.forEach(championInfoDTO ->
                            clientChampionInfoList.add(championInfo2ClientChampionInfo(championInfoDTO))
                    );
                    result.add(new ChampionInfoListDTO(clientChampionInfoList, "데이터가 존재하지 않습니다."));
                }
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
                return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
