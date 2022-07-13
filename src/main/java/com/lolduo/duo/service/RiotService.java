package com.lolduo.duo.service;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.dto.item.DataDto;
import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.match_v5.MatchDto;
import com.lolduo.duo.dto.match_v5.ObjectivesDto;
import com.lolduo.duo.dto.match_v5.PerkStyleDto;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.dto.setting.perk.PerkRune;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.timeline.MatchTimeLineDto;
import com.lolduo.duo.entity.*;
import com.lolduo.duo.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

@Service
@Slf4j
public class RiotService {

    private final UserIdRepository userIdRepository;
    private final MatchIdRepository matchIdRepository;
    private final SoloRepository soloRepository;
    private final TierRepository tierRepository;
    private final PerkRepository perkRepository;
    private final PerkAllRepository perkAllRepository;
    private final ItemRepository itemRepository;
    private final ChampionRepository championRepository;
    private final SpellRepository spellRepository;
    private final SpellCombinationRepository spellCombinationRepository;
    private final ItemFullRepository itemFullRepository;

    private final DuoRepository duoRepository;

    public RiotService(UserIdRepository userIdRepository, MatchIdRepository matchIdRepository, SoloRepository soloRepository, TierRepository tierRepository, PerkRepository perkRepository, PerkAllRepository perkAllRepository, ItemRepository itemRepository, ChampionRepository championRepository, SpellRepository spellRepository, SpellCombinationRepository spellCombinationRepository, ItemFullRepository itemFullRepository, DuoRepository duoRepository) {
        this.userIdRepository = userIdRepository;
        this.matchIdRepository = matchIdRepository;
        this.soloRepository = soloRepository;
        this.tierRepository = tierRepository;
        this.perkRepository = perkRepository;
        this.perkAllRepository = perkAllRepository;
        this.itemRepository = itemRepository;
        this.championRepository = championRepository;
        this.spellRepository = spellRepository;
        this.spellCombinationRepository = spellCombinationRepository;
        this.itemFullRepository = itemFullRepository;
        this.duoRepository = duoRepository;
    }

    public void getChallengerList(String key, String startTime, String endTime){
        String url_challengerId = "https://kr.api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<LeagueListDTO> response = restTemplate.exchange(url_challengerId, HttpMethod.GET, requestEntity, LeagueListDTO.class);

        String url_summoner = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/";
        response.getBody().getEntries().forEach(leagueItemDTO -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("서머너 아이디" + leagueItemDTO.getSummonerId());
            log.info("이름" + leagueItemDTO.getSummerName());
            userIdRepository.save(new UserIdEntity(restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class).getBody().getPuuid(), leagueItemDTO.getSummonerId(), tierRepository.findById(1L).orElse(null)));
        });

        String url_grandMaster = "https://kr.api.riotgames.com/lol/league/v4/grandmasterleagues/by-queue/RANKED_SOLO_5x5";
        response = restTemplate.exchange(url_grandMaster, HttpMethod.GET, requestEntity, LeagueListDTO.class);
        response.getBody().getEntries().forEach(leagueItemDTO -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userIdRepository.save(new UserIdEntity(restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class).getBody().getPuuid(), leagueItemDTO.getSummonerId(), tierRepository.findById(2L).orElse(null)));
        });

        String url_master = "https://kr.api.riotgames.com/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5";
        response = restTemplate.exchange(url_master, HttpMethod.GET, requestEntity, LeagueListDTO.class);
        response.getBody().getEntries().forEach(leagueItemDTO -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userIdRepository.save(new UserIdEntity(restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class).getBody().getPuuid(), leagueItemDTO.getSummonerId(), tierRepository.findById(3L).orElse(null)));
        });

        getMatchId(key,startTime,endTime);
    }

    public void getMatchId(String key, String startTime, String endTime) {
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/";
        List<UserIdEntity> userIdEntity = userIdRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        userIdEntity.forEach(userIdEntity1 -> {
            ResponseEntity<List> response = restTemplate.exchange(url + userIdEntity1.getPuuid() + "/ids?startTime=" + startTime+ "&endTime=" + endTime + "&type=ranked&start=0&count=100", HttpMethod.GET, requestEntity, List.class);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            response.getBody().forEach(s -> {
                try{
                    matchIdRepository.save(new MatchIdEntity(null, s.toString(),startTime));
                }
                catch (DataIntegrityViolationException e){
                }
            });
        });
    }

    public void setPerk(List<PerkDto> perkDtoList){
        perkRepository.deleteAll();
        perkDtoList.forEach(perkDto -> {
            Long type = 2L;
            for(int i = 0; i < perkDto.getSlots().size(); i++){
                List<PerkRune> perkRuneList = perkDto.getSlots().get(i).getRunes();
                for(int j = 0; j < perkRuneList.size(); j++){
                    perkRepository.save(new PerkEntity(perkRuneList.get(j).getId(),perkRuneList.get(j).getName(), perkRuneList.get(j).getIcon(), (i + 2L),perkDto.getId() ));
                }
            }
            perkRepository.save(new PerkEntity(perkDto.getId(), perkDto.getName(), perkDto.getIcon(), 1L, null));
        });
    }
    public void setItem(ItemDto item){
        itemRepository.deleteAll();
        //추후에 map iter 로 변경하여서 n번
        Set<String> s = item.getData().keySet();
        for(String str : s){
            itemRepository.save(new ItemEntity(Long.parseLong(str), item.getData().get(str).getName(),str + ".png"));
        }
        makeFullItem(item);
    }

    public void setChampion(ItemDto item){
        championRepository.deleteAll();
        Set<String> s = item.getData().keySet();
        for(String str : s){
            championRepository.save(new ChampionEntity(Long.parseLong(item.getData().get(str).getKey()), item.getData().get(str).getName(),str + ".png"));
        }
    }
    public void setSpell(ItemDto item){
        spellRepository.deleteAll();
        Set<String> s = item.getData().keySet();
        for(String str : s){
            if(Integer.parseInt(item.getData().get(str).getKey()) > 21) continue;
            spellRepository.save(new SpellEntity(Long.parseLong(item.getData().get(str).getKey()), item.getData().get(str).getName(),str + ".png"));
        }
        makeSpell();
    }
    public void makeSpell(){
        List<SpellEntity> spellEntityList = spellRepository.findAll();
        for(int i = 0; i < spellEntityList.size() - 1; i++){
            for(int j = i + 1; j < spellEntityList.size(); j++){
                spellCombinationRepository.save(new SpellCombinationEntity(null, spellEntityList.get(i), spellEntityList.get(j)));
            }
        }
    }

    public void makeFullItem(ItemDto item){
        itemFullRepository.deleteAll();
        Set<String> s = item.getData().keySet();
        for(String str : s){
            if(item.getData().get(str).getInto() == null && item.getData().get(str).getGold().getTotal() > 1000L){
                itemFullRepository.save(new ItemFullEntity(Long.parseLong(str)));
            }
        }
    }

    public void makeMatchInfo(String key, String start){
        List<MatchIdEntity> matchIdEntityList = matchIdRepository.findAllByStartTime(start);
        List<SpellEntity> spellEntityList = spellRepository.findAll();
        Map<Long, SpellEntity> spMap = new HashMap<>();
        spellEntityList.forEach(spellEntity -> {
            spMap.put(spellEntity.getId(), spellEntity);
        });

        String url_matchId = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        matchIdEntityList.forEach(matchIdEntity -> {
            ResponseEntity<MatchTimeLineDto> time_match = restTemplate.exchange(url_matchId + matchIdEntity.getMatchId() + "/timeline", HttpMethod.GET, requestEntity, MatchTimeLineDto.class);
            List<List<Long>> listList = new ArrayList<List<Long>>();
            for(int i =0 ; i< 11; i++){
                listList.add(new ArrayList<>());
            }
            Map<String, Long> puMap = new HashMap<>();
            time_match.getBody().getInfo().getParticipants().forEach(participantDto -> {
                puMap.put(participantDto.getPuuid(), participantDto.getParticipantId());
            });
            time_match.getBody().getInfo().getFrames().forEach(frameDto -> {
                frameDto.getEvents().forEach(eventDto -> {
                    if(eventDto.getType().equals("ITEM_PURCHASED") && itemFullRepository.findById(eventDto.getItemId()).orElse(null) != null){
                        listList.get(eventDto.getParticipantId().intValue()).add(eventDto.getItemId());
                    }
                });
            });
            solo(matchIdEntity, key, listList, puMap, spMap);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void solo(MatchIdEntity matchIdEntity, String key, List<List<Long>> listList, Map<String, Long> puMap, Map<Long, SpellEntity> spMap){
        log.info(listList.toString());
        List<PerkEntity> perkEntityList = perkRepository.findAll();
        Map<Long, PerkEntity> pMap = new HashMap<>();
        perkEntityList.forEach(perkEntity -> {
            pMap.put(perkEntity.getId(),perkEntity);
        });

        String url_matchId = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<MatchDto> response_match = restTemplate.exchange(url_matchId + matchIdEntity.getMatchId(), HttpMethod.GET, requestEntity, MatchDto.class);

        response_match.getBody().getInfo().getParticipants().forEach(participantDto -> {
            PerkStyleDto pStyle = participantDto.getPerks().getStyles().get(0);
            PerkStyleDto sStyle = participantDto.getPerks().getStyles().get(1);
            PerkAllEntity nowPerk = perkAllRepository.findByAll(pMap.get(pStyle.getStyle()), pMap.get(pStyle.getSelections().get(0).getPerk()),pMap.get(pStyle.getSelections().get(1).getPerk()),pMap.get(pStyle.getSelections().get(2).getPerk()),pMap.get(pStyle.getSelections().get(3).getPerk()),pMap.get(sStyle.getStyle()),pMap.get(sStyle.getSelections().get(0).getPerk()),pMap.get(sStyle.getSelections().get(1).getPerk()), participantDto.getPerks().getStatPerks().getDefense(),participantDto.getPerks().getStatPerks().getFlex(),participantDto.getPerks().getStatPerks().getOffense());
            if(nowPerk == null)
                nowPerk = perkAllRepository.save(new PerkAllEntity(null, pMap.get(pStyle.getStyle()), pMap.get(pStyle.getSelections().get(0).getPerk()),pMap.get(pStyle.getSelections().get(1).getPerk()),pMap.get(pStyle.getSelections().get(2).getPerk()),pMap.get(pStyle.getSelections().get(3).getPerk()),pMap.get(sStyle.getStyle()),pMap.get(sStyle.getSelections().get(0).getPerk()),pMap.get(sStyle.getSelections().get(1).getPerk()), participantDto.getPerks().getStatPerks().getDefense(),participantDto.getPerks().getStatPerks().getFlex(),participantDto.getPerks().getStatPerks().getOffense()));
            Long parid = puMap.get(participantDto.getPuuid());
            soloRepository.save(new SoloEntity(null, participantDto.getChampionId(), participantDto.getWin(), participantDto.getIndividualPosition(),nowPerk.getId(), listList.get(parid.intValue()).get(0), listList.get(parid.intValue()).get(1),listList.get(parid.intValue()).get(2),spellCombinationRepository.findbycom(spMap.get(participantDto.getSummoner1Id().longValue()),spMap.get(participantDto.getSummoner2Id().longValue()))));
        });

    }


}
