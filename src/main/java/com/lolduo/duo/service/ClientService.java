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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    private List<ClientChampionInfoDTO> makeDummy(List<ChampionInfoDTO> championInfoDTOList ){
        List<ClientChampionInfoDTO> result  = new ArrayList<>();
        championInfoDTOList.forEach(championInfoDTO -> {
            result.add(championInfo2ClientChampionInfo(championInfoDTO));
        });
        return result;
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

        if (championCount == 1) {
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);

            log.info("getChampionInfoList() - 매치 데이터 검색.\n검색 championId = {}\n검색 position = {}", championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            SoloInfoEntity infoEntity = soloInfoRepository.findByChampionIdAndPosition(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);

            if (infoEntity != null) {
                log.info("getChampionInfoList() - 검색 결과");
                log.info("championId = {}, position = {}, AllCount = {}, WinCount = {}",
                        infoEntity.getChampionId(), infoEntity.getPosition(), infoEntity.getAllCount(), infoEntity.getWinCount());

                List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<>(1);
                clientChampionInfoList.add(championInfo2ClientChampionInfo(new ChampionInfoDTO(infoEntity.getChampionId(), infoEntity.getPosition())));
                result.add(new ChampionInfoListDTO(clientChampionInfoList, String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount()))));
            }
            else {
                log.info("getChampionInfoList() - 검색 결과.\n해당하는 데이터 행이 존재하지 않습니다.");
                List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<>(1);
                result.add(new ChampionInfoListDTO(clientChampionInfoList, "데이터가 존재하지 않습니다."));
            }
        }
        else {
            TreeSet<Long> selectedChampionIdSet = new TreeSet<Long>();
            Map<Long, String> positionMap = new HashMap<Long, String>();
            List<String> excludePositionList = new ArrayList<>(5);

            championInfoDTOList.forEach(championInfoDTO -> {
                if (championInfoDTO.getChampionId() == 0) {
                    if (!championInfoDTO.getPosition().equals("ALL"))
                        excludePositionList.add(championInfoDTO.getPosition());
                }
                else {
                    selectedChampionIdSet.add(championInfoDTO.getChampionId());
                    if (!championInfoDTO.getPosition().equals("ALL"))
                        positionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
                }
            });

            try {
                List<? extends ICombinationInfoEntity> infoEntityList = infoRepository
                        .findAllByChampionIdAndPositionDesc(objectMapper.writeValueAsString(selectedChampionIdSet), objectMapper.writeValueAsString(positionMap)).orElse(null);
                log.info("getChampionInfoList() - 매치 데이터 검색.\n검색 championId = {}\n검색 position = {}\n선택한 챔피언들에게 금지된 position = {}",
                        objectMapper.writeValueAsString(selectedChampionIdSet), objectMapper.writeValueAsString(positionMap), objectMapper.writeValueAsString(excludePositionList));

                if (infoEntityList != null) {
                    infoEntityList.removeIf(infoEntity -> {
                        for (Long selectedChampionId : selectedChampionIdSet) {
                            if (excludePositionList.contains(infoEntity.getPosition().get(selectedChampionId)))
                                return true;
                        }
                        return false;
                    });
                }

                if (infoEntityList != null && !infoEntityList.isEmpty()) {
                    log.info("getChampionInfoList() - 검색 결과.");
                    infoEntityList.forEach(infoEntity -> {
                        List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<ClientChampionInfoDTO>();
                        infoEntity.getPosition().forEach((championId, position) -> {
                            clientChampionInfoList.add(championInfo2ClientChampionInfo(new ChampionInfoDTO(championId, position)));
                        });
                        result.add(new ChampionInfoListDTO(clientChampionInfoList, String.format("%.2f%%", 100 * ((double) infoEntity.getWinCount() / infoEntity.getAllCount()))));
                    });
                }
                else {
                    log.info("getChampionInfoList() - 검색 결과.\n해당하는 데이터 행이 존재하지 않습니다.");
                    List<ClientChampionInfoDTO> clientChampionInfoList = new ArrayList<ClientChampionInfoDTO>();
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
