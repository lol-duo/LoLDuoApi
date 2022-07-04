package com.lolduo.duo.controller;


import com.lolduo.duo.service.RiotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RiotApi {
    @Autowired
    private RiotService riotService;

    @GetMapping("/challenger")
    public void getChallengerlist(){
        riotService.getChallengerList();
    }

    @GetMapping("/getMatch")
    public void getMatchId() {
        riotService.getMatchId();
    }
}
