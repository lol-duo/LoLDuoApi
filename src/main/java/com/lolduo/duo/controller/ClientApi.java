package com.lolduo.duo.controller;

import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.ChampionEntity;
import com.lolduo.duo.object.response.ChampionInfoList;
import com.lolduo.duo.service.ClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequiredArgsConstructor
public class ClientApi {
    private final ClientService clientService;
    
    @GetMapping("/getChampionList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.",response = ChampionEntity[].class)
    public ResponseEntity<?> getChampionList() {
        return clientService.getChampionList();
    }

    @PostMapping("/getInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = ChampionInfoList[].class)
    public ResponseEntity<?> getInfo(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionInfoList(championInfoDTOList);
    }
}
