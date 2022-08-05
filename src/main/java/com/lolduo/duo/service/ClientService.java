package com.lolduo.duo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoListDTO;
import com.lolduo.duo.dto.client.ClinetChampionInfoDTO;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.entity.clientInfo.DuoInfoEntity;
import com.lolduo.duo.repository.ChampionRepository;
import com.lolduo.duo.repository.clientInfo.DuoInfoRepository;
import com.lolduo.duo.repository.gameInfo.DuoRepository;
import com.lolduo.duo.repository.gameInfo.QuintetRepository;
import com.lolduo.duo.repository.gameInfo.SoloRepository;
import com.lolduo.duo.repository.gameInfo.TrioRepository;
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
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;
    private final QuintetRepository quintetRepository;

    private final DuoInfoRepository duoInfoRepository;
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
    private List<ClinetChampionInfoDTO> makeDummy(List<ChampionInfoDTO> championInfoDTOList ){
        List<ClinetChampionInfoDTO> result  = new ArrayList<>();
        championInfoDTOList.forEach(championInfoDTO -> {
            result.add(championInfo2ClientChampionInfo(championInfoDTO));
        });
        return result;
    }
    private ClinetChampionInfoDTO championInfo2ClientChampionInfo(ChampionInfoDTO championInfoDTO){  // 챔피언 이름, 이미지 URL, 포지션 가져옴
        log.info(championRepository.findAll().size()+" 사이즈가 0 인 경우, ritoService에서 setChampion 실행 아직 안된 상태");
        ChampionEntity champion = championRepository.findById(championInfoDTO.getChampionId()).orElse(new ChampionEntity(0L,"A","A.png"));
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/";
        String positionbaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/line/";
        return new ClinetChampionInfoDTO(champion.getName() ,baseUrl+champion.getImgUrl(),championInfoDTO.getPosition(),positionbaseUrl +championInfoDTO.getPosition()+".png");
    }


    public ResponseEntity<?> getChampionInfoList(ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ChampionInfoListDTO> result = new ArrayList<>();
        if (championInfoDTOList.size() == 2){
            TreeSet<Long> championIdSet = new TreeSet<Long>();
            Map<Long, String> positionMap = new HashMap<Long, String>();

            for (ChampionInfoDTO championInfoDTO : championInfoDTOList) {
                championIdSet.add(championInfoDTO.getChampionId());
                positionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            }
            try {
                Optional<DuoInfoEntity> infoEntityOptional = duoInfoRepository
                        .findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet), objectMapper.writeValueAsString(positionMap));

                String winRate;
                if (infoEntityOptional.isEmpty())
                    winRate = "데이터가 존재하지 않습니다.";
                else
                    winRate = String.format("%.2f%%", (double) 100 * (infoEntityOptional.get().getWinCount() / infoEntityOptional.get().getAllCount()));

                List<ClinetChampionInfoDTO> clientChampionInfoList = new ArrayList<ClinetChampionInfoDTO>();
                championInfoDTOList.forEach(championInfoDTO ->
                        clientChampionInfoList.add(championInfo2ClientChampionInfo(championInfoDTO))
                );

                result.add(new ChampionInfoListDTO(clientChampionInfoList, winRate));
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
                return new ResponseEntity<>("404 BAD_REQUEST",HttpStatus.OK);
            }
        }
        else if(1<=championInfoDTOList.size() && championInfoDTOList.size() <= 5){
            log.info("getChampionList 요청 수행");
            for(int i = 0 ; i<50;i++) {
                result.add(new ChampionInfoListDTO(makeDummy(championInfoDTOList), "50.89%"));
            }
        }
        else{
            log.info("getChampionList 요청 문제 발생");
            return new ResponseEntity<>("요청한 사이즈가 1~5가 아닙니다.",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
