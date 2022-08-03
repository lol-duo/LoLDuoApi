package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

                DuoInfoEntity duo =  duoInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(duoEntity.getChampion()),objectMapper.writeValueAsString(duoEntity.getPosition())).orElse(null);
                /*
                DuoInfoEntity t =  duoInfoRepository.findById(3L).orElse(null);
                if(t==null) log.info("안댐");
                else log.info(t.getPosition().toString());
                 */
                if(duo==null){
                    Perk perk =new Perk(duoEntity.getPerkList(),0L);
                    Spell spell =new Spell(duoEntity.getSpellList(),0L);
                    Item item = new Item(duoEntity.getItemList(),0L);
                    if(duoEntity.getWin()){
                        //duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,1L,perkList,spellList,itemList));
                    }
                    else{
                        //duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,0L,perkList,spellList,itemList));
                    }
                }
                else{
                   // DuoInfoEntity test = objectMapper.readValue(duo, new TypeReference<DuoInfoEntity>() {});
                    // log.info(test.toString());
                }
            } catch (JsonProcessingException e) {
                log.info("objectMapper 파싱 런타임 에러");
                throw new RuntimeException(e);
            }
        });


        log.info("end");
    }
}
