package com.lolduo.duo.service;

import com.lolduo.duo.entity.front.DoubleMatchFrontEntity;
import com.lolduo.duo.entity.initialInfo.ChampionEntity;
import com.lolduo.duo.repository.front.DoubleMatchFrontRepository;
import com.lolduo.duo.response.getChampionList.Champion;
import com.lolduo.duo.repository.initailInfo.ChampionRepository;
import com.lolduo.duo.entity.DoubleMatchEntity;
import com.lolduo.duo.entity.MainPageChampionEntity;
import com.lolduo.duo.entity.MainPagePerkEntity;
import com.lolduo.duo.entity.SoloMatchEntity;
import com.lolduo.duo.entity.front.SoloMatchFrontEntity;
import com.lolduo.duo.parser.EntityToResponseParser;
import com.lolduo.duo.repository.DoubleMatchRepository;
import com.lolduo.duo.repository.MainPageChampionRepository;
import com.lolduo.duo.repository.MainPagePerkRepository;
import com.lolduo.duo.repository.SoloMatchRepository;
import com.lolduo.duo.repository.front.SoloMatchFrontRepository;
import com.lolduo.duo.response.ChampionResponse;
import com.lolduo.duo.response.mainPage.DoubleResponseV2;
import com.lolduo.duo.response.mainPage.SoloResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceV2 {
    private final SoloMatchRepository soloMatchRepository;
    private final SoloMatchFrontRepository soloMatchFrontRepository;

    private final DoubleMatchRepository doubleMatchRepository;
    private final DoubleMatchFrontRepository doubleMatchFrontRepository;

    private final MainPageChampionRepository mainPageChampionRepository;
    private final MainPagePerkRepository mainPagePerkRepository;
    private final EntityToResponseParser entityToResponseParser;
    private final ChampionRepository championRepository;

    private final String FILE_EXTENSION =".svg";
    private final String cloudFrontBaseUrl ="https://d2d4ci5rabfoyr.cloudfront.net";

    private static String swapStr(String localA, String localB) {
        return localA;
    }
    private boolean checkPositionIsValid(String position){
        return position.equals("ALL") || position.equals("MIDDLE") || position.equals("TOP") || position.equals("UTILITY") || position.equals("JUNGLE") || position.equals("BOTTOM");
    }
    private boolean compareRequestResponse(String requestPosition1, String responsePosition1,String requestPosition2, String responsePosition2 ){
        return requestPosition1.equals(responsePosition1) || requestPosition2.equals(responsePosition2);
    }
    public ResponseEntity<?> getSoloChampionInfoList(Long requestChampionId,String requestPosition) {
        if (requestPosition == null || requestChampionId == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        if(!checkPositionIsValid(requestPosition)){
            return new ResponseEntity<>("404 BAD_REQUEST : 요청한 포지션의 값이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getSoloChampionInfoList - 챔피언 조합 검색. championId : {}, position : {}", requestChampionId, requestPosition);

        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        String position = requestPosition;
        String championId = String.valueOf(requestChampionId);
        Long MINIMUM_ALL_COUNT = soloMatchRepository.getAllCountSum().orElse(40000L) / 200L;
        log.info("최소 판수 : {}" ,MINIMUM_ALL_COUNT);
        Long i = 1L;
        List<SoloMatchEntity> soloMatchEntityList;

        if (position.equals("ALL"))
            position = "%";
        if (championId.equals("0"))
            championId = "%";

        soloMatchEntityList = soloMatchRepository.findAllByPositionAndChampionId(position, championId, MINIMUM_ALL_COUNT);
        if (soloMatchEntityList == null || soloMatchEntityList.size() == 0) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
        }
        for (SoloMatchEntity soloMatchEntity : soloMatchEntityList) {
            SoloResponseV2 soloResponseV2 = entityToResponseParser.soloMatchToSoloResponseV2(soloMatchEntity,i++);
            soloResponseV2List.add(soloResponseV2);
        }
        return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
    }

    public ResponseEntity<?> getDoubleChampionInfoListToFront(Long requestChampionId, String requestPosition, Long requestChampionId2, String requestPosition2){
        boolean swapTrueOrFalse = false;
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
        String rankChangeImgUrl = cloudFrontBaseUrl + "/mainPage/rankChange/RankSame" + FILE_EXTENSION;
        Long rankChangeNumber = 0L;
        String rankNumberIcon = ""; //only 1,2,3 rank
        Long i = 1L;
        if(position1.equals("ALL"))
            position1 = "%";
        if(position2.equals("ALL"))
            position2 ="%";
        if(championId1.equals("0"))
            championId1 = "%";
        if(championId2.equals("0"))
            championId2 = "%";
        List<DoubleMatchFrontEntity> doubleMatchFrontEntityList;
        doubleMatchFrontEntityList = doubleMatchFrontRepository.findAllByPositionAndChampionId(position1,championId1,position2,championId2);
        if (doubleMatchFrontEntityList == null || doubleMatchFrontEntityList.size() == 0) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
        }
        for(DoubleMatchFrontEntity doubleMatchFrontEntity : doubleMatchFrontEntityList){
            if(doubleMatchFrontEntity ==null){
                log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
                return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
            }
            MainPageChampionEntity champion1Entity = mainPageChampionRepository.findById(doubleMatchFrontEntity.getChampionId1()).orElse(null);
            MainPageChampionEntity champion2Entity = mainPageChampionRepository.findById(doubleMatchFrontEntity.getChampionId2()).orElse(null);
            String champion1Name ="";
            String champion1ImgUrl = "";
            String champion2Name="";
            String champion2ImgUrl ="";

            if (champion1Entity == null ||champion2Entity == null ) {
                log.info("챔피언 테이블에서 챔피언을 찾을 수 없습니다. Champion 테이블을 확인해주세요.  champion1Id: {} , champion2Id: {}", doubleMatchFrontEntity.getChampionId1(), doubleMatchFrontEntity.getChampionId2());
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
            MainPagePerkEntity perkEntity1 = mainPagePerkRepository.findById(doubleMatchFrontEntity.getMainRune1()).orElse(null);
            MainPagePerkEntity perkEntity2 = mainPagePerkRepository.findById(doubleMatchFrontEntity.getMainRune2()).orElse(null);
            String mainRune1 ="";
            String mainRune2 ="";
            if(perkEntity1 ==null || perkEntity2 ==null){
                log.info("룬 테이블에서 해당 룬을 찾을 수 없습니다. Perk 테이블을 확인해주세요 mainRune1: {} , mainRune2 : {}", doubleMatchFrontEntity.getMainRune1(),doubleMatchFrontEntity.getMainRune2());
                mainRune1 =  cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
                mainRune2 =  cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
            } else{
                mainRune1 = cloudFrontBaseUrl +perkEntity1.getImgUrl() + FILE_EXTENSION;
                mainRune2 = cloudFrontBaseUrl +perkEntity2.getImgUrl() + FILE_EXTENSION;
                log.info("mainRune1 Url: {} , mainRune2 Url : {}", mainRune1,mainRune2);
            }
            String position1Url = cloudFrontBaseUrl + "/mainPage/position/" + doubleMatchFrontEntity.getPosition1() + FILE_EXTENSION;
            String position2Url = cloudFrontBaseUrl + "/mainPage/position/" + doubleMatchFrontEntity.getPosition2() + FILE_EXTENSION;
            String winRate = String.format("%.2f%%", 100 * doubleMatchFrontEntity.getWinRate());
            log.info("winRate : {}", winRate);
            ChampionResponse champion1 = new ChampionResponse(champion1Name,champion1ImgUrl,mainRune1,position1Url);
            ChampionResponse champion2 = new ChampionResponse(champion2Name,champion2ImgUrl,mainRune2,position2Url);
            DoubleResponseV2 responseV2 ;

            if(!compareRequestResponse(requestPosition, doubleMatchFrontEntity.getPosition1(),requestPosition2,doubleMatchFrontEntity.getPosition2())){
                swapTrueOrFalse = true;
            }
            if( i > 3L){
                rankNumberIcon = "";
            }
            else if(doubleMatchFrontEntityList.size()>3){
                winRate = String.format("%.4f",doubleMatchFrontEntity.getWinRate());
                rankNumberIcon = cloudFrontBaseUrl+ "/mainPage/rankChange/" +i + FILE_EXTENSION; //only 1,2,3 rank
            }

            if(swapTrueOrFalse){
                responseV2 = new DoubleResponseV2(doubleMatchFrontEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        i++,rankNumberIcon,champion2,champion1,winRate);
            }
            else{
                responseV2 = new DoubleResponseV2(doubleMatchFrontEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        i++,rankNumberIcon,champion1,champion2,winRate);
            }
            doubleResponseV2List.add(responseV2);
            swapTrueOrFalse = false;
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }
    public ResponseEntity<?> getDoubleChampionInfoList(Long requestChampionId, String requestPosition, Long requestChampionId2, String requestPosition2){
        Long MINIMUM_ALL_COUNT = doubleMatchRepository.getAllCountSum().orElse(14000L) / 200L;
        boolean swapTrueOrFalse = false;
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

        String rankChangeImgUrl = cloudFrontBaseUrl + "/mainPage/rankChange/RankSame" + FILE_EXTENSION;
        Long rankChangeNumber = 0L;
        String rankNumberIcon = ""; //only 1,2,3 rank
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
            ChampionResponse champion1 = new ChampionResponse(champion1Name,champion1ImgUrl,mainRune1,position1Url);
            ChampionResponse champion2 = new ChampionResponse(champion2Name,champion2ImgUrl,mainRune2,position2Url);
            DoubleResponseV2 responseV2 ;

            if(!compareRequestResponse(requestPosition, doubleMatchEntity.getPosition1(),requestPosition2,doubleMatchEntity.getPosition2())){
                swapTrueOrFalse = true;
            }
            if( i > 3L){
                rankNumberIcon = "";
            }
            else if(doubleMatchEntityList.size()>3){
                winRate = String.format("%.4f",doubleMatchEntity.getWinRate());
                rankNumberIcon = cloudFrontBaseUrl+ "/mainPage/rankChange/" +i + FILE_EXTENSION; //only 1,2,3 rank
            }

            if(swapTrueOrFalse){
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        i++,rankNumberIcon,champion2,champion1,winRate);
            }
            else{
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        i++,rankNumberIcon,champion1,champion2,winRate);
            }
            doubleResponseV2List.add(responseV2);
            swapTrueOrFalse = false;
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }

    public ResponseEntity<?> getSoloChampionInfoListToFront(Long requestChampionId,String requestPosition){
        if (requestPosition == null || requestChampionId == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        if(!checkPositionIsValid(requestPosition)){
            return new ResponseEntity<>("404 BAD_REQUEST : 요청한 포지션의 값이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getSoloChampionInfoList - 챔피언 조합 검색. championId : {}, position : {}", requestChampionId, requestPosition);
        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        String position = requestPosition;
        String championId = String.valueOf(requestChampionId);
        Long i = 1L;
        List<SoloMatchFrontEntity> soloMatchFrontEntityList;

        if (position.equals("ALL"))
            position = "%";
        if (championId.equals("0"))
            championId = "%";
        soloMatchFrontEntityList = soloMatchFrontRepository.findAllByPositionAndChampionId(position,championId);
        if (soloMatchFrontEntityList == null || soloMatchFrontEntityList.size() == 0) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
        }
        for(SoloMatchFrontEntity soloMatchFrontEntity :soloMatchFrontEntityList ){
            SoloResponseV2 soloResponseV2 = entityToResponseParser.soloMatchFrontToSoloResponseV2(soloMatchFrontEntity,i++);
            soloResponseV2List.add(soloResponseV2);
        }
        return new ResponseEntity<>(soloResponseV2List, HttpStatus.OK);
    }

    public ResponseEntity<?> getChampionList(){
        List<ChampionEntity> championEntityList = new ArrayList<>(championRepository.findAll());
        List<Champion> championList = new ArrayList<>();
        for(ChampionEntity championEntity : championEntityList){
            String engName = championEntity.getImgUrl().substring(0,championEntity.getImgUrl().length()-4);
            Champion temp = new Champion(championEntity.getId(),championEntity.getName(),engName,
                    cloudFrontBaseUrl+ "/champion/" +championEntity.getImgUrl() + FILE_EXTENSION);
            championList.add(temp);
        }
        Collections.sort(championList);
        return new ResponseEntity<>(championList, HttpStatus.OK);
    }

}
