package com.lolduo.duo.controller;


import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.service.RiotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RiotApi {
    @Autowired
    private RiotService riotService;

    @GetMapping("/challenger")
    public void getChallengerlist(@RequestParam(value = "k")String key, @RequestParam(value = "s")String startTime, @RequestParam(value = "e") String endTime){
        riotService.getChallengerList(key, startTime, endTime);
    }

    @GetMapping("/getMatch")
    public void getMatchId(@RequestParam(value = "k")String key, @RequestParam(value = "s")String startTime, @RequestParam(value = "e") String endTime) {
        riotService.getMatchId(key, startTime, endTime);
    }

    @GetMapping("/getSolo")
    public void makeDB(){
        riotService.getSolo();
    }

    @PostMapping("/setPerk")
    public void setPerk(@RequestBody List<PerkDto> perkDtoList){
        riotService.setPerk(perkDtoList);
    }
    @PostMapping("/setItem")
    public void setItem(@RequestBody ItemDto item){
        riotService.setItem(item);
    }
    @PostMapping("/setChampion")
    public void setChampion(@RequestBody ItemDto item){
        riotService.setChampion(item);
    }

    @PostMapping("/setSpell")
    public void setSpell(@RequestBody ItemDto item){
        riotService.setSpell(item);
    }

}
