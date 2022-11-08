package com.lolduo.duo.v2.parser;

import com.lolduo.duo.object.entity.initialInfo.ItemEntity;
import com.lolduo.duo.object.entity.initialInfo.PerkEntity;
import com.lolduo.duo.object.entity.initialInfo.SpellEntity;
import com.lolduo.duo.repository.initialInfo.ItemRepository;
import com.lolduo.duo.repository.initialInfo.PerkRepository;
import com.lolduo.duo.repository.initialInfo.SpellRepository;
import com.lolduo.duo.v2.entity.MainPageChampionEntity;
import com.lolduo.duo.v2.entity.MainPagePerkEntity;
import com.lolduo.duo.v2.entity.SoloMatchEntity;
import com.lolduo.duo.v2.entity.detail.*;
import com.lolduo.duo.v2.repository.MainPageChampionRepository;
import com.lolduo.duo.v2.repository.MainPagePerkRepository;
import com.lolduo.duo.v2.repository.detail.ItemCombRepository;
import com.lolduo.duo.v2.repository.detail.RuneCombRepository;
import com.lolduo.duo.v2.repository.detail.SoloChampionCombRepository;
import com.lolduo.duo.v2.repository.detail.SpellCombRepository;
import com.lolduo.duo.v2.response.championDetail.DetailSolo;
import com.lolduo.duo.v2.response.championDetail.DetailSoloResponse;
import com.lolduo.duo.v2.response.championDetail.sub.DetailChampionComp;
import com.lolduo.duo.v2.response.championDetail.sub.DetailInfo;
import com.lolduo.duo.v2.response.championDetail.sub.DetailRankWinRate;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailItem;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailRune;
import com.lolduo.duo.v2.response.championDetail.sub.sub.DetailSpell;
import com.lolduo.duo.v2.response.mainPage.SoloResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntityToResponseParser {
    private final String FILE_EXTENSION =".svg";
    private final String cloudFrontBaseUrl ="https://d2d4ci5rabfoyr.cloudfront.net";

    private final MainPageChampionRepository mainPageChampionRepository;
    private final MainPagePerkRepository mainPagePerkRepository;

    private final SoloChampionCombRepository soloChampionCombRepository;
    private final ItemCombRepository itemCombRepository;
    private final SpellCombRepository spellCombRepository;
    private final RuneCombRepository runeCombRepository;

    private final PerkRepository perkRepository;
    private final SpellRepository spellRepository;
    private final ItemRepository itemRepository;

    public SoloResponseV2 soloMatchToSoloResponseV2(SoloMatchEntity soloMatchEntity,Long i){
        String rankChangeImgUrl = cloudFrontBaseUrl + "/mainPage/rankChange/RankSame" + FILE_EXTENSION;
        Long rankChangeNumber = 0L;

        if (soloMatchEntity == null) {
            log.info("요청하신 챔피언 조합을 찾을 수 없습니다.");
            return null;
        }
        String championName = "";
        String championImgUrl = "";
        MainPageChampionEntity championEntity = mainPageChampionRepository.findById(soloMatchEntity.getChampionId()).orElse(null);
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
        if(i==1)
            rankChangeNumber = 10L;
        else if(i == 2)
            rankChangeNumber = 0L;
        else if(i == 3)
            rankChangeNumber = -3L;
        else
            rankChangeNumber = 0L;
        responseV2 = new SoloResponseV2(soloMatchEntity.getSoloCombId(), rankChangeImgUrl, rankChangeNumber,
                championName, championImgUrl, mainRune,
                positionUrl, winRate, i+1);
        return responseV2;
    }
    public DetailSoloResponse soloMatchDetailListToDetailResponse(Long soloChampionCombId,List<SoloMatchDetailEntity> soloMatchDetailEntityList){
        if(soloMatchDetailEntityList == null) {
            log.info("soloMatchEntity가 null 입니다.");
            return null;
        }
        SoloChampionCombEntity soloChampionCombEntity;
        DetailChampionComp detailChampionComp;
        soloChampionCombEntity =  soloChampionCombRepository.findById(soloChampionCombId).orElse(null);
        if(soloChampionCombEntity == null){
            log.info("soloChampionCombEntity가 null 입니다. solo_champion_comb 테이블을 확인해주세요, 요청 id : {}",soloChampionCombId);
            return null;
        }
        else{
            detailChampionComp = soloChampionCombToDetailChampionComp(soloChampionCombEntity);
        }
        List<DetailSolo> detailSoloList = new ArrayList<>();
        int i = 1;
        for(SoloMatchDetailEntity soloMatchDetailEntity : soloMatchDetailEntityList){
            detailSoloList.add(soloMatchDetailToDetailSolo(soloMatchDetailEntityList.get(i-1),i));
            i++;
        }
        return new DetailSoloResponse(detailChampionComp,detailSoloList);
    }
    public DetailChampionComp soloChampionCombToDetailChampionComp(SoloChampionCombEntity soloChampionCombEntity){
        String championName = "";
        String championImgUrl = "";
        MainPageChampionEntity championEntity = mainPageChampionRepository.findById(soloChampionCombEntity.getChampionId()).orElse(null);
        if (championEntity == null) {
            log.info("챔피언 테이블에서 챔피언을 찾을 수 없습니다. Champion 테이블을 확인해주세요.  championId: {}", soloChampionCombEntity.getChampionId());
            championName = "이름 없음";
            championImgUrl = cloudFrontBaseUrl + "/champion/Teemo" + FILE_EXTENSION;
        } else {
            championName = championEntity.getName();
            log.info("championName  : {}", championName);
            championImgUrl = cloudFrontBaseUrl + championEntity.getImgUrl() + FILE_EXTENSION;
            log.info("championImgUrl  : {}", championImgUrl);
        }
        MainPagePerkEntity perkEntity = mainPagePerkRepository.findById(soloChampionCombEntity.getMainRune()).orElse(null);
        String mainRuneImgUrl = "";
        if (perkEntity == null) {
            log.info("룬 테이블에서 해당 룬을 찾을 수 없습니다. Perk 테이블을 확인해주세요 mainRune: {}", soloChampionCombEntity.getMainRune());
            mainRuneImgUrl = cloudFrontBaseUrl + "/mainPage/mainRune/ArcaneComet" + FILE_EXTENSION;
        } else {
            mainRuneImgUrl = cloudFrontBaseUrl +perkEntity.getImgUrl() + FILE_EXTENSION;
            log.info("mainRune Url : {}", mainRuneImgUrl);
        }
        String positionUrl = cloudFrontBaseUrl + "/mainPage/position/" + soloChampionCombEntity.getPosition() + FILE_EXTENSION;
        DetailChampionComp detailChampionComp = new DetailChampionComp(championName,championImgUrl,mainRuneImgUrl,positionUrl);
        return detailChampionComp;
    }

    public DetailSolo soloMatchDetailToDetailSolo(SoloMatchDetailEntity soloMatchDetailEntity,int i ){
        DetailRankWinRate detailRankWinRate = CountToDetailRankWinRate(soloMatchDetailEntity.getAllCount(),soloMatchDetailEntity.getWinCount(),i);
        DetailSpell detailSpell = spellCombToDetailSpell(soloMatchDetailEntity.getSpellCombId());
        DetailRune detailRune = runeCombToDetailRune(soloMatchDetailEntity.getRuneCombId());
        DetailItem detailItem = itemCombToDetailItem(soloMatchDetailEntity.getItemCombId());
        DetailInfo detailInfo = new DetailInfo(detailSpell,detailRune,detailItem);
        return new DetailSolo(detailRankWinRate,detailInfo);
    }
    private DetailRankWinRate CountToDetailRankWinRate(Long allCount,Long winCount,int i){
        String winRate = CountToWinRate(allCount,winCount);
        String rankBadge = cloudFrontBaseUrl + "/detail/RankBadge/" + i + FILE_EXTENSION;
        return new DetailRankWinRate(rankBadge,winRate);
    }
    private String CountToWinRate(Long allCount,Long winCount){
        return String.format("%.2f%%", 100 * ((double) winCount / allCount));
    }
    private DetailSpell spellCombToDetailSpell(Long spellCombId){
        String firstSpell ="";
        String secondSpell ="";
        SpellCombEntity spellCombEntity = spellCombRepository.findById(spellCombId).orElse(null);
        if(spellCombEntity == null){
            log.info("spell_comb 테이블에서 스펠 조합을 찾을 수 없습니다. spell_comb 테이블을 확인해주세요.  요청 spell_comb_id {}",spellCombId );
        }
        else{
            SpellEntity spell1 = spellRepository.findById(spellCombEntity.getSpell1()).orElse(null);
            SpellEntity spell2 = spellRepository.findById(spellCombEntity.getSpell2()).orElse(null);
            if(spell1 ==null || spell2 ==null ){
                log.info("spell 테이블에서 스펠을 찾을 수 없습니다. spell 테이블을 확인해주세요.  요청 id1 : {}, 요청 id2 : {}"
                        ,spellCombEntity.getSpell1(),spellCombEntity.getSpell2());
            }
            else{
                firstSpell = cloudFrontBaseUrl + "/spell/"+spell1.getImgUrl();
                secondSpell = cloudFrontBaseUrl + "/spell/"+spell2.getImgUrl();
            }
        }
        return new DetailSpell(firstSpell,secondSpell);
    }
    private DetailRune runeCombToDetailRune(Long runeCombId){
        String mainRuneImgUrl ="";
        String subRuneImgUrl ="";
        List<String> mainRuneList1;
        List<String> mainRuneList2;
        List<String> mainRuneList3;
        List<String> mainRuneList4;
        List<String> subRuneList1;
        List<String> subRuneList2;
        List<String> subRuneList3;

        RuneCombEntity runeCombEntity = runeCombRepository.findById(runeCombId).orElse(null);
        if(runeCombEntity ==null){
            log.info("rune_comb 테이블에서 룬 조합을 찾을 수 없습니다. rune_comb 테이블을 확인해주세요.  요청 rune_comb_id {}",runeCombId );
            return null;
        }
        else{
            mainRuneImgUrl = getRuneImgUrlByRuneId(runeCombEntity.getMainRuneConcept());
            subRuneImgUrl = getRuneImgUrlByRuneId(runeCombEntity.getSubRuneConcept());
            List<List<Long>> mainRuneList = getRuneList(runeCombEntity.getMainRuneConcept());
            mainRuneList1 = new ArrayList<>();
            mainRuneList1.add(getRuneImgUrlByRuneId(runeCombEntity.getMainRune0()));
            mainRuneList2 = getRineImgUrlListByLongList(mainRuneList.get(0));
            mainRuneList3 = getRineImgUrlListByLongList(mainRuneList.get(1));
            mainRuneList4 = getRineImgUrlListByLongList(mainRuneList.get(2));
            List<List<Long>> subRuneList =getRuneList(runeCombEntity.getSubRuneConcept());
            subRuneList1 = getRineImgUrlListByLongList(subRuneList.get(0));
            subRuneList2 = getRineImgUrlListByLongList(subRuneList.get(1));
            subRuneList3 = getRineImgUrlListByLongList(subRuneList.get(2));
            return new DetailRune(mainRuneImgUrl,subRuneImgUrl,mainRuneList1,mainRuneList2,mainRuneList3,mainRuneList4,subRuneList1,subRuneList2,subRuneList3);
        }
    }
    private List<String> getRineImgUrlListByLongList(List<Long> runeList){
        List<String> result = new ArrayList<>();
        for(Long rundId : runeList){
            result.add(getRuneImgUrlByRuneId(rundId));
        }
        return result;
    }
    private String getRuneImgUrlByRuneId(Long rundId){
        String perkImgUrl ="";
        PerkEntity perkEntity = perkRepository.findById(rundId).orElse(null);
        if(perkEntity ==null){
            log.info("perk_id 테이블에서 스펠을 찾을 수 없습니다. perk_id 테이블을 확인해주세요.  요청 id: {}",rundId);
        }
        else {
            perkImgUrl = cloudFrontBaseUrl + "/" + perkEntity.getImgUrl();
        }
        return perkImgUrl;
    }
    private List<List<Long>> getRuneList(Long runeConceptId){
        List<List<Long>> result = new ArrayList<>();
        List<Long> rune1 = new ArrayList<>();
        List<Long> rune2 = new ArrayList<>();
        List<Long> rune3 = new ArrayList<>();
        if(runeConceptId == 8000L){
            rune1.add(9101L);rune1.add(9111L);rune1.add(8009L);
            rune2.add(9104L);rune2.add(9105L);rune2.add(9103L);
            rune3.add(8014L);rune3.add(8017L);rune3.add(8299L);
        }
        else if(runeConceptId == 8100L){
            rune1.add(8126L);rune1.add(8139L);rune1.add(8143L);
            rune2.add(8136L);rune2.add(8120L);rune2.add(8138L);
            rune3.add(8135L);rune3.add(8134L);rune3.add(8105L);rune3.add(8106L);
        }
        else if(runeConceptId == 8200L){
            rune1.add(8224L);rune1.add(8226L);rune1.add(8275L);
            rune2.add(8210L);rune2.add(8234L);rune2.add(8233L);
            rune3.add(8237L);rune3.add(8232L);rune3.add(8236L);
        }
        else if(runeConceptId == 8300L){
            rune1.add(8306L);rune1.add(8304L);rune1.add(8313L);
            rune2.add(8321L);rune2.add(8316L);rune2.add(8345L);
            rune3.add(8347L);rune3.add(8410L);rune3.add(8352L);
        }
        else if(runeConceptId ==8400L){
            rune1.add(8446L);rune1.add(8463L);rune1.add(8401L);
            rune2.add(8429L);rune2.add(8444L);rune2.add(8473L);
            rune3.add(8451L);rune3.add(8453L);rune3.add(8242L);
        }
        result.add(rune1);result.add(rune2);result.add(rune3);
        return  result;
    }
    private DetailItem itemCombToDetailItem(Long itemCombId){
        String firstItem ="";
        String secondItem ="";
        String thirdItem ="";
        ItemCombEntity itemCombEntity = itemCombRepository.findById(itemCombId).orElse(null);
        if(itemCombEntity ==null){
            log.info("item_comb 테이블에서 아이템 조합을 찾을 수 없습니다. item_comb 테이블을 확인해주세요.  요청 item_comb_id {}",itemCombId );
        }
        else{
            ItemEntity item1 = itemRepository.findById(itemCombEntity.getItem1()).orElse(null);
            ItemEntity item2 = itemRepository.findById(itemCombEntity.getItem2()).orElse(null);
            ItemEntity item3 = itemRepository.findById(itemCombEntity.getItem3()).orElse(null);
            if(item1==null || item2 ==null | item3 ==null){
                log.info("Item 테이블에서 스펠을 찾을 수 없습니다. Item 테이블을 확인해주세요.  요청 id1 : {}, 요청 id2 : {}, 요청 id3 : {}"
                        ,itemCombEntity.getItem1(),itemCombEntity.getItem2(),itemCombEntity.getItem3());
            }
            else{
                firstItem =cloudFrontBaseUrl + "/item/" + item1.getImgUrl();
                secondItem =cloudFrontBaseUrl + "/item/" + item2.getImgUrl();
                thirdItem =cloudFrontBaseUrl + "/item/" + item3.getImgUrl();
            }
        }
        return new DetailItem(firstItem,secondItem,thirdItem);
    }

}
