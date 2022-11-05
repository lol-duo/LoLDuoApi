package com.lolduo.duo.v2;

import com.lolduo.duo.v2.entity.DoubleMatchEntity;
import com.lolduo.duo.v2.entity.MainPageChampionEntity;
import com.lolduo.duo.v2.entity.MainPagePerkEntity;
import com.lolduo.duo.v2.entity.SoloMatchEntity;
import com.lolduo.duo.v2.repository.DoubleMatchRepository;
import com.lolduo.duo.v2.repository.MainPageChampionRepository;
import com.lolduo.duo.v2.repository.MainPagePerkRepository;
import com.lolduo.duo.v2.repository.SoloMatchRepository;
import com.lolduo.duo.v2.response.ChampionResponse;
import com.lolduo.duo.v2.response.championDetail.*;
import com.lolduo.duo.v2.response.championDetail.sub.DetailChampionComp;
import com.lolduo.duo.v2.response.championDetail.sub.DetailInfo;
import com.lolduo.duo.v2.response.championDetail.sub.DetailRankWinRate;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailItem;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailRune;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailSpell;
import com.lolduo.duo.v2.response.mainPage.DoubleResponseV2;
import com.lolduo.duo.v2.response.mainPage.SoloResponseV2;
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
        return position.equals("ALL") || position.equals("MIDDLE") || position.equals("TOP") || position.equals("UTILITY") || position.equals("JUNGLE") || position.equals("BOTTOM");
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
        String rankChangeNumber = "";
        String rankChangeColor = "";
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
            } else{
                rankNumberIcon = cloudFrontBaseUrl+ "/mainPage/rankChange/" +i + FILE_EXTENSION; //only 1,2,3 rank

            }
            if(swapTrueOrFalse){
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        rankChangeColor, i++,rankNumberIcon,champion2,champion1,winRate);
            }
            else{
                responseV2 = new DoubleResponseV2(doubleMatchEntity.getId(),rankChangeImgUrl,rankChangeNumber,
                        rankChangeColor, i++,rankNumberIcon,champion1,champion2,winRate);
            }
            doubleResponseV2List.add(responseV2);
            swapTrueOrFalse = false;
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }

    private boolean compareRequestResponse(String requestPosition1, String responsePosition1,String requestPosition2, String responsePosition2 ){
        return requestPosition1.equals(responsePosition1) || requestPosition2.equals(responsePosition2);
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
            if (i > 3) {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionUrl, winRate, i++);
            } else {
                responseV2 = new SoloResponseV2(soloMatchEntity.getId(), rankChangeImgUrl, rankChangeNumber,
                        rankNumberColor, championName, championImgUrl, mainRune,
                        positionUrl, winRate, i++);
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

        String champion1Name = "티모";
        String champion1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Teemo.svg";
        String mainRune1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg";
        String position1ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/MIDDLE.svg";

        String champion2Name = "직스";
        String champion2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Ziggs.svg";
        String mainRune2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/mainRune/ArcaneComet.svg";
        String position2ImgUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/mainPage/position/BOTTOM.svg";

        String winRate ="67.2%";
        for(Long i = 0L ; i < 30L;i++ ){
            if(i<3){
                rankNumberIcon = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/logo/Group.svg";
            }
            else{
                rankNumberIcon = "";
            }
            ChampionResponse champion1= new ChampionResponse(champion1Name,champion1ImgUrl,mainRune1ImgUrl,position1ImgUrl);
            ChampionResponse champion2= new ChampionResponse(champion2Name,champion2ImgUrl,mainRune2ImgUrl,position2ImgUrl);
            DoubleResponseV2 doubleResponseV2 = new DoubleResponseV2(id,rankChangeImgUrl,rankChangeNumber,rankChangeColor,rankNumber+i,rankNumberIcon,champion1, champion2, winRate);
            doubleResponseV2List.add(doubleResponseV2);
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }
    public ResponseEntity<?> getDoubleChampionDetailDummy(Long dbId1,Long dbId2) {
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/";
        String championName = "미스 포츈";
        String championImgUrl  = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/champion/MissFortune.svg";
        String mainRuneImgUrl = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/mainRune/LethalTempoTemp.svg";
        String positionImgUrl = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/position/BOTTOM.svg";
        DetailChampionComp detailChampionComp = new DetailChampionComp(championName,championImgUrl,mainRuneImgUrl,positionImgUrl);
        DetailSpell detailSpell = new DetailSpell("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/SummonerFlash.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/SummonerExhaust.png");
        DetailItem detailItem = new DetailItem("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3068.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3065.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3075.png");
        List<String> mainRuneList1 = new ArrayList<>();
        mainRuneList1.add(baseUrl + "Domination/main/DisabledPressTheAttack.svg");
        mainRuneList1.add(baseUrl + "Domination/main/LethalTempo.svg");
        mainRuneList1.add(baseUrl + "Domination/main/DisabledFleetFootwork.svg");
        mainRuneList1.add(baseUrl + "Domination/main/DisabledConqueror.svg");


        List<String> mainRuneList2= new ArrayList<>();
        mainRuneList2.add(baseUrl + "Precision/1/Overheal.svg");
        mainRuneList2.add(baseUrl + "Precision/1/PresenceofMind.svg");
        mainRuneList2.add(baseUrl + "Precision/1/Triumph.svg");


        List<String> mainRuneList3= new ArrayList<>();
        mainRuneList3.add(baseUrl + "Precision/2/Alarcrity.svg");
        mainRuneList3.add(baseUrl + "Precision/2/Tenacity.svg");
        mainRuneList3.add(baseUrl + "Precision/2/Bloodline.svg");


        List<String> mainRuneList4= new ArrayList<>();
        mainRuneList4.add(baseUrl + "Precision/3/CoupdeGrace.svg");
        mainRuneList4.add(baseUrl + "Precision/3/CutDown.svg");
        mainRuneList4.add(baseUrl + "Precision/3/LastStand.svg");


        List<String> subRuneList1= new ArrayList<>();
        subRuneList1.add(baseUrl + "Domination/1/CheapShot.svg");
        subRuneList1.add(baseUrl + "Domination/1/TasteofBlood.svg");
        subRuneList1.add(baseUrl + "Domination/1/SuddenImpact.svg");
        List<String> subRuneList2= new ArrayList<>();
        subRuneList2.add(baseUrl + "Domination/2/ZombieWard.svg");
        subRuneList2.add(baseUrl + "Domination/2/GhostPoro.svg");
        subRuneList2.add(baseUrl + "Domination/2/EyeballCollection.svg");
        List<String> subRuneList3= new ArrayList<>();
        subRuneList3.add(baseUrl + "Domination/3/TreausreHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/IngeniousHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/RelentlessHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/UltimateHunter.svg");

        DetailRune detailRune = new DetailRune("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/Precision/Precision.svg",
                "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/Domination/Domination.svg",
                mainRuneList1,  mainRuneList2,  mainRuneList3,  mainRuneList4,  subRuneList1, subRuneList2, subRuneList3);
        DetailInfo detailInfo =new DetailInfo(detailSpell,detailRune,detailItem);
        DetailRankWinRate detailRankWinRate1 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/1.svg","70.1%");
        DetailRankWinRate detailRankWinRate2 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/2.svg","68.1%");
        DetailRankWinRate detailRankWinRate3 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/3.svg","76.1%");
        DetailDouble detailDouble1 =new DetailDouble(detailRankWinRate1,detailInfo,detailInfo);
        DetailDouble detailDouble2 =new DetailDouble(detailRankWinRate2,detailInfo,detailInfo);
        DetailDouble detailDouble3 =new DetailDouble(detailRankWinRate3,detailInfo,detailInfo);
        List<DetailDouble> detailDoubleList = new ArrayList<>();
        detailDoubleList.add(detailDouble1);
        detailDoubleList.add(detailDouble2);
        detailDoubleList.add(detailDouble3);
        DetailDoubleResponse detailDoubleResponse = new DetailDoubleResponse(detailChampionComp,detailChampionComp,detailDoubleList);
        return new ResponseEntity<>(detailDoubleResponse, HttpStatus.OK);
    }
    public ResponseEntity<?> getSoloChampionDetailDummy(Long dbId){
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/";
        String championName = "미스 포츈";
        String championImgUrl  = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/champion/MissFortune.svg";
        String mainRuneImgUrl = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/mainRune/LethalTempoTemp.svg";
        String positionImgUrl = "https://d2d4ci5rabfoyr.cloudfront.net/mainPage/position/BOTTOM.svg";
        DetailChampionComp detailChampionComp = new DetailChampionComp(championName,championImgUrl,mainRuneImgUrl,positionImgUrl);
        DetailSpell detailSpell = new DetailSpell("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/SummonerFlash.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/SummonerExhaust.png");
        DetailItem detailItem = new DetailItem("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3068.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3065.png","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/3075.png");
        List<String> mainRuneList1 = new ArrayList<>();
        mainRuneList1.add(baseUrl + "Domination/main/DisabledPressTheAttack.svg");
        mainRuneList1.add(baseUrl + "Domination/main/LethalTempo.svg");
        mainRuneList1.add(baseUrl + "Domination/main/DisabledFleetFootwork.svg");
        mainRuneList1.add(baseUrl + "Domination/main/DisabledConqueror.svg");


        List<String> mainRuneList2= new ArrayList<>();
        mainRuneList2.add(baseUrl + "Precision/1/Overheal.svg");
        mainRuneList2.add(baseUrl + "Precision/1/PresenceofMind.svg");
        mainRuneList2.add(baseUrl + "Precision/1/Triumph.svg");


        List<String> mainRuneList3= new ArrayList<>();
        mainRuneList3.add(baseUrl + "Precision/2/Alarcrity.svg");
        mainRuneList3.add(baseUrl + "Precision/2/Tenacity.svg");
        mainRuneList3.add(baseUrl + "Precision/2/Bloodline.svg");


        List<String> mainRuneList4= new ArrayList<>();
        mainRuneList4.add(baseUrl + "Precision/3/CoupdeGrace.svg");
        mainRuneList4.add(baseUrl + "Precision/3/CutDown.svg");
        mainRuneList4.add(baseUrl + "Precision/3/LastStand.svg");


        List<String> subRuneList1= new ArrayList<>();
        subRuneList1.add(baseUrl + "Domination/1/CheapShot.svg");
        subRuneList1.add(baseUrl + "Domination/1/TasteofBlood.svg");
        subRuneList1.add(baseUrl + "Domination/1/SuddenImpact.svg");
        List<String> subRuneList2= new ArrayList<>();
        subRuneList2.add(baseUrl + "Domination/2/ZombieWard.svg");
        subRuneList2.add(baseUrl + "Domination/2/GhostPoro.svg");
        subRuneList2.add(baseUrl + "Domination/2/EyeballCollection.svg");
        List<String> subRuneList3= new ArrayList<>();
        subRuneList3.add(baseUrl + "Domination/3/TreausreHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/IngeniousHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/RelentlessHunter.svg");
        subRuneList3.add(baseUrl + "Domination/3/UltimateHunter.svg");

        DetailRune detailRune = new DetailRune("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/Precision/Precision.svg",
                "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/Rune/Domination/Domination.svg",
                 mainRuneList1,  mainRuneList2,  mainRuneList3,  mainRuneList4,  subRuneList1, subRuneList2, subRuneList3);

        DetailInfo detailInfo =new DetailInfo(detailSpell,detailRune,detailItem);
        DetailRankWinRate detailRankWinRate1 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/1.svg","70.1%");
        DetailRankWinRate detailRankWinRate2 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/2.svg","65.1%");
        DetailRankWinRate detailRankWinRate3 = new DetailRankWinRate("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/detail/RankBadge/3.svg","60.1%");
        DetailSolo detailSolo1 = new DetailSolo(detailRankWinRate1,detailInfo);
        DetailSolo detailSolo2 = new DetailSolo(detailRankWinRate2,detailInfo);
        DetailSolo detailSolo3 = new DetailSolo(detailRankWinRate3,detailInfo);

        List<DetailSolo> detailSoloList = new ArrayList<>();
        detailSoloList.add(detailSolo1);
        detailSoloList.add(detailSolo2);
        detailSoloList.add(detailSolo3);
        DetailSoloResponse detailSoloResponse = new DetailSoloResponse(detailChampionComp,detailSoloList);
        return new ResponseEntity<>(detailSoloResponse, HttpStatus.OK);
    }
}
