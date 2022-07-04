package com.lolduo.duo.service;


import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.match_v5.MatchDto;
import com.lolduo.duo.dto.summoner_v4.MatchIdDTO;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.entity.MatchIdEntity;
import com.lolduo.duo.entity.UserIdEntity;
import com.lolduo.duo.repository.MatchIdRepository;
import com.lolduo.duo.repository.UserIdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@Slf4j
public class RiotService {

    private final UserIdRepository userIdRepository;
    private final MatchIdRepository matchIdRepository;
    private String key = "RGAPI-501dbae8-011e-48d6-9f02-179652d81e29";

    private RiotService(UserIdRepository userIdRepository, MatchIdRepository matchIdRepository) {
        this.userIdRepository = userIdRepository;
        this.matchIdRepository = matchIdRepository;
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
            ResponseEntity<SummonerDTO> response_id = restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class);
            userIdRepository.save(new UserIdEntity(response_id.getBody().getPuuid(), leagueItemDTO.getSummonerId()));
        });
    }

    public void getMatchId() {
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/";
        List<UserIdEntity> userIdEntity = userIdRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        userIdEntity.forEach(userIdEntity1 -> {
            ResponseEntity<List> response = restTemplate.exchange(url + userIdEntity1.getPuuid() + "/ids?type=ranked&start=0&count=100", HttpMethod.GET, requestEntity, List.class);
            response.getBody().forEach(s -> {
                try{
                    matchIdRepository.save(new MatchIdEntity(null, s.toString()));
                }
                catch (DataIntegrityViolationException e){
                }
            });
        });
    }

    public void getMatch(){
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        List<MatchIdEntity> matchIdEntityList = matchIdRepository.findAll();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        matchIdEntityList.forEach(matchIdEntity -> {
            ResponseEntity<MatchDto> response = restTemplate.exchange(url + matchIdEntity.getMatchId(), HttpMethod.GET, requestEntity, MatchDto.class);

        });
    }
}
