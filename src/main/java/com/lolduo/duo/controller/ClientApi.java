package com.lolduo.duo.controller;

import com.lolduo.duo.object.dto.client.ChampionInfoDTO;
import com.lolduo.duo.object.entity.ChampionEntity;
import com.lolduo.duo.object.response.ChampionInfoList;
import com.lolduo.duo.object.response.championDetail.ChampionDetail;
import com.lolduo.duo.object.response.getChampionList.Champion;
import com.lolduo.duo.service.ClientService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@CrossOrigin(originPatterns = "*")
@RestController
@RequiredArgsConstructor
public class ClientApi {
    private final ClientService clientService;

    @GetMapping("/getChampionList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.",response = Champion[].class)
    public ResponseEntity<?> getChampionList() {
        return clientService.getChampionList();
    }

    @PostMapping("/getInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = ChampionInfoList[].class)
    public ResponseEntity<?> getInfo(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionInfoList(championInfoDTOList);
    }

    @PostMapping("/championDetail")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠 상위 2개의 정보를 간략화해서 보여준다.  ",response = ChampionDetail.class)
    public ResponseEntity<?> getChampionDetail(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionDetail(championInfoDTOList);
    }
}
