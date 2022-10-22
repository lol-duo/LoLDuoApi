package com.lolduo.duo.v2;

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
    public ResponseEntity<?> getDoubleChampionInfoList(Long championId, String position, Long championId2, String position2){
        Long MINIMUM_ALL_COUNT = doubleMatchRepository.getAllCountSum().orElse(240000L) / 1000L;
        List<DoubleResponseV2> doubleResponseV2List = new ArrayList<>();
        if(championId == null || position == null || championId2 == null || position2==null){
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getChampionInfoList - 챔피언 조합 검색. champion1Id : {}, position1 : {},  champion2Id : {}, position2 : {}", championId, position,championId2,position2);

        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);

    }
    public ResponseEntity<?> getSoloChampionInfoList(Long requestChampionId,String requestPosition) {
        Long MINIMUM_ALL_COUNT = soloMatchRepository.getAllCountSum().orElse(240000L) / 1000L;
        List<SoloResponseV2> soloResponseV2List = new ArrayList<>();
        if (requestPosition == null || requestChampionId == null) {
            return new ResponseEntity<>("404 BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        log.info("v2/getChampionInfoList - 챔피언 조합 검색. championId : {}, position : {}", requestChampionId, requestPosition);
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
        Long combiId = 0L;
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
        for(Long i = 0L ; i < 100L;i++ ){
            if(i<3){
                rankNumberIcon = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/logo/Group.svg";
                rankNumberColor = "C8AA6E";
            }
            else{
                rankNumberIcon = "";
                rankNumberColor = "";
            }
            DoubleResponseV2 doubleResponseV2 = new DoubleResponseV2(combiId,rankChangeImgUrl,rankChangeNumber,rankChangeColor,rankNumber+i,rankNumberIcon,rankNumberColor,
                    champion1Name,champion1ImgUrl,mainRune1ImgUrl,position1ImgUrl,listImage1,champion2Name,champion2ImgUrl,mainRune2ImgUrl,position2ImgUrl,listImage2,winRate);
            doubleResponseV2List.add(doubleResponseV2);
        }
        return new ResponseEntity<>(doubleResponseV2List, HttpStatus.OK);
    }
}
