package com.lolduo.duo.service;


import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoDTOList;
import com.lolduo.duo.dto.client.ClinetChampionInfoDTO;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.repository.ChampionRepository;
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
    private final ChampionRepository championRepository;

    public ResponseEntity<?> getChampionList(){
        List<ChampionEntity> championEntityList = new ArrayList<>(championRepository.findAll());
        for(ChampionEntity champion : championEntityList){
            champion.setImgUrl("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/"+champion.getImgUrl());
        }
        Collections.sort(championEntityList);
        return new ResponseEntity<>(championEntityList, HttpStatus.OK);
    }
    private ClinetChampionInfoDTO championInfo2ClientChampionInfo(ChampionInfoDTO championInfoDTO){
        log.info(championRepository.findAll().size()+" 사이즈가 0 인 경우, ritoService에서 setChampion 실행 아직 안된 상태");
        ChampionEntity champion = championRepository.findById(championInfoDTO.getChampionId()).orElse(new ChampionEntity(0L,"A","A.png"));
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/";
        return new ClinetChampionInfoDTO(champion.getName() ,baseUrl+champion.getImgUrl(),championInfoDTO.getPosition());
    }
    private List<ClinetChampionInfoDTO> makeDummy(List<ChampionInfoDTO> championInfoDTOList ){
        List<ClinetChampionInfoDTO> result  = new ArrayList<>();
        championInfoDTOList.forEach(championInfoDTO -> {
                result.add(championInfo2ClientChampionInfo(championInfoDTO));
        });
        return result;
    }
    public ResponseEntity<?> getChampionInfoList(ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ChampionInfoDTOList> result = new ArrayList<>();
        if(1<=championInfoDTOList.size() && championInfoDTOList.size() <= 5){
            log.info("getChampionList 요청 수행");
            for(int i = 0 ; i<50;i++) {
                result.add(new ChampionInfoDTOList(makeDummy(championInfoDTOList), "50.89%"));
            }
        }
        else{
            log.info("getChampionList 요청 문제 발생");
            return new ResponseEntity<>("요청한 사이즈가 1~5가 아닙니다.",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



    private String calWinRateSolo(Long championId, String position){
        int All = soloRepository.findAllByChampionAndPosition(championId, position).size();
        if(All == 0) {
            return "NULL";
        }
        int Win = soloRepository.findAllByChampionAndPositionAndWinTrue(championId,position).size();
        return String.format("%.2f", (Win / (double)All) * 100.0)+"%";
    }
    private List<ChampionInfoDTO> makeChampionInfoSolo(Long championId, String position){
        List<ChampionInfoDTO> list = new ArrayList<>();
        list.add(new ChampionInfoDTO(championId,position));
        return list;
    }
    /*
    private List<ChampionInfoDTOList> makeSolo(ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ChampionInfoDTOList> result = new ArrayList<>();
        ChampionInfoDTO target = championInfoDTOList.get(0);
        String winRate = "";
        if(target.getChampionId()!=0 && !target.getPosition().equals("ALL")){ //챔피언과 포지션 둘 다 고른 경우
            winRate = calWinRateSolo(target.getChampionId(), target.getPosition());
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), target.getPosition()),winRate));
            }
        }
        else if(target.getChampionId()!=0){ //챔피언만 고른 경우
            winRate = calWinRateSolo(target.getChampionId(), "TOP");
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), "TOP"), winRate));
            }
            winRate = calWinRateSolo(target.getChampionId(), "JUNGLE");
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), "JUNGLE"), winRate));
            }
            winRate = calWinRateSolo(target.getChampionId(), "MIDDLE");
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), "MIDDLE"), winRate));
            }
            winRate = calWinRateSolo(target.getChampionId(), "BOTTOM");
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), "BOTTOM"), winRate));
            }
            winRate = calWinRateSolo(target.getChampionId(), "UTILITY");
            if(!winRate.equals("NULL")) {
                result.add(new ChampionInfoDTOList(makeChampionInfoSolo(target.getChampionId(), "UTILITY"), winRate));
            }
        }
        else if(!target.getPosition().equals("ALL")){ //포지션만 고른 경우
            List<ChampionEntity> championList =  championRepository.findAll();
            championList.forEach(champion ->{
                final String winRateLamda = calWinRateSolo(champion.getId(), target.getPosition());
                if(!winRateLamda.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), target.getPosition()), winRateLamda));
                }
            });
        }
        else{ //챔피언, 포지션 둘 다 all 인 경우.
            List<ChampionEntity> championList =  championRepository.findAll();
            championList.forEach(champion ->{
                final String winRateTop = calWinRateSolo(champion.getId(),"TOP");
                final String winRateJungle = calWinRateSolo(champion.getId(),"JUNGLE");
                final String winRateMiddle = calWinRateSolo(champion.getId(),"MIDDLE");
                final String winRateBottom = calWinRateSolo(champion.getId(),"BOTTOM");
                final String winRateUtility = calWinRateSolo(champion.getId(),"UTILITY");
                if(!winRateTop.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), "TOP"), winRateTop));
                }
                if(!winRateJungle.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), "JUNGLE"), winRateJungle));
                }
                if(!winRateMiddle.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), "MIDDLE"), winRateMiddle));
                }
                if(!winRateBottom.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), "BOTTOM"), winRateBottom));
                }
                if(!winRateUtility.equals("NULL")) {
                    result.add(new ChampionInfoDTOList(makeChampionInfoSolo(champion.getId(), "UTILITY"), winRateUtility));
                }
            });
        }
        Collections.sort(result);
        return result;
    }

     */
}
