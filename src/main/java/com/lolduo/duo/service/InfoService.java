package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.entity.clientInfo.*;
import com.lolduo.duo.entity.gameInfo.DuoEntity;
import com.lolduo.duo.repository.clientInfo.DuoInfoRepository;
import com.lolduo.duo.repository.gameInfo.DuoRepository;
import com.lolduo.duo.repository.gameInfo.QuintetRepository;
import com.lolduo.duo.repository.gameInfo.SoloRepository;
import com.lolduo.duo.repository.gameInfo.TrioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoService {
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;
    private final QuintetRepository quintetRepository;

    private final DuoInfoRepository duoInfoRepository;

    public void makeDuoInfo() throws JsonProcessingException {
        List<DuoEntity> duoEntityList = duoRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("start");

        duoEntityList.forEach(duoEntity -> {
            try{
                String duo =duoInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(duoEntity.getChampion()),objectMapper.writeValueAsString(duoEntity.getPosition())).orElse(null);
                   //DuoInfoEntity duo =  duoInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(duoEntity.getChampion()),objectMapper.writeValueAsString(duoEntity.getPosition())).orElse(null);
                if(duo==null){
                    Map<Map<Long, List<Long>>, Long> perkList = new HashMap<>();
                    Map<Map<Long, TreeSet<Long>>, Long> spellList = new HashMap<>();
                    Map<Map<Long,List<Long>>,Long> itemList = new HashMap<>();
                    if(duoEntity.getWin()){
                        perkList.put(duoEntity.getPerkList(),1L);
                        spellList.put(duoEntity.getSpellList(),1L);
                        itemList.put(duoEntity.getItemList(),1L);
                        duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,1L,perkList,spellList,itemList));
                    }
                    else{
                        perkList.put(duoEntity.getPerkList(),0L);
                        spellList.put(duoEntity.getSpellList(),0L);
                        itemList.put(duoEntity.getItemList(),0L);
                        duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,0L,perkList,spellList,itemList));
                    }
                }
                else{
                    log.info(duo);
                }
            } catch (JsonProcessingException e) {
                log.info("objectMapper 파싱 런타임 에러");
                throw new RuntimeException(e);
            }
        });


        log.info("end");
    }
}
