package com.lolduo.duo.v2;

import com.lolduo.duo.v2.entity.DoubleMatchEntity;
import com.lolduo.duo.v2.entity.MainPageChampionEntity;
import com.lolduo.duo.v2.entity.MainPagePerkEntity;
import com.lolduo.duo.v2.entity.SoloMatchEntity;
import com.lolduo.duo.v2.repository.DoubleMatchRepository;
import com.lolduo.duo.v2.repository.MainPageChampionRepository;
import com.lolduo.duo.v2.repository.MainPagePerkRepository;
import com.lolduo.duo.v2.repository.SoloMatchRepository;
import com.lolduo.duo.v2.response.DoubleResponseV2;
import com.lolduo.duo.v2.response.SoloResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceV2 {
    private final SoloMatchRepository soloMatchRepository;
    private final DoubleMatchRepository doubleMatchRepository;
    private final MainPageChampionRepository mainPageChampionRepository;
    private final MainPagePerkRepository mainPagePerkRepository;
    private final String FILE_EXTENSION =".svg";
    private final String cloudFrontBaseUrl ="https://d2d4ci5rabfoyr.cloudfront.net";

    private static String swapStr(String localA, String localB) {
        return localA;
    }
    private boolean checkPositionIsValid(String position){
        if(position.equals("ALL") || position.equals("MIDDLE") || position.equals("TOP")||position.equals("UTILITY") ||position.equals("JUNGLE") || position.equals("BOTTOM")){
            return true;
        }
        return false;
    }
    public ResponseEntity<?> getDoubleChampionInfoList(Long requestChampionId, String requestPosition, Long requestChampionId2, String requestPosition2){
        Long MINIMUM_ALL_COUNT = doubleMatchRepository.getAllCountSum().orElse(14000L) / 200L;
        List<DoubleResponseV2> doubleResponseV2List = new ArrayList<>();
        if(requestChampionId == null || requestPosition == null || requestChampionId2 == null || requestPosition2==null){
            return new ResponseEntity<>("404 BAD_REQUEST : 요청한 값들 중 null이 존재합니다. ", HttpStatus.BAD_REQUEST);
        }
        if(!checkPositionIsValid(requestPosition) || !checkPositionIsValid(requestPosition2)){
            return new ResponseEntity<>("404 BAD_REQUEST : 요청한 포지션의 값이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getDoubleChampionInfoList - 챔피언 조합 검색. champion1Id : {}, position1 : {},  champion2Id : {}, position2 : {}", requestChampionId, requestPosition,requestChampionId2,requestPosition2);
        String position1 = requestPosition;
        String position2 = requestPosition2;
        String championId1 = String.valueOf(requestChampionId);
        String championId2 = String.valueOf(requestChampionId2);
        if(requestChampionId > requestChampionId2){
            position2 = swapStr(position1,position1=position2);
            championId2 = swapStr(championId1,championId1=championId2);
        }

        String rankChangeImgUrl = cloudFrontBaseUrl + "/mainPage/rankChange/RankSame" + FILE_EXTENSION;
        String rankChangeNumber = "";
        String rankChangeColor = "";
        String rankNumberIcon = ""; //only 1,2,3 rank
        String rankNumberColor =""; //only 1,2,3 rank
        String listImage1 =""; //only 4 rank after
        String listImage2 =""; //only 4 rank after
        Long i = 1L;
        if(position1.equals("ALL"))
            position1 = "%";
        if(position2.equals("ALL"))
            position2 ="%";
        if(championId1.equals("0"))
            championId1 = "%";
        if(championId2.equals("0"))
            championId2 = "%";
        List<DoubleMatchEntity> doubleMatchEntityList;
        doubleMatchEntityList = doubleMatchRepository.findAllByPositionAndChampionId(position1,championId1,position2,championId2,MINIMUM_ALL_COUNT);
        if (doubleMatchEntityList == null || doubleMatchEntityList.size() == 0) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
        }
        for(DoubleMatchEntity doubleMatchEntity : doubleMatchEntityList){
            if(doubleMatchEntity ==null){
                log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
                return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
            }
            MainPageChampionEntity champion1Entity = mainPageChampionRepository.findById(doubleMatchEntity.getChampionId1()).orElse(null);
            MainPageChampionEntity champion2Entity = mainPageChampionRepository.findById(doubleMatchEntity.getChampionId2()).orElse(null);
            String champion1Name ="";
            String champion1ImgUrl = "";
            String champion2Name="";
            String champion2ImgUrl ="";

            if (champion1Entity == null ||champion2Entity == null ) {
                log.info("챔피언 테이블에서 챔피언을 찾을 수 없습니다. Champion 테이블을 확인해주세요.  champion1Id: {} , champion2Id: {}", doubleMatchEntity.getChampionId1(), doubleMatchEntity.getChampionId2());
                champion1Name = "이름 없음";
                champion1ImgUrl = cloudFrontBaseUrl + "/champion/Teemo" + FILE_EXTENSION;
                champion2Name = "이름 없음";
                champion2ImgUrl = cloudFrontBaseUrl + "/champion/Teemo" + FILE_EXTENSION;
            } else{
                champion1Name = champion1Entity.getName();
                champion2Name = champion2Entity.getName();
                log.info("champion1Name  : {} , champion2Name : {}", champion1Name,champion2Name );
                champion1ImgUrl = cloudFrontBaseUrl + champion1Entity.getImgUrl() + FILE_EXTENSION;
                champion2ImgUrl = cloudFrontBaseUrl + champion2Entity.getImgUrl() + FILE_EXTENSION;
                log.info("champion1ImgUrl  : {} ,champion2ImgUrl : {} ", champion1ImgUrl,champion2ImgUrl);
            }
            MainPagePerkEntity perkEntity1 = mainPagePerkRepository.findById(doubleMatchEntity.getMainRune1()).orElse(null);
            MainPagePerkEntity perkEntity2 = mainPagePerkRepository.findById(doubleMatchEntity.getMainRune2()).orElse(null);
            String mainRune1 ="";
            String mainRune2 ="";
            if(perkEntity1 ==null || perkEntity2 ==null){
                log.info("룬 테이블에서 해당 룬을 찾을 수 없습니다. Perk 테이블을 확인해주세요 mainRune1: {} , mainRune2 : {}", doubleMatchEntity.getMainRune1(),doubleMatchEntity.getMainRune2());
                mainRune1 =  cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
                mainRune2 =  cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
            } else{
                mainRune1 = cloudFrontBaseUrl +perkEntity1.getImgUrl() + FILE_EXTENSION;
                mainRune2 = cloudFrontBaseUrl +perkEntity2.getImgUrl() + FILE_EXTENSION;
                log.info("mainRune1 Url: {} , mainRune2 Url : {}", mainRune1,mainRune2);
            }
            String position1Url = cloudFrontBaseUrl + "/mainPage/position/" + doubleMatchEntity.getPosition1() + FILE_EXTENSION;
            String position2Url = cloudFrontBaseUrl + "/mainPage/position/" + doubleMatchEntity.getPosition2() + FILE_EXTENSION;
            String winRate = String.format("%.2f%%", 100 * ((double) doubleMatchEntity.getWinCount() / doubleMatchEntity.getAllCount()));
            log.info("winRate : {}", winRate);
            DoubleResponseV2 responseV2 ;
            if( i > 3L){
                rankNumberIcon = "";
                rankNumberColor ="";
                listImage1 =cloudFrontBaseUrl+ "/mainPage/icon/listImage" + FILE_EXTENSION; // 4 rank after
                listImage2 =cloudFrontBaseUrl+ "/mainPage/icon/listImage" + FILE_EXTENSION; // 4 rank after
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        rankChangeColor, i++,rankNumberIcon,rankNumberColor,champion1Name,champion1ImgUrl,mainRune1,
                        position1Url,listImage1,champion2Name,champion2ImgUrl,mainRune2,position2Url,listImage2,winRate);
            } else{
                rankNumberIcon = cloudFrontBaseUrl+ "/mainPage/icon/rankChangeIcon" + FILE_EXTENSION; //only 1,2,3 rank
                rankNumberColor ="C8AA6E"; //only 1,2,3 rank
                listImage1 ="";
                listImage2 ="";
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        rankChangeColor, i++,rankNumberIcon,rankNumberColor,champion1Name,champion1ImgUrl,mainRune1,
                        position1Url,listImage1,champion2Name,champion2ImgUrl,mainRune2,position2Url,listImage2,winRate);
            }
            doubleResponseV2List.add(responseV2);
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }
    public ResponseEntity<?> getSoloChampionInfoList(Long requestChampionId,String requestPosition) {
        Long MINIMUM_ALL_COUNT = soloMatchRepository.getAllCountSum().orElse(40000L) / 200L;
        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        if (requestPosition == null || requestChampionId == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        if(!checkPositionIsValid(requestPosition)){
            return new ResponseEntity<>("404 BAD_REQUEST : 요청한 포지션의 값이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getSoloChampionInfoList - 챔피언 조합 검색. championId : {}, position : {}", requestChampionId, requestPosition);
        String position = requestPosition;
        String championId = String.valueOf(requestChampionId);
        String rankChangeImgUrl = cloudFrontBaseUrl + "/mainPage/rankChange/RankSame" + FILE_EXTENSION;
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
            MainPageChampionEntity championEntity = mainPageChampionRepository.findById(soloMatchEntity.getChampionId()).orElse(null);
            String championName = "";
            String championImgUrl = "";
            if (championEntity == null) {
                log.info("챔피언 테이블에서 챔피언을 찾을 수 없습니다. Champion 테이블을 확인해주세요.  championId: {}", soloMatchEntity.getChampionId());
                championName = "이름 없음";
                championImgUrl = cloudFrontBaseUrl + "/champion/Teemo" + FILE_EXTENSION;
            } else {
                championName = championEntity.getName();
                log.info("championName  : {}", championName);
                championImgUrl = cloudFrontBaseUrl + championEntity.getImgUrl() + FILE_EXTENSION;
                log.info("championImgUrl  : {}", championImgUrl);
            }
            MainPagePerkEntity perkEntity = mainPagePerkRepository.findById(soloMatchEntity.getMainRune()).orElse(null);
            String mainRune = "";
            if (perkEntity == null) {
                log.info("룬 테이블에서 해당 룬을 찾을 수 없습니다. Perk 테이블을 확인해주세요 mainRune: {}", soloMatchEntity.getMainRune());
                mainRune = cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
            } else {
                mainRune = cloudFrontBaseUrl +perkEntity.getImgUrl() + FILE_EXTENSION;
                log.info("mainRune Url : {}", mainRune);
            }
            String positionUrl = cloudFrontBaseUrl + "/mainPage/position/" + soloMatchEntity.getPosition() + FILE_EXTENSION;
            String winRate = String.format("%.2f%%", 100 * ((double) soloMatchEntity.getWinCount() / soloMatchEntity.getAllCount()));
            log.info("winRate : {}", winRate);
            SoloResponseV2 responseV2;
            if (i > 2) {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionUrl, winRate, i++, "");
            } else {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionUrl, winRate, i++, "C8AA6E");
            }
            soloResponseV2List.add(responseV2);
        }
        return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
    }
    public ResponseEntity<?> getDoubleDummy(Long championId, String position, Long championId2, String position2) {
        if(championId == null || position == null || championId2 ==null || position2 == null){
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        List<DoubleResponseV2> doubleResponseV2List = new ArrayList<>();
        Long id = 0L;
        String rankChangeImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/rankChange/RankUp.svg";
        String rankChangeNumber = "+1";
        String rankChangeColor = "C8AA6E";
        Long rankNumber = 1L;
        String rankNumberIcon = "";
        String rankNumberColor = "";

        String champion1Name = "티모";
        String champion1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.svg";
        String mainRune1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg";
        String position1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.svg";
        String listImage1 = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/icon/listImage.svg";

        String champion2Name = "직스";
        String champion2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Ziggs.svg";
        String mainRune2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg";
        String position2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/BOTTOM.svg";
        String listImage2 = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/icon/listImage.svg";

        String winRate ="67.2%";
        for(Long i = 0L ; i < 30L;i++ ){
            if(i<3){
                rankNumberIcon = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/logo/Group.svg";
                rankNumberColor = "C8AA6E";
            }
            else{
                rankNumberIcon = "";
                rankNumberColor = "";
            }
            DoubleResponseV2 doubleResponseV2 = new DoubleResponseV2(id,rankChangeImgUrl,rankChangeNumber,rankChangeColor,rankNumber+i,rankNumberIcon,rankNumberColor,
                    champion1Name,champion1ImgUrl,mainRune1ImgUrl,position1ImgUrl,listImage1,champion2Name,champion2ImgUrl,mainRune2ImgUrl,position2ImgUrl,listImage2,winRate);
            doubleResponseV2List.add(doubleResponseV2);
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }
}
