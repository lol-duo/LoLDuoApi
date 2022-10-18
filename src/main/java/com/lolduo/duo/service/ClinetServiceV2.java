package com.lolduo.duo.service;

import com.lolduo.duo.object.dto.client.CombiSearchV2DTO;
import com.lolduo.duo.object.entity.initialInfo.ChampionEntity;
import com.lolduo.duo.object.entity.initialInfo.PerkEntity;
import com.lolduo.duo.object.entity.v2.SoloMatchEntity;
import com.lolduo.duo.object.response.v2.SoloResponseV2;
import com.lolduo.duo.repository.initialInfo.ChampionRepository;
import com.lolduo.duo.repository.initialInfo.PerkRepository;
import com.lolduo.duo.repository.v2.SoloMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClinetServiceV2 {
    private final SoloMatchRepository soloMatchRepository;
    private final ChampionRepository championRepository;
    private final PerkRepository perkRepository;

    public ResponseEntity<?> getDummy(CombiSearchV2DTO combiSearchV2DTO) {
        if(combiSearchV2DTO ==null){
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        if(combiSearchV2DTO.getPosition() == null || combiSearchV2DTO.getChampionId() == null){
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        List<SoloResponseV2> result = new ArrayList<>();
        Long combiId = 15L;
        String rankChangeImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.png";
        String rankChangeNumber = "+1";
        String rankChangeColor ="C8AA6E";
        String championName = "티모";
        String championImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.png";
        String mainRuneImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.png";
        String positionImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.png";
        String winRate = "67.2%";
        String rankNumberColor = "C8AA6E";

        if(combiSearchV2DTO.getChampionId()!=0){
            if(combiSearchV2DTO.getPosition().equals("ALL")){
                for(Long i = 0L ; i < 5L; i++){
                    SoloResponseV2 dummy;
                    if(i>2){
                        dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate,i+1,"");
                    }
                    else{
                        dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate,i+1, rankNumberColor);
                    }
                     result.add(dummy);
                }
            }
            else{
                SoloResponseV2 dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate,1L,"");
                result.add(dummy);
            }
        }
        else{
            for(Long i =0L ; i < 30 ;i++){
                SoloResponseV2 dummy;
                if(i>2){
                    dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate,i+1L,"");
                }
                else{
                    dummy = new SoloResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,championName,championImgUrl,mainRuneImgUrl,positionImgUrl,winRate,i+1L, rankNumberColor);
                }
                result.add(dummy);
            }
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    public ResponseEntity<?> getSoloChampionInfoList(CombiSearchV2DTO combiSearchV2DTO) {
        Long MINIMUM_ALL_COUNT = soloMatchRepository.getAllCountSum().orElse(240000L) / 3000L;
        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        if (combiSearchV2DTO == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        if (combiSearchV2DTO.getPosition() == null || combiSearchV2DTO.getChampionId() == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getChampionInfoList - 챔피언 조합 검색. championId : {}, position : {}", combiSearchV2DTO.getChampionId(), combiSearchV2DTO.getPosition());
        String position = combiSearchV2DTO.getPosition();
        String championId = String.valueOf(combiSearchV2DTO.getChampionId());
        String rankChangeImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankSame.png";
        String rankChangeNumber = "";
        String rankNumberColor = "";
        Long i = 1L;
        if (position.equals("ALL"))
            position = "%";
        if (championId.equals("0"))
            championId = "%";
        List<SoloMatchEntity> soloMatchEntityList;
        soloMatchEntityList = soloMatchRepository.findAllByPositionAndChampionId(position, championId, MINIMUM_ALL_COUNT);
        if (soloMatchEntityList == null || soloMatchEntityList.size() == 0) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
        }
        for (SoloMatchEntity soloMatchEntity : soloMatchEntityList) {
            if (soloMatchEntity == null) {
                log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
                return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
            }
            ChampionEntity championEntity = championRepository.findById(soloMatchEntity.getChampionId()).orElse(null);
            String championName = "";
            String championImgUrl = "";
            if (championEntity == null) {
                log.info("챔피언 테이블에서 챔피언을 찾을 수 없습니다. Champion 테이블을 확인해주세요.  championId: {}", soloMatchEntity.getChampionId());
                championName = "이름 없음";
                championImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.png";
            } else {
                championName = championEntity.getName();
                log.info("championName  : {}", championName);
                championImgUrl = championEntity.getImgUrl();
                log.info("championImgUrl  : {}", championImgUrl);
            }
            PerkEntity perkEntity = perkRepository.findById(soloMatchEntity.getMainRune()).orElse(null);
            String mainRune = "";
            if (perkEntity == null) {
                log.info("룬 테이블에서 해당 룬을 찾을 수 없습니다. Perk 테이블을 확인해주세요 mainRune: {}", soloMatchEntity.getMainRune());
                mainRune = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.png";
            } else {
                String[] imgUrlArr = perkEntity.getImgUrl().split("/");
                String mainRuneBaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/";
                mainRune = mainRuneBaseUrl + imgUrlArr[imgUrlArr.length - 1];
                log.info("mainRune Url : {}", mainRune);
            }
            String positionBaseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/";
            String winRate = String.format("%.2f%%", 100 * ((double) soloMatchEntity.getWinCount() / soloMatchEntity.getAllCount()));
            log.info("winRate : {}", winRate);
            SoloResponseV2 responseV2;
            if (i > 2) {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionBaseUrl + soloMatchEntity.getPosition() + ".png", winRate, i++, "");
            } else {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionBaseUrl + soloMatchEntity.getPosition() + ".png", winRate, i++, "C8AA6E");
            }
            soloResponseV2List.add(responseV2);
        }
        return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
    }

}
