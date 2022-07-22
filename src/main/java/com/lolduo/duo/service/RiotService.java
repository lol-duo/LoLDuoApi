package com.lolduo.duo.service;


import com.lolduo.duo.dto.champion.ChampionDto;
import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.entity.match_v5.MatchDto;
import com.lolduo.duo.dto.setting.perk.PerkDtoList;
import com.lolduo.duo.dto.setting.perk.PerkRune;
import com.lolduo.duo.dto.spell.SpellDto;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.timeline.MatchTimeLineDto;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.entity.PerkEntity;
import com.lolduo.duo.entity.SpellEntity;
import com.lolduo.duo.entity.item.ItemEntity;
import com.lolduo.duo.entity.item.ItemFullEntity;
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

    public void setKey(String key) {
        this.key = key;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setKey("RGAPI-9d5f8dc6-7149-4d60-969f-b88fc7effc25");
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
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }
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
