package com.lolduo.duo.service;


import com.lolduo.duo.dto.champion.ChampionDto;
import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.dto.setting.perk.PerkRune;
import com.lolduo.duo.dto.spell.SpellDto;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.timeline.MatchTimeLineDto;
import com.lolduo.duo.entity.*;
import com.lolduo.duo.entity.item.ItemEntity;
import com.lolduo.duo.entity.item.ItemFullEntity;
import com.lolduo.duo.entity.match_v5.MatchDto;
import com.lolduo.duo.entity.match_v5.Participant;
import com.lolduo.duo.entity.solo.SoloEntity;
import com.lolduo.duo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RiotService implements ApplicationRunner{
    private String key;
    private String version;
    private final ItemRepository itemRepository;
    private final ItemFullRepository itemFullRepository;
    private final PerkRepository perkRepository;
    private final ChampionRepository championRepository;
    private final SpellRepository spellRepository;
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;

    public void setKey(String key) {
        this.key = key;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setKey("RGAPI-92131ab8-5ef7-4afe-a002-ce0a7a2b5ea3");
        setVersion("12.13.1");
        //setItem();
        //setChampion();
        //setSpell();
        //setPerk();
        //All();
    }

    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    private void All(){
        Long endTime = System.currentTimeMillis() / 1000;
        Long startTime = endTime - 86400;
        Map<String, List<String>> AllLeaguePuuid = new HashMap<>();

        AllLeaguePuuid.put("challenger",getPuuIdList("challenger"));
        AllLeaguePuuid.put("grandmaster",getPuuIdList("grandmaster"));
        AllLeaguePuuid.put("master",getPuuIdList("master"));

        Set<String> matchIdList = new HashSet<>();
        matchIdList.addAll(getMatchId(startTime,endTime,AllLeaguePuuid.get("challenger")));
        matchIdList.addAll(getMatchId(startTime,endTime,AllLeaguePuuid.get("grandmaster")));
        matchIdList.addAll(getMatchId(startTime,endTime,AllLeaguePuuid.get("master")));
        getMatchInfo(matchIdList);
        log.info(startTime + ": done");
    }

    private void setItem(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/item.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ItemDto> item = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ItemDto.class);
        //추후에 map iter 로 변경하여서 n번
        Set<String> itemIdList = item.getBody().getData().keySet();
        for(String itemId : itemIdList){
            itemRepository.save(new ItemEntity(Long.parseLong(itemId), item.getBody().getData().get(itemId).getName(),itemId + ".png"));
        }
        makeFullItem(item.getBody());
    }
    private void makeFullItem(ItemDto item){
        Set<String> itemIdList = item.getData().keySet();
        for(String itemId : itemIdList){
            if(item.getData().get(itemId).getInto() == null && item.getData().get(itemId).getGold().getTotal() > 1500L){
                itemFullRepository.save(new ItemFullEntity(Long.parseLong(itemId)));
            }
        }
    }
    private void getMatchInfo(Set<String> matchIdList){
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        matchIdList.forEach(matchId -> {
            final int PlayerNum = 10;
            ResponseEntity<MatchTimeLineDto> time_match = restTemplate.exchange(url + matchId + "/timeline", HttpMethod.GET, requestEntity, MatchTimeLineDto.class);
            List<List<Long>> playerItemList = new ArrayList<List<Long>>();
            for(int i =0 ; i<= PlayerNum; i++){
                playerItemList.add(new ArrayList<>());
            }

            Map<String, Long> puuIdMap = new HashMap<>();
            time_match.getBody().getInfo().getParticipants().forEach(participantDto -> {
                puuIdMap.put(participantDto.getPuuid(), participantDto.getParticipantId());
            });
            time_match.getBody().getInfo().getFrames().forEach(frameDto -> {
                frameDto.getEvents().forEach(eventDto -> {
                    if(eventDto.getType().equals("ITEM_PURCHASED") && itemFullRepository.findById(eventDto.getItemId()).orElse(null) != null){
                        playerItemList.get(eventDto.getParticipantId().intValue()).add(eventDto.getItemId());
                    }
                });
            });
            for(int i =0 ; i<= PlayerNum; i++){
                playerItemList.get(i).add(0L);
                playerItemList.get(i).add(0L);
                playerItemList.get(i).add(0L);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ResponseEntity<MatchDto> response_match = restTemplate.exchange(url + matchId, HttpMethod.GET, requestEntity, MatchDto.class);
            setSolo(response_match.getBody(),playerItemList,puuIdMap);
            //setDuo(response_match.getBody(),playerItemList,puuIdMap);
            setTrio(response_match.getBody(),playerItemList,puuIdMap);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }
    /*
    private void setDuo(MatchDto matchDto, List<List<Long>> playerItemList, Map<String,Long> puuIdMap){
        List<Participant> winList = new ArrayList<>();
        List<Participant> loseList = new ArrayList<>();
        matchDto.getInfo().getParticipants().forEach(participant -> {
            Boolean win = participant.getWin();
            if(win) winList.add(participant);
            else loseList.add(participant);
        });
        List<List<Participant>> list = new ArrayList<>();
        list.add(winList);
        list.add(loseList);
        //후에 함수화하여 list로 반환하기 필요할듯.
        list.forEach(listParticipant -> {
            for(int i = 0; i < listParticipant.size() - 1; i++){
                for(int j = i+1; j < listParticipant.size(); j++){
                    Map<Long, String> position = new HashMap<>();
                    Map<Long, List<Long>> itemList = new HashMap<>();
                    Map<Long, TreeSet<Long>> spellList = new HashMap<>();
                    Map<Long,List<Long>> perkList = new HashMap<>();
                    TreeSet<Long> champion = new TreeSet<>();
                    //champion
                    champion.add(listParticipant.get(i).getChampionId());
                    champion.add(listParticipant.get(j).getChampionId());
                    //position
                    position.put(listParticipant.get(i).getChampionId(),listParticipant.get(i).getIndividualPosition());
                    position.put(listParticipant.get(j).getChampionId(),listParticipant.get(j).getIndividualPosition());
                    //itemList
                    itemList.put(listParticipant.get(i).getChampionId(), playerItemList.get(puuIdMap.get(listParticipant.get(i).getPuuid()).intValue()));
                    itemList.put(listParticipant.get(j).getChampionId(), playerItemList.get(puuIdMap.get(listParticipant.get(j).getPuuid()).intValue()));
                    //spellList
                    TreeSet<Long> firstSpellList = new TreeSet<>();
                    TreeSet<Long> secondSpellList = new TreeSet<>();
                    firstSpellList.add(listParticipant.get(i).getSummoner1Id());
                    firstSpellList.add(listParticipant.get(i).getSummoner2Id());
                    secondSpellList.add(listParticipant.get(j).getSummoner1Id());
                    secondSpellList.add(listParticipant.get(j).getSummoner2Id());

                    spellList.put(listParticipant.get(i).getChampionId(), firstSpellList);
                    spellList.put(listParticipant.get(j).getChampionId(), secondSpellList);
                    //perkList
                    List<Long> firstPerkList = new ArrayList<>();
                    List<Long> secondPerkList = new ArrayList<>();

                    firstPerkList.add(listParticipant.get(i).getPerks().getStatPerks().getDefense());
                    firstPerkList.add(listParticipant.get(i).getPerks().getStatPerks().getOffense());
                    firstPerkList.add(listParticipant.get(i).getPerks().getStatPerks().getFlex());
                    listParticipant.get(i).getPerks().getStyles().forEach(perkStyle -> {
                        perkStyle.getSelections().forEach(perkStyleSelection -> {
                            firstPerkList.add(perkStyleSelection.getPerk());
                        });
                        firstPerkList.add(perkStyle.getStyle());
                    });
                    Collections.sort(firstPerkList);

                    secondPerkList.add(listParticipant.get(j).getPerks().getStatPerks().getDefense());
                    secondPerkList.add(listParticipant.get(j).getPerks().getStatPerks().getOffense());
                    secondPerkList.add(listParticipant.get(j).getPerks().getStatPerks().getFlex());
                    listParticipant.get(j).getPerks().getStyles().forEach(perkStyle -> {
                        perkStyle.getSelections().forEach(perkStyleSelection -> {
                            secondPerkList.add(perkStyleSelection.getPerk());
                        });
                        secondPerkList.add(perkStyle.getStyle());
                    });
                    Collections.sort(secondPerkList);

                    perkList.put(listParticipant.get(i).getParticipantId(), firstPerkList);
                    perkList.put(listParticipant.get(j).getParticipantId(), secondPerkList);

                    duoRepository.save(new DuoEntity(listParticipant.get(i).getWin(),position,itemList,spellList,champion,perkList));
                }
            }
        });

    }
     */
    private void setSolo(MatchDto matchDto, List<List<Long>> playerItemList, Map<String, Long> puuIdMap){
        matchDto.getInfo().getParticipants().forEach(participant -> {
            Boolean win = participant.getWin();
            String position = participant.getIndividualPosition();
            List<Long> itemList = playerItemList.get(puuIdMap.get(participant.getPuuid()).intValue());
            TreeSet<Long> spellList = new TreeSet<>();
            spellList.add(participant.getSummoner1Id());
            spellList.add(participant.getSummoner2Id());
            Long champion = participant.getChampionId();
            List<Long> perkList = new ArrayList<>();
            perkList.add(participant.getPerks().getStatPerks().getDefense());
            perkList.add(participant.getPerks().getStatPerks().getOffense());
            perkList.add(participant.getPerks().getStatPerks().getFlex());
            participant.getPerks().getStyles().forEach(perkStyle -> {
                perkStyle.getSelections().forEach(perkStyleSelection -> {
                    perkList.add(perkStyleSelection.getPerk());
                });
                perkList.add(perkStyle.getStyle());
            });
            Collections.sort(perkList);
            soloRepository.save(new SoloEntity(win,position,itemList,spellList,champion,perkList));
        });
    }

    private void setTrio(MatchDto matchDto, List<List<Long>> playerItemList, Map<String, Long> puuIdMap) {
        Map<String,Boolean> visitedWin =new HashMap<>();
        Map<String,Boolean> visitedLose =new HashMap<>();
        matchDto.getInfo().getParticipants().forEach(participant -> {
            if(participant.getWin()==true) visitedWin.put(participant.getPuuid(),false);
            else visitedLose.put(participant.getPuuid(),false);
        });
        //2인 정보
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedWin,true,2);
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedLose,false,2);
        //3인 정보
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedWin,true,3);
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedLose,false,3);
        //5인 정보
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedWin,true,5);
        combination(matchDto,playerItemList,puuIdMap,new ArrayList<>(),visitedLose,false,5);
    }
    private void combination(MatchDto matchDto, List<List<Long>> playerItemList, Map<String, Long> puuIdMap,List<Participant> participantList,Map<String,Boolean> visited,Boolean win,int number){
        if(participantList.size()==number){
            saveTrio(participantList,playerItemList,puuIdMap,win,number);
            return;
        }
        matchDto.getInfo().getParticipants().forEach(participant -> {
            if (visited.containsKey(participant.getPuuid())==true && visited.get(participant.getPuuid())==false) {
                visited.put(participant.getPuuid(),true);
                participantList.add(participant);
                visited.put(participant.getPuuid(),false);
            }
        });
    }
    private void saveTrio(List<Participant> participantList, List<List<Long>> playerItemList, Map<String, Long> puuIdMap, Boolean win,int number){
        Map<Long,String> positionMap = new HashMap<>();
        Map<Long,List<Long>> itemListMap = new HashMap<>();
        Map<Long,TreeSet<Long>> spellListMap = new HashMap<>();
        TreeSet<Long> championList = new TreeSet<>();
        Map<Long,List<Long>> perkListMap = new HashMap<>();

        // 중복 start
        participantList.forEach(participant -> {
            String position = participant.getIndividualPosition();
            List<Long> itemList = playerItemList.get(puuIdMap.get(participant.getPuuid()).intValue());
            TreeSet<Long> spellList = new TreeSet<>();
            spellList.add(participant.getSummoner1Id());
            spellList.add(participant.getSummoner2Id());
            Long champion = participant.getChampionId();
            List<Long> perkList = new ArrayList<>();
            perkList.add(participant.getPerks().getStatPerks().getDefense());
            perkList.add(participant.getPerks().getStatPerks().getOffense());
            perkList.add(participant.getPerks().getStatPerks().getFlex());
            participant.getPerks().getStyles().forEach(perkStyle -> {
                perkStyle.getSelections().forEach(perkStyleSelection -> {
                    perkList.add(perkStyleSelection.getPerk());
                });
                perkList.add(perkStyle.getStyle());
            });
            Collections.sort(perkList);
            positionMap.put(champion,position);
            itemListMap.put(champion,itemList);
            spellListMap.put(champion,spellList);
            championList.add(champion);
            perkListMap.put(champion,perkList);
        });
        // 중복 end
        if(number==2){
            duoRepository.save(new DuoEntity(win,positionMap,itemListMap,spellListMap,championList,perkListMap) );
        }
        else if(number==3){
            trioRepository.save(new TrioEntity(win,positionMap,itemListMap,spellListMap,championList,perkListMap));
        }
        else if(number==5){
            //trioRepository.save(new TrioEntity(win,positionMap,itemListMap,spellListMap,championList,perkListMap));
            //team 저장 추가 예정.
        }

    }
    private List<String> getPuuIdList(String league){
        String url = "https://kr.api.riotgames.com/lol/league/v4/"+league+"leagues/by-queue/RANKED_SOLO_5x5";
        String url_summoner = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/";
        List<String> puuidList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<LeagueListDTO> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LeagueListDTO.class);

        response.getBody().getEntries().forEach(leagueItemDTO -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            puuidList.add(restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class).getBody().getPuuid());
        });
        return puuidList;
    }

    private Set<String> getMatchId(Long startTime, Long endTime, List<String> puuidList) {
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/";
        Set<String> matchList = new HashSet<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        puuidList.forEach(puuid -> {
            ResponseEntity<List> response = restTemplate.exchange(url + puuid + "/ids?startTime=" + startTime+ "&endTime=" + endTime + "&type=ranked&start=0&count=100", HttpMethod.GET, requestEntity, List.class);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            matchList.addAll(response.getBody());
        });
        return matchList;
    }

    private void setPerk(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/runesReforged.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<PerkDto[]> response_perkDtoList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PerkDto[].class);
        List<PerkDto> perkDtoList = Arrays.asList(response_perkDtoList.getBody());
        perkDtoList.forEach(perkDto -> {
            for(int i = 0; i < perkDto.getSlots().size(); i++){
                List<PerkRune> perkRuneList = perkDto.getSlots().get(i).getRunes();
                for(int j = 0; j < perkRuneList.size(); j++){
                    perkRepository.save(new PerkEntity(perkRuneList.get(j).getId(),perkRuneList.get(j).getName(), perkRuneList.get(j).getIcon()));
                }
            }
            perkRepository.save(new PerkEntity(perkDto.getId(), perkDto.getName(), perkDto.getIcon()));
        });

    }
    private void setChampion(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/champion.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ChampionDto> championList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ChampionDto.class);

        Set<String> championIdList = championList.getBody().getData().keySet();
        for(String championId : championIdList){
            championRepository.save(new ChampionEntity(Long.parseLong(championList.getBody().getData().get(championId).getKey()), championList.getBody().getData().get(championId).getName(),championId + ".png"));
        }
    }
    private void setSpell(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/summoner.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SpellDto> spellList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpellDto.class);

        Set<String> spellIdList = spellList.getBody().getData().keySet();
        for(String spellId : spellIdList){
            if(Integer.parseInt(spellList.getBody().getData().get(spellId).getKey()) > 21) continue;
            spellRepository.save(new SpellEntity(Long.parseLong(spellList.getBody().getData().get(spellId).getKey()), spellList.getBody().getData().get(spellId).getName(),spellId + ".png"));
        }
    }
}
