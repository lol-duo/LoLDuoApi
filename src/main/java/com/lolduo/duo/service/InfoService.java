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


import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoService {
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;
    private final QuintetRepository quintetRepository;

    private final DuoInfoRepository duoInfoRepository;

    private void updateItemList(List<Item> duoInfoItemList, Map<Long,List<Long>> duoItemList){
        boolean isUpdated = false;
        for(int i = 0 ; i <duoInfoItemList.size();i++){
            Item duoInfoItem = duoInfoItemList.get(i);
            if(duoInfoItem.getItemMap().values().containsAll(duoItemList.values())){
                duoInfoItem.setWin(duoInfoItem.getWin()+1);
                duoInfoItemList.add(i,duoInfoItem);
                duoInfoItemList.remove(i+1);
                isUpdated =true;
                break;
            }
        }
        if(!isUpdated){
            duoInfoItemList.add(new Item(duoItemList,1L));
        }
    }

    private void updatePerkList(List<Perk> duoInfoPerkList, Map<Long,List<Long>> duoPerkList){
        boolean isUpdated =false;
        for(int i = 0 ; i < duoInfoPerkList.size();i++){
            Perk duoInfoPerk = duoInfoPerkList.get(i);
            if(duoInfoPerk.getPerkMap().values().containsAll(duoPerkList.values())){
                duoInfoPerk.setWin(duoInfoPerk.getWin()+1);
                duoInfoPerkList.add(i,duoInfoPerk);
                duoInfoPerkList.remove(i+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            duoInfoPerkList.add(new Perk(duoPerkList,1L));
        }
    }
    private void updateSpellList(List<Spell> duoInfoSpellList, Map<Long, TreeSet<Long>> duoSpellList){
        boolean isUpdated =false;
        for(int i = 0 ; i < duoInfoSpellList.size();i++){
            Spell duoInfoSpell = duoInfoSpellList.get(i);
            if(duoInfoSpell.getSpellMap().values().containsAll(duoSpellList.values())){
                duoInfoSpell.setWin(duoInfoSpell.getWin()+1);
                duoInfoSpellList.add(i,duoInfoSpell);
                duoInfoSpellList.remove(i+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            duoInfoSpellList.add(new Spell(duoSpellList,1L));
        }
    }
    public void makeDuoInfo()  {
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
                List<Perk> perkList = new LinkedList<>();
                List<Spell> spellList = new LinkedList<>();
                List<Item> itemList = new LinkedList<>();
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
                duoInfoEntity.setAllCount(duoInfoEntity.getAllCount()+1);
                if(duoEntity.getWin()){
                    duoInfoEntity.setWinCount(duoInfoEntity.getWinCount()+1);
                    updateItemList(duoInfoEntity.getItemList(),duoEntity.getItemList());
                    updatePerkList(duoInfoEntity.getPerkList(),duoEntity.getPerkList());
                    updateSpellList(duoInfoEntity.getSpellList(),duoEntity.getSpellList());
                }
                log.info("값 존재,수정 후 save: " );
                duoInfoRepository.save(duoInfoEntity);
            }
        });

        log.info("end");
    }
}
