package com.lolduo.duo.controller;

import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoDTOList;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(originPatterns = "http://13.124.202.156/")
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
