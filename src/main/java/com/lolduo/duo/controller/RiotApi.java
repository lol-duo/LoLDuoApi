package com.lolduo.duo.controller;


import com.lolduo.duo.dto.KeyDto;
import com.lolduo.duo.dto.VersionDto;
import com.lolduo.duo.service.RiotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiotApi {

    private final RiotService riotService;
    @PostMapping("/setKey")
    public void setKey(@RequestBody KeyDto key){
        riotService.setKey(key.getKey());
    }
    @PostMapping("/setVersion")
    public void setVersion(@RequestBody VersionDto version){
        riotService.setVersion(version.getVersion());
    }
}
