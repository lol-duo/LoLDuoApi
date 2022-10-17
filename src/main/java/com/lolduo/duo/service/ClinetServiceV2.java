package com.lolduo.duo.service;

import com.lolduo.duo.object.dto.client.CombiSearchV2DTO;
import com.lolduo.duo.object.response.v2.SoloResponseV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ClinetServiceV2 {

    public ResponseEntity<?> getDummy(CombiSearchV2DTO combiSearchV2DTO) {
        if(combiSearchV2DTO ==null || combiSearchV2DTO.getChampionId() == null || combiSearchV2DTO.getPosition() == null){
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        List<SoloResponseV2> result = new ArrayList<>();
        Long combiId = 15L;
        String rankChangeImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.png";
        String rankChangeNumber = "+1";
        String rankNumberColor ="C8AA6E";
        String championName = "티모";
        String mainRuneImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.png";
        String championImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.png";
        String positionImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.png";
        String winRate = "67.2%";
        SoloResponseV2 dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankNumberColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate);
        if(combiSearchV2DTO.getPosition().equals("ALL") ==false && combiSearchV2DTO.getChampionId()!=0){
            result.add(dummy);
        }
        else if(combiSearchV2DTO.getPosition().equals("ALL") && combiSearchV2DTO.getChampionId()!=0){
            result.add(dummy);
            result.add(dummy);
            result.add(dummy);
            result.add(dummy);
            result.add(dummy);
        }
        else{
            for(int i =0 ; i < 30 ;i++){
                result.add(dummy);
            }
        }
        return new ResponseEntity<>( result,HttpStatus.OK);
    }
    /*
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
     */
}
