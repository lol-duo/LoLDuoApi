package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.clientInfo.ICombinationInfoEntity;
import com.lolduo.duo.object.entity.clientInfo.entity.SoloInfoEntity;
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
import com.lolduo.duo.repository.clientInfo.ICombinationInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.DuoInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.QuintetInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.SoloInfoRepository;
import com.lolduo.duo.repository.clientInfo.repository.TrioInfoRepository;
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

    private final SoloInfoRepository soloInfoRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final PerkRepository perkRepository;
    private final SpellRepository spellRepository;
    private final ItemRepository itemRepository;
    private final DuoInfoRepository duoInfoRepository;
    private final TrioInfoRepository trioInfoRepository;
    private final QuintetInfoRepository quintetInfoRepository;

    public Long getAllCount(ArrayList<ChampionInfoDTO> championInfoDTOList){
        ICombinationInfoRepository repository = getInfoRepository(championInfoDTOList.size());
        if(championInfoDTOList.size()==1){
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);
            SoloInfoEntity soloInfoEntity = soloInfoRepository.findByChampionIdAndPosition(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
            if(soloInfoEntity==null){
                log.info("1명일때 soloInfoEntity null 오류!");
                return 0L;
            }
            return soloInfoEntity.getAllCount();
        }
        else{
            Map<Long, String> champPositionMap = new HashMap<Long, String>();
            TreeSet<Long> championIdSet = new TreeSet<>();
            setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
            ICombinationInfoEntity infoEntity =null;
            try {
                infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
            } catch (JsonProcessingException e) {
                log.info("objectMapper Json Parsing 오류!");
                return 0L;
            }
            return infoEntity.getAllCount();
        }
    }
    //Spell
    public List<Spell> getSpellDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        ICombinationInfoRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Spell> spellList = new ArrayList<>();
        if(championInfoDTOList.size()==1){ //1명일 때
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);
            SoloInfoEntity soloInfoEntity = soloInfoRepository.findByChampionIdAndPosition(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
            if(soloInfoEntity==null){
                log.info("1명일때 soloInfoEntity null 오류!");
                return null;
            }
            findTopK(soloInfoEntity.getSpellList(),2).forEach(spell->{
                spellList.add((Spell) spell);
            });
        }
        else{ //2명이상일 때
            Map<Long, String> champPositionMap = new HashMap<Long, String>();
            TreeSet<Long> championIdSet = new TreeSet<>();
            setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
            ICombinationInfoEntity infoEntity =null;
            try{
                infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
            } catch (JsonProcessingException e) {
                log.info("objectMapper Json Parsing 오류!");
                return null;
            }
            if(infoEntity ==null){
                log.info(" 2명이상일때 infoEntity null 오류!");
                return null;
            }
            findTopK(infoEntity.getSpellList(),2).forEach(spell ->{
                spellList.add((Spell) spell);
            });
        }
        return spellList;
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

    public List<ResponseSpell> editSpellDetail(List<Spell> spellList,ArrayList<ChampionInfoDTO> championInfoDTOList,Long allCount){
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
                    ,String.valueOf(spell.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") +" 게임"
                    , String.format("%.2f%%", 100 * ((double) spell.getWin() / allCount)));
            responseSpells.add(responseSpell);
        }
        return responseSpells;
    }
    public List<ResponseItem> editItemDetail(List<Item> itemList,ArrayList<ChampionInfoDTO> championInfoDTOList,Long allCount){
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
                    ,String.valueOf(item.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") +" 게임"
                    ,String.format("%.2f%%", 100 * ((double) item.getWin() / allCount)));
            responseItems.add(responseItem);
        }
        return responseItems;
    }
    //Item
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
    public List<Item> getItemDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        ICombinationInfoRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Item> itemList = new ArrayList<>();
        if(championInfoDTOList.size()==1){ //1명일 때
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);
            SoloInfoEntity soloInfoEntity = soloInfoRepository.findByChampionIdAndPosition(championInfoDTO.getChampionId(), championInfoDTO.getPosition()).orElse(null);
            if(soloInfoEntity==null){
                log.info("1명일때 soloInfoEntity null 오류!");
                return null;
            }

            removeAllZeroItems(soloInfoEntity);
            findTopK(getSummarizedItemList(soloInfoEntity),2).forEach(item ->{
                itemList.add((Item)item);
            });
        }
        else { //2명이상일 때
            Map<Long, String> champPositionMap = new HashMap<Long, String>();
            TreeSet<Long> championIdSet = new TreeSet<>();
            setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
            ICombinationInfoEntity infoEntity =null;
            try{
                infoEntity = repository.findByChampionIdAndPosition(objectMapper.writeValueAsString(championIdSet),objectMapper.writeValueAsString(champPositionMap)).orElse(null);
            }   catch (JsonProcessingException e) {
                log.info("objectMapper Json Parsing 오류!");
                return null;
            }
            if(infoEntity ==null){
                log.info(" 2명이상일때 infoEntity null 오류!");
                return null;
            }

            removeAllZeroItems(infoEntity);
            findTopK(getSummarizedItemList(infoEntity),2).forEach(item ->{
                itemList.add((Item)item);
            });
        }
        return itemList;
    }

    private void removeAllZeroItems(SoloInfoEntity soloInfoEntity) {
        soloInfoEntity.getItemList().removeIf(item -> {
            for (List<Long> itemIdList : item.getItemMap().values()) {
                boolean hasNonZero = false;
                for (Long itemId : itemIdList)
                    if (itemId != 0)
                        hasNonZero = true;

                if (!hasNonZero)
                    return true;
            }
            return false;
        });
    }

    private void removeAllZeroItems(ICombinationInfoEntity infoEntity) {
        infoEntity.getItemList().removeIf(item -> {
            for (List<Long> itemIdList : item.getItemMap().values()) {
                boolean hasNonZero = false;
                for (Long itemId : itemIdList)
                    if (itemId != 0)
                        hasNonZero = true;

                if (!hasNonZero)
                    return true;
            }
            return false;
        });
    }

    private List<Item> getSummarizedItemList(SoloInfoEntity soloInfoEntity) {
        Map<Map<Long, List<Long>>, Long> summarizedItemMapWinMap = new HashMap<>();
        List<Item> summarizedItemList = new ArrayList<>();

        soloInfoEntity.getItemList().forEach(item -> {
            Map<Long, List<Long>> champThreeItemMap = new HashMap<>();
            item.getItemMap().forEach((championId, wholeItemList) -> {
                champThreeItemMap.put(championId, wholeItemList.subList(0, 3));
            });

            if(summarizedItemMapWinMap.containsKey(champThreeItemMap))
                summarizedItemMapWinMap.put(champThreeItemMap, item.getWin() + summarizedItemMapWinMap.get(champThreeItemMap));
            else
                summarizedItemMapWinMap.put(champThreeItemMap, item.getWin());
        });

        summarizedItemMapWinMap.forEach((threeItemMap, winCount) -> {
            summarizedItemList.add(new Item(threeItemMap, winCount));
        });

        return summarizedItemList;
    }

    private List<Item> getSummarizedItemList(ICombinationInfoEntity infoEntity) {
        Map<Map<Long, List<Long>>, Long> summarizedItemMapWinMap = new HashMap<>();
        List<Item> summarizedItemList = new ArrayList<>();

        infoEntity.getItemList().forEach(item -> {
            Map<Long, List<Long>> champThreeItemMap = new HashMap<>();
            item.getItemMap().forEach((championId, wholeItemList) -> {
                champThreeItemMap.put(championId, wholeItemList.subList(0, 3));
            });

            if(summarizedItemMapWinMap.containsKey(champThreeItemMap))
                summarizedItemMapWinMap.put(champThreeItemMap, item.getWin() + summarizedItemMapWinMap.get(champThreeItemMap));
            else
                summarizedItemMapWinMap.put(champThreeItemMap, item.getWin());
        });

        summarizedItemMapWinMap.forEach((threeItemMap, winCount) -> {
            summarizedItemList.add(new Item(threeItemMap, winCount));
        });

        return summarizedItemList;
    }

    //Perk
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

    public List<String> perkList2UrlList(List<Long> perkList) {
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

    public List<ResponsePerk> editPerkDetail(List<Perk> perkList, ArrayList<ChampionInfoDTO> championInfoDTOList, Long allCount){
        List<ResponsePerk> responsePerks = new ArrayList<>();
        if(perkList==null || perkList.size()==0){
            log.info("perkList가 null 입니다.");
            return null;
        }

        for(Perk perk : perkList){
            List<PerkUrl> perkUrlList = new ArrayList<>();
            for(ChampionInfoDTO championInfoDTO : championInfoDTOList){
                List<String> urlList = perkList2UrlList(findMajorPerks(perk.getPerkMap().get(championInfoDTO.getChampionId())));
                PerkUrl perkUrl = new PerkUrl(urlList);
                perkUrlList.add(perkUrl);
            }
            ResponsePerk responsePerk = new ResponsePerk(perkUrlList
                    ,String.valueOf(perk.getWin()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",") + " 게임"
                    ,String.format("%.2f%%", 100 * ((double) perk.getWin() / allCount)));
            responsePerks.add(responsePerk);
        }

        return responsePerks;
    }
    public List<Perk> getPerkDetail(ArrayList<ChampionInfoDTO> championInfoDTOList){
        ICombinationInfoRepository repository = getInfoRepository(championInfoDTOList.size());
        List<Perk> perkList = new ArrayList<>();
        if(championInfoDTOList.size()==1){ //1명 일 때
            ChampionInfoDTO championInfoDTO = championInfoDTOList.get(0);
            SoloInfoEntity soloInfoEntity = soloInfoRepository.findByChampionIdAndPosition(championInfoDTO.getChampionId(),championInfoDTO.getPosition()).orElse(null);
            if(soloInfoEntity == null){
                log.info(" soloInfoEntity null 오류!");
                return null;
            }
            findTopK(soloInfoEntity.getPerkList(),2).forEach(perk ->{
                perkList.add( (Perk)perk);
            });
        }
        else { //2명이상일 때
            Map<Long, String> champPositionMap = new HashMap<Long, String>();
            TreeSet<Long> championIdSet = new TreeSet<>();
            setChampAndPositionInfo(championInfoDTOList,champPositionMap,championIdSet);
            ICombinationInfoEntity infoEntity =null;
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
            findTopK(infoEntity.getPerkList(),2).forEach(perk ->{
                perkList.add( (Perk) perk);
            });
        }
        return perkList;
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
    public void setChampAndPositionInfo(List<ChampionInfoDTO> championInfoDTOList, Map<Long, String> champPositionMap,Set<Long> championIdSet){
        for (ChampionInfoDTO championInfoDTO : championInfoDTOList) {
            champPositionMap.put(championInfoDTO.getChampionId(), championInfoDTO.getPosition());
            championIdSet.add(championInfoDTO.getChampionId());
        }
    }
    private ICombinationInfoRepository getInfoRepository(int championCount) {
        if (championCount == 1) {
            log.info("getInfoRepository() - championCount : {}, 1명", championCount);
            return null;
        }
        else if (championCount == 2) {
            log.info("getInfoRepository() - championCount : {}, 2명", championCount);
            return duoInfoRepository;
        }
        else if (championCount == 3) {
            log.info("getInfoRepository() - championCount : {}, 3명", championCount);
            return trioInfoRepository;
        }
        else if (championCount == 5) {
            log.info("getInfoRepository() - championCount : {}, 5명", championCount);
            return quintetInfoRepository;
        }
        else {
            log.info("getInfoRepository() - 요청 문제 발생");
            return null;
        }
    }
}
