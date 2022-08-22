package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.clientInfo.ICombiEntity;
import com.lolduo.duo.object.entity.clientInfo.sub.Item;
import com.lolduo.duo.object.entity.clientInfo.sub.Perk;
import com.lolduo.duo.object.entity.clientInfo.sub.Spell;
import com.lolduo.duo.object.entity.clientInfo.sub.Sub;
import com.lolduo.duo.object.entity.initialInfo.ItemEntity;
import com.lolduo.duo.object.entity.initialInfo.PerkEntity;
import com.lolduo.duo.object.entity.initialInfo.SpellEntity;
import com.lolduo.duo.object.response.championDetail.ResponseItem;
import com.lolduo.duo.object.response.championDetail.ResponseSpell;
import com.lolduo.duo.object.response.championDetail.sub.ItemUrl;
import com.lolduo.duo.object.response.championDetail.sub.PerkUrl;
import com.lolduo.duo.object.response.championDetail.ResponsePerk;
import com.lolduo.duo.object.response.championDetail.sub.SpellUrl;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.initialInfo.ItemRepository;
import com.lolduo.duo.repository.initialInfo.PerkRepository;
import com.lolduo.duo.repository.initialInfo.SpellRepository;
import com.lolduo.duo.service.temp.PrimaryPerkMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChampionDetailComponent {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PerkRepository perkRepository;
    private final SpellRepository spellRepository;
    private final ItemRepository itemRepository;

    private final SoloCombiRepository soloCombiRepository;
    private final DoubleCombiRepository doubleCombiRepository;
    private final TripleCombiRepository tripleCombiRepository;
    private final PentaCombiRepository pentaCombiRepository;

    //Spell
    public List<Spell> getSpellDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        ICombiRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Spell> spellList = new ArrayList<>();

        Map<Long, String> champPositionMap = new HashMap<Long, String>();
        TreeSet<Long> championIdSet = new TreeSet<>();
        setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
        ICombiEntity infoEntity =null;
        try{
            infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
        } catch (JsonProcessingException e) {
            log.info("getSpellDetail - objectMapper Json Parsing 오류!");
            return null;
        }
        if(infoEntity ==null){
            log.info("getSpellDetail - infoEntity null 오류!");
            return null;
        }
        findTopK(infoEntity.getSpellList(),2).forEach(spell ->
            spellList.add((Spell) spell)
        );
        return spellList;
    }

    public List<ResponseSpell> editSpellDetail(List<Spell> spellList,ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ResponseSpell> responseSpells = new ArrayList<>();
        if(spellList==null || spellList.size()==0){
            log.info("spellList가 null 입니다.");
            return null;
        }
        for(Spell spell : spellList){
            List<SpellUrl> spellUrlList = new ArrayList<>();
            for(ChampionInfoDTO championInfoDTO : championInfoDTOList){
                List<String> urlList = findSpell(spell.getSpellMap().get(championInfoDTO.getChampionId()));
                SpellUrl spellUrl = new SpellUrl(urlList);
                spellUrlList.add(spellUrl);
            }
            ResponseSpell responseSpell = new ResponseSpell(spellUrlList
                    ,String.valueOf(spell.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                    , String.format("%.2f%%", 100 * ((double) spell.getWin() / spell.getAllCount())));
            responseSpells.add(responseSpell);
        }
        return responseSpells;
    }

    public List<String> findSpell(TreeSet<Long> spellSet){
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/spell/";
        List<String> urlList = new ArrayList<>();
        for(Long spellId : spellSet){
            SpellEntity spellEntity = spellRepository.findById(spellId).orElse(new SpellEntity(0L,"No Spell","https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/X.png"));
            urlList.add(baseUrl+spellEntity.getImgUrl());
        }
        return urlList;
    }

    //Item
    public List<Item> getItemDetail(ArrayList<ChampionInfoDTO> championInfoDTOList, boolean summarize){
        ICombiRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Item> topItemList = new ArrayList<>();

        Map<Long, String> champPositionMap = new HashMap<Long, String>();
        TreeSet<Long> championIdSet = new TreeSet<>();
        setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
        ICombiEntity infoEntity =null;
        try{
            infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
        }   catch (JsonProcessingException e) {
            log.info("getItemDetail - objectMapper Json Parsing 오류!");
            return null;
        }
        if(infoEntity ==null){
            log.info("getItemDetail - infoEntity null 오류!");
            return null;
        }

        removeItemsAmountUnderNumber(infoEntity, 3);
        List<Item> allItemList = summarize? getSummarizedItemList(infoEntity) : infoEntity.getItemList();
        findTopK(allItemList,2).forEach(item ->
            topItemList.add((Item)item)
        );
        return topItemList;
    }

    public List<ResponseItem> editItemDetail(List<Item> itemList,ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ResponseItem> responseItems = new ArrayList<>();
        if(itemList==null || itemList.size()==0){
            log.info("itemList 가 null 입니다.");
            return null;
        }
        for(Item item : itemList){
            List<ItemUrl> itemUrlList = new ArrayList<>();
            for(ChampionInfoDTO championInfoDTO : championInfoDTOList){
                List<String> urlList = findItem(item.getItemMap().get(championInfoDTO.getChampionId()));
                ItemUrl itemUrl = new ItemUrl(urlList);
                itemUrlList.add(itemUrl);
            }
            ResponseItem responseItem = new ResponseItem(itemUrlList
                    ,String.valueOf(item.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                    ,String.format("%.2f%%", 100 * ((double) item.getWin() / item.getAllCount())));
            responseItems.add(responseItem);
        }
        return responseItems;
    }

    public List<String> findItem(List<Long> itemList){
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/item/";
        List<String> urlList = new ArrayList<>();
        for(int i = 0 ; i < 3; i++){
            Long itemId = itemList.get(i);
            ItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
            if(itemEntity == null)
                break;
            if(i != 0)
                urlList.add(baseUrl+"next.png");
            urlList.add(baseUrl+itemEntity.getImgUrl());
        }
        return urlList;
    }

    private void removeItemsAmountUnderNumber(ICombiEntity infoEntity, int number) {
        infoEntity.getItemList().removeIf(item -> {
            for (List<Long> itemIdList : item.getItemMap().values()) {
                int count = 0;
                for (Long itemId : itemIdList) {
                    if (itemId == 0)
                        break;
                    else
                        count++;
                }

                if (count < number)
                    return true;
            }
            return false;
        });
    }

    private List<Item> getSummarizedItemList(ICombiEntity infoEntity) {
        Map<Map<Long, List<Long>>, List<Long>> itemCombiAndWinAllCountsMap = new HashMap<>();
        List<Item> summarizedItemList = new ArrayList<>();

        infoEntity.getItemList().forEach(item -> {
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

    //Perk
    public List<Perk> getPerkDetail(ArrayList<ChampionInfoDTO> championInfoDTOList, boolean summarize){
        ICombiRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Perk> topPerkList = new ArrayList<>();

        Map<Long, String> champPositionMap = new HashMap<Long, String>();
        TreeSet<Long> championIdSet = new TreeSet<>();
        setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
        ICombiEntity infoEntity =null;
        try {
            infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
        } catch (JsonProcessingException e) {
            log.info("objectMapper Json Parsing 오류!");
            return null;
        }
        if(infoEntity ==null){
            log.info(" infoEntity null 오류!");
            return null;
        }

        List<Perk> allPerkList = summarize? getSummarizedPerkList(infoEntity) : infoEntity.getPerkList();
        findTopK(allPerkList,2).forEach(perk ->
            topPerkList.add( (Perk) perk)
        );

        return topPerkList;
    }

    public List<ResponsePerk> editPerkDetail(List<Perk> perkList, ArrayList<ChampionInfoDTO> championInfoDTOList){
        List<ResponsePerk> responsePerks = new ArrayList<>();
        if(perkList==null || perkList.size()==0){
            log.info("perkList가 null 입니다.");
            return null;
        }

        for(Perk perk : perkList){
            List<PerkUrl> perkUrlList = new ArrayList<>();
            for(ChampionInfoDTO championInfoDTO : championInfoDTOList){
                List<String> urlList = perkIdList2UrlList(findMajorPerks(perk.getPerkMap().get(championInfoDTO.getChampionId())));
                PerkUrl perkUrl = new PerkUrl(urlList);
                perkUrlList.add(perkUrl);
            }
            ResponsePerk responsePerk = new ResponsePerk(perkUrlList
                    ,String.valueOf(perk.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                    ,String.format("%.2f%%", 100 * ((double) perk.getWin() / perk.getAllCount())));
            responsePerks.add(responsePerk);
        }

        return responsePerks;
    }

    public List<Long> findMajorPerks(List<Long> perkList){
        log.info("findMajorPerks - perkList: {}", perkList.toString());
        List<Long> majorPerkList = new ArrayList<>();

        PrimaryPerkMap primaryPerkMap = new PrimaryPerkMap();
        List<Long> secondaryPerk = new LinkedList<>();
        Long primaryPerk = 0L;
        Long keystonePerk = 0L;

        for (Long perk : perkList) {
            if (perk >= 8000L && perk % 100 == 0)
                secondaryPerk.add(perk);
            else if (primaryPerkMap.getPrimaryMap().containsKey(perk))
                keystonePerk = perk;
        }
        primaryPerk = primaryPerkMap.getPrimaryMap().get(keystonePerk);
        if(primaryPerk == null) primaryPerk = 0L; //Exception handling

        secondaryPerk.remove(primaryPerk);
        if(secondaryPerk.size() < 1) secondaryPerk.add(0L); //Exception handling
        log.info("findMajorPerks - primaryPerk: {}, keystonePerk: {}, secondaryPerk: {}", primaryPerk, keystonePerk, secondaryPerk.get(0));

        majorPerkList.add(primaryPerk);
        majorPerkList.add(keystonePerk);
        majorPerkList.add(secondaryPerk.get(0));

        return majorPerkList;
    }

    public List<String> perkIdList2UrlList(List<Long> perkList) {
        String baseUrl = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/";
        List<String> urlList = new ArrayList<>();

        perkList.forEach(perk -> {
            PerkEntity perkEntity = perkRepository.findById(perk).orElse(null);
            if (perkEntity == null) {
                urlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/X.png");
                log.info("findPrimaryAndSecondaryPerk - perkEntity가 null 입니다.");
            }
            else
                urlList.add(baseUrl + perkEntity.getImgUrl());
        });

        return urlList;
    }

    private List<Perk> getSummarizedPerkList(ICombiEntity infoEntity) {
        Map<Map<Long, List<Long>>, List<Long>> perkCombiMatchRecordMap = new HashMap<>();
        List<Perk> summarizedPerkList = new ArrayList<>();

        infoEntity.getPerkList().forEach(perk -> {
            Map<Long, List<Long>> champMajorPerkMap = new HashMap<>();
            perk.getPerkMap().forEach((championId, wholePerkList) -> {
                champMajorPerkMap.put(championId, findMajorPerks(wholePerkList));
            });

            if(perkCombiMatchRecordMap.containsKey(champMajorPerkMap)) {
                List<Long> matchRecord = perkCombiMatchRecordMap.get(champMajorPerkMap);
                matchRecord.set(0, matchRecord.get(0) + perk.getWin());
                matchRecord.set(1, matchRecord.get(1) + perk.getAllCount());
            }
            else {
                List<Long> matchRecord = new ArrayList<>(2);
                matchRecord.add(perk.getWin());
                matchRecord.add(perk.getAllCount());
                perkCombiMatchRecordMap.put(champMajorPerkMap, matchRecord);
            }
        });

        perkCombiMatchRecordMap.forEach((majorPerkMap, matchRecord) -> {
            summarizedPerkList.add(new Perk(majorPerkMap, matchRecord.get(0), matchRecord.get(1)));
        });

        return summarizedPerkList;
    }

    //Common
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
    public void setChampAndPositionInfo(List<ChampionInfoDTO> championInfoDTOList, Map<Long, String> champPositionMap,Set<Long> championIdSet){
        for (ChampionInfoDTO championInfoDTO : championInfoDTOList) {
            champPositionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            championIdSet.add(championInfoDTO.getChampionId());
        }
    }
    private ICombiRepository getInfoRepository(int championCount) {
        if (championCount == 1) {
            log.info("getInfoRepository() - championCount : {}, 1명", championCount);
            return soloCombiRepository;
        }
        else if (championCount == 2) {
            log.info("getInfoRepository() - championCount : {}, 2명", championCount);
            return doubleCombiRepository;
        }
        else if (championCount == 3) {
            log.info("getInfoRepository() - championCount : {}, 3명", championCount);
            return tripleCombiRepository;
        }
        else if (championCount == 5) {
            log.info("getInfoRepository() - championCount : {}, 5명", championCount);
            return pentaCombiRepository;
        }
        else {
            log.info("getInfoRepository() - 요청 문제 발생");
            return null;
        }
    }
}
