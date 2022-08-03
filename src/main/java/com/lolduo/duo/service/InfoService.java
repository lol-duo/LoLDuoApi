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


import java.util.ArrayList;
import java.util.List;

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

        duoEntityList.forEach(duoEntity -> {
            DuoInfoEntity duoInfoEntity = null;
            try {
                duoInfoEntity = duoInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(duoEntity.getChampion()),objectMapper.writeValueAsString(duoEntity.getPosition())).orElse(null);
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
            }
            if(duoInfoEntity==null){
                Perk perk =new Perk(duoEntity.getPerkList(),1L);
                Spell spell =new Spell(duoEntity.getSpellList(),1L);
                Item item = new Item(duoEntity.getItemList(),1L);
                List<Perk> perkList = new ArrayList<>();
                List<Spell> spellList = new ArrayList<>();
                List<Item> itemList = new ArrayList<>();
                if(duoEntity.getWin()){
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,1L,perkList,spellList,itemList));
                }
                else{
                    perk.setWin(0L);
                    spell.setWin(0L);
                    item.setWin(0L);
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,0L,perkList,spellList,itemList));
                }
            }
            else{
                /*
                duoInfoEntity.setAllCount(duoInfoEntity.getAllCount()+1);
                if(duoEntity.getWin()){
                    duoInfoEntity.setWinCount(duoInfoEntity.getWinCount()+1);
                }
                 */
                log.info("값 존재 : "  + duoInfoEntity.toString());
            }
        });


        log.info("end");
    }
}
