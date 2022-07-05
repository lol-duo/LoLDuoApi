package com.lolduo.duo.service;


import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.match_v5.MatchDto;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.entity.MatchIdEntity;
import com.lolduo.duo.entity.SoloEntity;
import com.lolduo.duo.entity.UserIdEntity;
import com.lolduo.duo.repository.MatchIdRepository;
import com.lolduo.duo.repository.SoloRepository;
import com.lolduo.duo.repository.TierRepository;
import com.lolduo.duo.repository.UserIdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RiotService {

    private final UserIdRepository userIdRepository;
    private final MatchIdRepository matchIdRepository;
    private final SoloRepository soloRepository;
    private final TierRepository tierRepository;
    private String key = "RGAPI-d2339d99-679e-4226-9659-d3f39b416577";

    public RiotService(UserIdRepository userIdRepository, MatchIdRepository matchIdRepository, SoloRepository soloRepository, TierRepository tierRepository) {
        this.userIdRepository = userIdRepository;
        this.matchIdRepository = matchIdRepository;
        this.soloRepository = soloRepository;
        this.tierRepository = tierRepository;
    }

    public void getChallengerList(){
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
    }

    public void getMatchId(String startTime, String endTime) {
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
                    matchIdRepository.save(new MatchIdEntity(null, s.toString()));
                }
                catch (DataIntegrityViolationException e){
                }
            });
        });
    }

    public void getSolo(){
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        List<MatchIdEntity> matchIdEntityList = matchIdRepository.findAll();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        matchIdEntityList.forEach(matchIdEntity -> {
            log.info(url+matchIdEntity.getMatchId());
            ResponseEntity<MatchDto> response = restTemplate.exchange(url + matchIdEntity.getMatchId(), HttpMethod.GET, requestEntity, MatchDto.class);
            response.getBody().getInfo().getParticipants().forEach(participantDto -> {
                soloRepository.save(new SoloEntity(null, participantDto.getChampionId(),participantDto.getWin()));
            });
        });
    }
}
