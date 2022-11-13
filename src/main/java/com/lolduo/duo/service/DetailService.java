package com.lolduo.duo.service;

import com.lolduo.duo.entity.detail.DoubleMatchDetailEntity;
import com.lolduo.duo.entity.detail.SoloMatchDetailEntity;
import com.lolduo.duo.parser.EntityToResponseParser;
import com.lolduo.duo.repository.DoubleMatchRepository;
import com.lolduo.duo.repository.SoloMatchRepository;
import com.lolduo.duo.repository.detail.DoubleMatchDetailRepository;
import com.lolduo.duo.repository.detail.SoloMatchDetailRepository;
import com.lolduo.duo.response.championDetail.DetailDouble;
import com.lolduo.duo.response.championDetail.DetailDoubleResponse;
import com.lolduo.duo.response.championDetail.DetailSolo;
import com.lolduo.duo.response.championDetail.DetailSoloResponse;
import com.lolduo.duo.response.championDetail.sub.DetailChampionComp;
import com.lolduo.duo.response.championDetail.sub.DetailInfo;
import com.lolduo.duo.response.championDetail.sub.DetailRankWinRate;
import com.lolduo.duo.response.championDetail.sub.sub.DetailItem;
import com.lolduo.duo.response.championDetail.sub.sub.DetailRune;
import com.lolduo.duo.response.championDetail.sub.sub.DetailSpell;
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
public class DetailService {
    private final SoloMatchDetailRepository soloMatchDetailRepository;
    private final DoubleMatchDetailRepository doubleMatchDetailRepository;

    private final EntityToResponseParser entityToResponseParser;

    public ResponseEntity<?> getDoubleChampionDetailDummy(Long dbId) {
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
        DetailRankWinRate detailRankWinRate1 = new DetailRankWinRate(1L,"70.1%");
        DetailRankWinRate detailRankWinRate2 = new DetailRankWinRate(2L,"68.1%");
        DetailRankWinRate detailRankWinRate3 = new DetailRankWinRate(3L,"76.1%");
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
        DetailRankWinRate detailRankWinRate1 = new DetailRankWinRate(1L,"70.1%");
        DetailRankWinRate detailRankWinRate2 = new DetailRankWinRate(2L,"65.1%");
        DetailRankWinRate detailRankWinRate3 = new DetailRankWinRate(3L,"60.1%");
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

    public ResponseEntity<?> getSoloChampionDetail(Long soloMatchId){
        Long MINIMUM_ALL_COUNT = soloMatchDetailRepository.getAllCountSum().orElse(200L) / 200L;
        Long championCombId =entityToResponseParser.findChampionCombBySoloMatchId(soloMatchId);
        DetailSoloResponse detailSoloResponse;
        List<SoloMatchDetailEntity> soloMatchDetailEntityList = soloMatchDetailRepository.findAllBySoloCombIdAndAllCount(championCombId,MINIMUM_ALL_COUNT);
        if(soloMatchDetailEntityList == null){
            return new ResponseEntity<>("해당 챔피언의 detail 정보를 찾을 수 없습니다.",HttpStatus.OK);
        }
        detailSoloResponse =entityToResponseParser.soloMatchDetailListToDetailResponse(championCombId,soloMatchDetailEntityList);
        return new ResponseEntity<>(detailSoloResponse,HttpStatus.OK);
    }
    public ResponseEntity<?> getDoubleChampionDetail(Long doubleMatchId){
        Long MINIMUM_ALL_COUNT = doubleMatchDetailRepository.getAllCountSum().orElse(200L) / 200L;
        Long[] championCombIdArr =entityToResponseParser.findChampionCombByDoubleMatchId(doubleMatchId);
        DetailDoubleResponse detailDoubleResponse;
        List<DoubleMatchDetailEntity> doubleMatchDetailEntityList = doubleMatchDetailRepository.findAllBySoloCombIdAndAllCount(championCombIdArr[0],championCombIdArr[1],MINIMUM_ALL_COUNT);
        if(doubleMatchDetailEntityList == null){
            return new ResponseEntity<>("해당 챔피언 조합의 detail 정보를 찾을 수 없습니다.",HttpStatus.OK);
        }
        detailDoubleResponse = entityToResponseParser.doubleMatchDetailListToDetailResponse(championCombIdArr[0],championCombIdArr[1],doubleMatchDetailEntityList);
        return new ResponseEntity<>(detailDoubleResponse,HttpStatus.OK);
    }
}
