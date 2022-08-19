package com.lolduo.duo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;
import com.lolduo.duo.object.entity.clientInfo.sub.Item;
import com.lolduo.duo.object.entity.clientInfo.sub.Perk;
import com.lolduo.duo.object.entity.clientInfo.sub.Spell;
import com.lolduo.duo.object.entity.clientInfo.sub.Sub;
import com.lolduo.duo.object.entity.initialInfo.PerkEntity;
import com.lolduo.duo.object.response.championDetail2.*;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.initialInfo.ChampionRepository;
import com.lolduo.duo.repository.initialInfo.ItemRepository;
import com.lolduo.duo.repository.initialInfo.PerkRepository;
import com.lolduo.duo.repository.initialInfo.SpellRepository;
import com.lolduo.duo.service.temp.PerkUrlMap;
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
    private PerkUrlMap perkUrlMap =new PerkUrlMap();


    public List<ResponseSpell2> makeSpellList(List<Spell> spellList,Long ChampionId){
        List<ResponseSpell2> responseSpellList = new ArrayList<>();

        for(Spell spell : spellList){
            String winRate = String.valueOf(spell.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";
            String AllCount =String.format("%.2f%%", 100 * ((double) spell.getWin() / spell.getAllCount()));
            List<String> spellUrlList = new ArrayList<>();
            for (Long spellId : spell.getSpellMap().get(ChampionId)) {
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
        List<Perk> perkList =new ArrayList<>();
        findTopK(combiEntity.getPerkList(),2).forEach(perk->{
            perkList.add((Perk)perk);
        });
        return perkList;
    }
    public List<ResponsePerk2> makePerkList(List<Perk> perkList, Long ChampionId,Long mainPerkId,Long subPerkId){
        List<ResponsePerk2> responsePerkList = new ArrayList<>();
        for(Perk perk : perkList){
            String winRate = String.valueOf(perk.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";
            String AllCount =String.format("%.2f%%", 100 * ((double) perk.getWin() / perk.getAllCount()));
            ResponsePerk2 temp = initResponsePerk(mainPerkId,subPerkId,perk.getPerkMap().get(ChampionId),winRate,AllCount);
            responsePerkList.add(temp);
        }
        return responsePerkList;
    }
    public ResponsePerk2 initResponsePerk(Long MainPerkId, Long SecondaryPekrId,List<Long> perkList,String winRate,String allCount){
        String baseUrl ="https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/";
        ResponsePerk2 result =new ResponsePerk2();

        perkList.remove(MainPerkId);
        perkList.remove(SecondaryPekrId);

        result.setAllCount(allCount);
        result.setWinRate(winRate);
        log.info("initResponsePerk - 메인 룬 ID : " + MainPerkId + " , 보조 룬 ID : " + SecondaryPekrId);
        String mainPerkUrl = perkRepository.findById(MainPerkId).get().getImgUrl();
        result.setMainPerkUrl(baseUrl+ mainPerkUrl);
        String subPerkUrl = perkRepository.findById(SecondaryPekrId).get().getImgUrl();
        result.setSubPerkUrl(baseUrl+subPerkUrl);

        List<PerkUrlMap.PerkCheck> mainList = new ArrayList<>();
        mainList.addAll(perkUrlMap.getMainPerkMap().get(MainPerkId));
        List<PerkUrlMap.PerkCheck> subList  = new ArrayList<>();
        subList.addAll(perkUrlMap.getSecondaryPerkMap().get(SecondaryPekrId));
        List<PerkUrlMap.PerkCheck> subSubList = new ArrayList<>();
        subSubList.addAll(perkUrlMap.getSubSubPerkList());
        for(Long perkId : perkList){
            String perkUrl ="";
            PerkEntity perkEntity= perkRepository.findById(perkId).orElse(null);
            if(perkEntity==null) {
                log.info("initResponsePerk - perkEntity 가 NULL입니다. 따로 perk를 활성화 하지않습니다. perkId : {}",perkId);
            }
            else{
                perkUrl = baseUrl+perkEntity.getImgUrl()+"_disabled.png";
                log.info("initResponsePerk - perkUrl : {}",perkUrl);
                boolean isFind =false;
                for(int i = 0 ; i <mainList.size();i++){
                    int index = mainList.get(i).getPerkList().indexOf(perkUrl);
                    if(index!=-1){
                        isFind=true;
                        mainList.get(i).getPerkList().set(index,mainList.get(i).getPerkList().get(index).substring(0,mainList.get(i).getPerkList().get(index).length()-13));
                        break;
                    }
                }
                if(isFind)
                    continue;
                for(int i = 0 ; i <subList.size();i++){
                    int index = subList.get(i).getPerkList().indexOf(perkUrl);
                    if(index!=-1){
                        isFind=true;
                        subList.get(i).getPerkList().set(index, subList.get(i).getPerkList().get(index).substring(0,subList.get(i).getPerkList().get(index).length()-13)) ;
                        break;
                    }
                }
                if(isFind)
                    continue;
                for(int i = 0 ; i <subSubList.size();i++){
                    int index = subSubList.get(i).getPerkList().indexOf(perkUrl);
                    if(index!=-1){
                        subList.get(i).getPerkList().set(index,subSubList.get(i).getPerkList().get(index).substring(0,subList.get(i).getPerkList().get(index).length()-13));
                        break;
                    }
                }
            }
        }
        log.info("initResponsePerk - mainList size : " + mainList.size());
        result.setKeyPerkUrlList(mainList.get(0).getPerkList());
        result.setMain1UrlList(mainList.get(1).getPerkList());
        result.setMain2UrlList(mainList.get(2).getPerkList());
        result.setMain3UrlList(mainList.get(3).getPerkList());

        log.info("initResponsePerk - subsubList size : " + subSubList.size());
        result.setSubsub1UrlList(subSubList.get(0).getPerkList());
        result.setSubsub2UrlListl(subSubList.get(1).getPerkList());
        result.setSubsub3UrlList(subSubList.get(2).getPerkList());

        log.info("initResponsePerk - subList size : " + subList.size());
        result.setSub1UrlList(subList.get(0).getPerkList());
        result.setSub2UrlList(subList.get(1).getPerkList());
        result.setSub3UrlList(subList.get(2).getPerkList());


        return result;
    }
    public List<ResponseItem2> makeItemList(List<Item> itemList,Long ChampionId){
        List<ResponseItem2> responseItemList = new ArrayList<>();
        for(Item item : itemList){
            String winRate = String.valueOf(item.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임";
            String AllCount =String.format("%.2f%%", 100 * ((double) item.getWin() / item.getAllCount()));
            List<String> itemUrlList = new ArrayList<>();
            for(Long itemId : item.getItemMap().get(ChampionId).subList(0,3)){
                String itemName = itemRepository.findById(itemId).get().getImgUrl();
                if(itemName==null){
                    log.info("makeSpellList -> spellEntitiy가 없습니다. DB 및 spellId를 확인하세요. itemId : {} \n기본값인 3330.png(허수아비)로 초기화합니다." ,itemId );
                    itemName="3330.png";
                }
                itemUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/" + itemName);
            }
            ResponseItem2 temp = new ResponseItem2(winRate,AllCount,itemUrlList);
            responseItemList.add(temp);
        }
        return responseItemList;
    }
    public List<Item> pickItemList(@NotNull ICombiEntity combiEntity){
        List<Item> itemList =new ArrayList<>();
        removeItemsAmountUnderThree(combiEntity);
        findTopK(combiEntity.getItemList(),3).forEach(item ->
                itemList.add((Item)item));
        return itemList;
    }
    private void removeItemsAmountUnderThree(ICombiEntity infoEntity) {
        infoEntity.getItemList().removeIf(item -> {
            for (List<Long> itemIdList : item.getItemMap().values()) {
                int count = 0;
                for (Long itemId : itemIdList) {
                    if (itemId == 0)
                        break;
                    else
                        count++;
                }

                if (count < 3)
                    return true;
            }
            return false;
        });
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

}
