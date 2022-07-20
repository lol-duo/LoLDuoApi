package com.lolduo.duo.controller;


import com.lolduo.duo.dto.KeyDto;
import com.lolduo.duo.dto.VersionDto;
import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.service.RiotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RiotApi {
    @Autowired
    private RiotService riotService;
    @PostMapping("/setKey")
    public void setKey(@RequestBody KeyDto key){
        riotService.setKey(key.getKey());
    }
    @PostMapping("/setVersion")
    public void setVersion(@RequestBody VersionDto version){
        riotService.setVersion(version.getVersion());
    }
}
