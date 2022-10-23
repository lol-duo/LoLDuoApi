package com.lolduo.duo.controller;

import com.lolduo.duo.object.response.getChampionList.Champion;
import com.lolduo.duo.v2.response.SoloResponseV2;
import com.lolduo.duo.service.ClientService;
import com.lolduo.duo.v2.service.ClientServiceV2;
import com.lolduo.duo.service.HealthCheckService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@CrossOrigin(originPatterns = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientApi {
    private final ClientService clientService;
    private final HealthCheckService healthCheckService;
    private final ClientServiceV2 clientServiceV2;

    @GetMapping("/getChampionList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.",response = Champion[].class)
    public ResponseEntity<?> getChampionList() {
        return clientService.getChampionList();
    }
    @GetMapping("/health")
    @ApiOperation(value ="서버 정상 작동 여부 반환", notes = "서버의 HealthCheck 결과를 반환한다.",response = Champion[].class)
    public ResponseEntity<?> getHealth() {
        return healthCheckService.getHealthCheckResult();
    }
    /*
    @PostMapping("/getInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = ChampionInfoList[].class)
    public ResponseEntity<?> getInfo(@RequestBody CombiSearchDTO combiSearchDTO){
        log.info("getInfo() - 시간 측정 : API CALL {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return clientService.getChampionInfoList(combiSearchDTO);
    }
     */
    @GetMapping("/v2/getSoloInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = SoloResponseV2[].class)
    public ResponseEntity<?> getInfoV2(@RequestParam String position,@RequestParam Long championId){
        log.info("/v2/getInfo() - 시간 측정 : API CALL {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return clientServiceV2.getSoloChampionInfoList(championId,position);
    }
    @GetMapping("/v2/getSoloInfoDummy")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = SoloResponseV2[].class)
    public ResponseEntity<?> getInfoV2Dummy(@RequestParam String position,@RequestParam Long championId){
        log.info("/v2/getInfo() - 시간 측정 : API CALL {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Seoul")));
        return clientServiceV2.getDummy(championId,position);
    }
    /*
    @PostMapping("/championDetail")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠 상위 2개의 정보를 간략화해서 보여준다.  ",response = ChampionDetail.class)
    public ResponseEntity<?> getChampionDetail(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionDetail(championInfoDTOList);
    }
    @PostMapping("/v2/championDetail")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠 상위 2개의 정보를 간략화해서 보여준다.  ",response = ChampionDetail2.class)
    public ResponseEntity<?> getChampionDetail2(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionDetail2(championInfoDTOList);
    }
     */
}
