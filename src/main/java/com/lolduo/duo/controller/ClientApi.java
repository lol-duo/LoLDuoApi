package com.lolduo.duo.controller;

import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.dto.client.ChampionInfoDTOList;
import com.lolduo.duo.entity.ChampionEntity;
import com.lolduo.duo.service.ClientService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(originPatterns = "http://13.124.202.156/")
@RestController
@RequiredArgsConstructor
public class ClientApi {
    private final ClientService clientService;

    @GetMapping("/getChampionList")
    @ApiOperation(value ="riot key 세팅", notes = "기존의 riot key값을 변경한다.")
    public ResponseEntity<?> getChampionList() {
        return clientService.getChampionList();
    }

    @PostMapping("/getInfo")
    public ResponseEntity<?> getInfo(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionInfoList(championInfoDTOList);
    }
}
