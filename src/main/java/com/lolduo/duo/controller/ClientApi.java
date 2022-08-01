package com.lolduo.duo.controller;

import com.lolduo.duo.dto.VersionDto;
import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoDTOList;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientApi {
    private final ClientService clientService;

    @GetMapping("/getChampionList")
    public List<ChampionEntity> getChampionList(){
        return clientService.getChampionList();
    }

    @PostMapping("/getInfo")
    public List<ChampionInfoDTOList> getInfo(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
            return clientService.getChampionInfoList(championInfoDTOList);
    }
}
