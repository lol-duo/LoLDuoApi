package com.lolduo.duo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;
import com.lolduo.duo.object.entity.clientInfo.sub.Item;
import com.lolduo.duo.object.entity.clientInfo.sub.Perk;
import com.lolduo.duo.object.entity.clientInfo.sub.Spell;
import com.lolduo.duo.object.entity.clientInfo.sub.Sub;
import com.lolduo.duo.object.response.championDetail2.*;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.initialInfo.ChampionRepository;
import com.lolduo.duo.repository.initialInfo.ItemRepository;
import com.lolduo.duo.repository.initialInfo.PerkRepository;
import com.lolduo.duo.repository.initialInfo.SpellRepository;
import com.lolduo.duo.service.temp.PerkFormationMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChampionDetailComponent2 {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PerkRepository perkRepository;
    private final SpellRepository spellRepository;
    private final ItemRepository itemRepository;

    private final ChampionRepository championRepository;
    private final SoloCombiRepository soloCombiRepository;
    private final DoubleCombiRepository doubleCombiRepository;
    private final TripleCombiRepository tripleCombiRepository;
    private final PentaCombiRepository pentaCombiRepository;
    private PerkFormationMap perkFormationMap = new PerkFormationMap();

    public List<ResponseSpell2> makeSpellList(List<Spell> spellList,Long championId){
        List<ResponseSpell2> responseSpellList = new ArrayList<>();
        List<Set<Long>> addedSpellIdSetList = new ArrayList<>();
        Map<Set<Long>, List<Long>> spellIdSetAndWinAllCountsMap = new HashMap<>();

        for(Spell spell : spellList){
            if (!spellIdSetAndWinAllCountsMap.containsKey(spell.getSpellMap().get(championId))) {
                List<Long> winAllCounts = new ArrayList<>();
                winAllCounts.add(spell.getWin());
                winAllCounts.add(spell.getAllCount());
                spellIdSetAndWinAllCountsMap.put(spell.getSpellMap().get(championId), winAllCounts);
            }
            else {
                List<Long> winAllCounts = spellIdSetAndWinAllCountsMap.get(spell.getSpellMap().get(championId));
                winAllCounts.set(0, winAllCounts.get(0) + spell.getWin());
                winAllCounts.set(1, winAllCounts.get(1) + spell.getAllCount());
            }
        }

        for(Spell spell : spellList){
            if (!addedSpellIdSetList.contains(spell.getSpellMap().get(championId))) {
                addedSpellIdSetList.add(spell.getSpellMap().get(championId));

                List<Long> winAllCounts = spellIdSetAndWinAllCountsMap.get(spell.getSpellMap().get(championId));
                String winRate = String.format("%.2f%%", 100 * ((double) winAllCounts.get(0) / winAllCounts.get(1)));
                String AllCount = String.valueOf(winAllCounts.get(1)).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";

                List<String> spellUrlList = new ArrayList<>();
                for (Long spellId : spell.getSpellMap().get(championId)) {
                    String spellName = spellRepository.findById(spellId).get().getImgUrl();
                    if(spellName==null){
                        log.info("makeSpellList -> spellEntitiy가 없습니다. DB 및 spellId를 확인하세요. spellId : {} \n기본값인 애쉬Q값으로 초기화합니다." ,spellId );
                        spellName="AsheQ.png";
                    }
                    spellUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/" +spellName);
                }
                ResponseSpell2 temp = new ResponseSpell2(winRate,AllCount,spellUrlList);
                responseSpellList.add(temp);
            }
        }
        return responseSpellList;
    }
    public List<Spell> pickSpellList(@NotNull ICombiEntity combiEntity){
        List<Spell> spellList = new ArrayList<>();
        findTopK(combiEntity.getSpellList(),2).forEach(spell ->
                spellList.add((Spell) spell)
        );
        return spellList;
    }
    public List<Perk> pickPerkList(@NotNull ICombiEntity combiEntity){
        List<Perk> perkList = new ArrayList<>();
        findTopK(combiEntity.getPerkList(),2).forEach(perk->{
            perkList.add((Perk)perk);
        });
        return perkList;
    }
    public List<ResponsePerk2> makePerkList(List<Perk> perkList, Long ChampionId, Long mainPerkId, Long subPerkId){
        List<ResponsePerk2> responsePerkList = new ArrayList<>();
        for(Perk perk : perkList){
            String winRate = String.format("%.2f%%", 100 * ((double) perk.getWin() / perk.getAllCount()));
            String AllCount =String.valueOf(perk.getAllCount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";
            ResponsePerk2 temp = initResponsePerk(mainPerkId,subPerkId,perk.getPerkMap().get(ChampionId),winRate,AllCount);
            responsePerkList.add(temp);
        }
        return responsePerkList;
    }

    public ResponsePerk2 initResponsePerk(Long MainPerkId, Long SecondaryPerkId, List<Long> activePerkList, String winRate, String allCount){
        String baseUrl ="https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/";
        ResponsePerk2 result = new ResponsePerk2();

        activePerkList.remove(MainPerkId);
        activePerkList.remove(SecondaryPerkId);

        result.setAllCount(allCount);
        result.setWinRate(winRate);
        log.info("initResponsePerk - 메인 룬 ID : " + MainPerkId + " , 보조 룬 ID : " + SecondaryPerkId);
        String mainPerkUrl = perkRepository.findById(MainPerkId).get().getImgUrl();
        result.setMainPerkUrl(baseUrl + mainPerkUrl);
        String subPerkUrl = perkRepository.findById(SecondaryPerkId).get().getImgUrl();
        result.setSubPerkUrl(baseUrl + subPerkUrl);

        List<PerkFormationMap.PerkCheck> mainPerkRowList = perkFormationMap.getMainPerkMap().get(MainPerkId);
        List<PerkFormationMap.PerkCheck> subPerkRowList = perkFormationMap.getSecondaryPerkMap().get(SecondaryPerkId);
        List<PerkFormationMap.PerkCheck> statModRowList = perkFormationMap.getStatModList();

        mainPerkRowList.forEach(perkCheck -> perkCheck.initActivePerkIndex(activePerkList));
        result.setKeyPerkUrlList(mainPerkRowList.get(0).getPerkUrlListDisableApplied());
        result.setMain1UrlList(mainPerkRowList.get(1).getPerkUrlListDisableApplied());
        result.setMain2UrlList(mainPerkRowList.get(2).getPerkUrlListDisableApplied());
        result.setMain3UrlList(mainPerkRowList.get(3).getPerkUrlListDisableApplied());

        subPerkRowList.forEach(perkCheck -> perkCheck.initActivePerkIndex(activePerkList));
        result.setSub1UrlList(subPerkRowList.get(0).getPerkUrlListDisableApplied());
        result.setSub2UrlList(subPerkRowList.get(1).getPerkUrlListDisableApplied());
        result.setSub3UrlList(subPerkRowList.get(2).getPerkUrlListDisableApplied());

        disableInactiveStatMod(statModRowList, activePerkList);
        result.setSubsub1UrlList(statModRowList.get(0).getPerkUrlListDisableApplied());
        result.setSubsub2UrlList(statModRowList.get(1).getPerkUrlListDisableApplied());
        result.setSubsub3UrlList(statModRowList.get(2).getPerkUrlListDisableApplied());

        return result;
    }
    public List<ResponseItem2> makeItemList(List<Item> itemList,Long championId){
        List<ResponseItem2> responseItemList = new ArrayList<>();
        List<List<Long>> addedItemIdListList = new ArrayList<>();
        Map<List<Long>, List<Long>> itemIdListAndWinAllCountsMap = new HashMap<>();

        for(Item item : itemList){
            if (!itemIdListAndWinAllCountsMap.containsKey(item.getItemMap().get(championId))) {
                List<Long> winAllCounts = new ArrayList<>();
                winAllCounts.add(item.getWin());
                winAllCounts.add(item.getAllCount());
                itemIdListAndWinAllCountsMap.put(item.getItemMap().get(championId), winAllCounts);
            }
            else {
                List<Long> winAllCounts = itemIdListAndWinAllCountsMap.get(item.getItemMap().get(championId));
                winAllCounts.set(0, winAllCounts.get(0) + item.getWin());
                winAllCounts.set(1, winAllCounts.get(1) + item.getAllCount());
            }
        }

        for(Item item : itemList){
            if (!addedItemIdListList.contains(item.getItemMap().get(championId))) {
                addedItemIdListList.add(item.getItemMap().get(championId));

                List<Long> winAllCounts = itemIdListAndWinAllCountsMap.get(item.getItemMap().get(championId));
                String winRate = String.format("%.2f%%", 100 * ((double) winAllCounts.get(0) / winAllCounts.get(1)));
                String AllCount = String.valueOf(winAllCounts.get(1)).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";

                List<String> itemUrlList = new ArrayList<>();
                for (Long itemId : item.getItemMap().get(championId)) {
                    String itemName = itemRepository.findById(itemId).get().getImgUrl();
                    if (itemName == null) {
                        log.info("makeSpellList -> spellEntitiy가 없습니다. DB 및 spellId를 확인하세요. itemId : {} \n기본값인 3330.png(허수아비)로 초기화합니다.", itemId);
                        itemName = "3330.png";
                    }
                    itemUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/" + itemName);
                }
                ResponseItem2 temp = new ResponseItem2(winRate, AllCount, itemUrlList);
                responseItemList.add(temp);
            }
        }
        return responseItemList;
    }
    public List<Item> pickItemList(@NotNull ICombiEntity combiEntity){
        List<Item> summarizedItemList = new ArrayList<>();
        findTopK(getSummarizedItemList(getItemListWhoseItemsNotUnderK(combiEntity.getItemList(), 2)),3).forEach(item ->
                summarizedItemList.add((Item)item));
        return summarizedItemList;
    }
    private List<Item> getSummarizedItemList(List<Item> originalItemList) {
        Map<Map<Long, List<Long>>, List<Long>> itemCombiAndWinAllCountsMap = new HashMap<>();
        List<Item> summarizedItemList = new ArrayList<>();

        originalItemList.forEach(item -> {
            Map<Long, List<Long>> champThreeItemMap = new HashMap<>();
            item.getItemMap().forEach((championId, wholeItemList) ->
                    champThreeItemMap.put(championId, wholeItemList.subList(0, 3))
            );

            if(itemCombiAndWinAllCountsMap.containsKey(champThreeItemMap)) {
                List<Long> winAndAllCounts = itemCombiAndWinAllCountsMap.get(champThreeItemMap);
                winAndAllCounts.set(0, winAndAllCounts.get(0) + item.getWin());
                winAndAllCounts.set(1, winAndAllCounts.get(1) + item.getAllCount());
            }
            else {
                List<Long> winAndAllCounts = new ArrayList<>(2);
                winAndAllCounts.add(item.getWin());
                winAndAllCounts.add(item.getAllCount());
                itemCombiAndWinAllCountsMap.put(champThreeItemMap, winAndAllCounts);
            }
        });

        itemCombiAndWinAllCountsMap.forEach((threeItemMap, winAndAllCounts) ->
                summarizedItemList.add(new Item(threeItemMap, winAndAllCounts.get(0), winAndAllCounts.get(1)))
        );

        return summarizedItemList;
    }
    private List<Item> getItemListWhoseItemsNotUnderK(List<Item> originalItemList, int k) {
        List<Item> resultItemList = new ArrayList<>(originalItemList);
        resultItemList.removeIf(item -> {
            for (List<Long> itemIdList : item.getItemMap().values()) {
                int count = 0;
                for (Long itemId : itemIdList) {
                    if (itemId == 0)
                        break;
                    else
                        count++;
                }

                if (count < k)
                    return true;
            }
            return false;
        });

        return resultItemList;
    }
    public Map<Long,String> initChampionPositionMap(ArrayList<ChampionInfoDTO> championInfoDTOList){
        Map<Long,String> champPositionMap = new HashMap<>();
        for(ChampionInfoDTO championInfoDTO : championInfoDTOList){
            champPositionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
        }
        return champPositionMap;
    }

    public List<? extends Sub> findTopK(List<? extends  Sub> input , int k){
        PriorityQueue<Sub> maxHeap = new PriorityQueue<>();
        for (Sub element : input) {
            maxHeap.add(element);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }
        List<Sub> topList = new ArrayList<>(maxHeap);
        Collections.reverse(topList);
        return topList;
    }

    private void disableInactiveStatMod(List<PerkFormationMap.PerkCheck> statModRowList, List<Long> activePerkList) {
        List<Long> activeStatModList = activePerkList.subList(0, 3); // StatMod가 activePerkList의 앞의 3가지 원소인 경우라 가정하고 사용. 업데이트로 statmod의 id가 변경될 경우 수정 필요.
        log.info("disableInactiveStatMod - 활성화된 statMod ID : {}", activeStatModList.toString());
        if (activeStatModList.contains(5001L)) { // ? - ? - 5001
            statModRowList.get(2).initActivePerkIndexWithId(5001L);
            if (activeStatModList.contains(5005L)) { // 5005 - ? - 5001
                statModRowList.get(0).initActivePerkIndexWithId(5005L);
                statModRowList.get(1).initActivePerkIndex(activeStatModList);
            }
            else if (activeStatModList.contains(5007L)) { // 5007 - ? - 5001
                statModRowList.get(0).initActivePerkIndexWithId(5007L);
                statModRowList.get(1).initActivePerkIndex(activeStatModList);
            }
            else { // 5008 - ? - 5001
                statModRowList.get(0).initActivePerkIndexWithId(5008L);
                if (activeStatModList.contains(5002L))
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                else if (activeStatModList.contains(5003L))
                    statModRowList.get(1).initActivePerkIndexWithId(5003L);
                else
                    statModRowList.get(1).initActivePerkIndexWithId(5008L);
            }
        }
        else { // ? - ? - ?
            if (activeStatModList.contains(5005L)) { // 5005 - ? - ?
                statModRowList.get(0).initActivePerkIndexWithId(5005L);
                if (activeStatModList.contains(5008L)) { // 5005 - 5008 - ?
                    statModRowList.get(1).initActivePerkIndexWithId(5008L);
                    statModRowList.get(2).initActivePerkIndex(activeStatModList);
                }
                else if (Collections.frequency(activeStatModList, 5002L) == 2) {
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5002L);
                }
                else if (Collections.frequency(activeStatModList, 5003L) == 2) {
                    statModRowList.get(1).initActivePerkIndexWithId(5003L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
                else {
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
            }
            else if (activeStatModList.contains(5007L)) { // 5007 - ? - ?
                statModRowList.get(0).initActivePerkIndexWithId(5007L);
                if (activeStatModList.contains(5008L)) { // 5007 - 5008 - ?
                    statModRowList.get(1).initActivePerkIndexWithId(5008L);
                    statModRowList.get(2).initActivePerkIndex(activeStatModList);
                }
                else if (Collections.frequency(activeStatModList, 5002L) == 2) {
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5002L);
                }
                else if (Collections.frequency(activeStatModList, 5003L) == 2) {
                    statModRowList.get(1).initActivePerkIndexWithId(5003L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
                else {
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
            }
            else { // 5008 - ? - ?
                statModRowList.get(0).initActivePerkIndexWithId(5008L);
                if (Collections.frequency(activeStatModList, 5008L) == 2) { // 5008 - 5008 - ?
                    statModRowList.get(1).initActivePerkIndexWithId(5008L);
                    statModRowList.get(2).initActivePerkIndex(activeStatModList);
                }
                else if (Collections.frequency(activeStatModList, 5002L) == 2) { // 5008 - 5002 - 5002
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5002L);
                }
                else if (Collections.frequency(activeStatModList, 5003L) == 2) { // 5008 - 5003 - 5003
                    statModRowList.get(1).initActivePerkIndexWithId(5003L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
                else { // 5008 - 5002 - 5003 or // 5008 - 5003 - 5002 (둘을 하나로 통일)
                    statModRowList.get(1).initActivePerkIndexWithId(5002L);
                    statModRowList.get(2).initActivePerkIndexWithId(5003L);
                }
            }
        }
    }
}
