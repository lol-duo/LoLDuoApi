package com.lolduo.duo.controller;

import com.lolduo.duo.object.response.getChampionList.Champion;
import com.lolduo.duo.v2.response.championDetail.DetailDoubleResponse;
import com.lolduo.duo.v2.response.championDetail.SoloChampionDetailResponse;
import com.lolduo.duo.v2.response.mainPage.DoubleResponseV2;
import com.lolduo.duo.v2.response.mainPage.SoloResponseV2;
import com.lolduo.duo.service.ClientService;
import com.lolduo.duo.v2.ClientServiceV2;
import com.lolduo.duo.service.HealthCheckService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/championList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.",response = Champion[].class)
    public ResponseEntity<?> ChampionList() {
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientService.getChampionList();
        log.info("/v2/ChampionList() API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
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
    @GetMapping("/v2/soloInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = SoloResponseV2[].class)
    public ResponseEntity<?> soloInfo(@RequestParam String position,@RequestParam Long championId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getSoloChampionInfoList(championId,position);
        log.info("/v2/SoloInfo() API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/v2/doubleInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = DoubleResponseV2[].class)
    public ResponseEntity<?> doubleInfo(@RequestParam String position,@RequestParam String position2,@RequestParam Long championId,@RequestParam Long championId2){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getDoubleChampionInfoList(championId,position,championId2,position2);
        log.info("/v2/DoubleInfo()  API Call 된 시간 : {}, 소요 시간  : {} ms", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/v2/doubleInfoDummy")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = DoubleResponseV2[].class)
    public ResponseEntity<?> doubleInfoDummy(@RequestParam String position,@RequestParam String position2,@RequestParam Long championId,@RequestParam Long championId2){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getDoubleDummy(championId,position,championId2,position2);
        log.info("/v2/getDoubleInfoDummy()  API Call 된 시간 : {}, 소요 시간  : {} ms", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/checkServer")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = DoubleResponseV2[].class)
    public ResponseEntity<?> doubleInfoDummy(@RequestParam String name) {
        long start = System.currentTimeMillis();
        log.info("/checkServer  API Call 된 시간 : {},호출한 사람 : {} ", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,name);
        return new ResponseEntity<>("호출한 사람 : " + name,HttpStatus.OK);
    }

    @GetMapping("/v2/soloChampionDetailDummy")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = SoloChampionDetailResponse.class)
    public ResponseEntity<?> getSoloChampionDetailDummy(@RequestParam Long dbId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getSoloChampionDetailDummy(dbId);
        log.info("/v2/soloChampionDetailDummy  API Call 된 시간 : {} ", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) );
        return responseEntity;
    }
    @GetMapping("/v2/doubleChampionDetailDummy")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = DetailDoubleResponse.class)
    public ResponseEntity<?> getDoubleChampionDetailDummy(@RequestParam Long dbId1,@RequestParam Long dbId2){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getDoubleChampionDetailDummy(dbId1,dbId2);
        log.info("/v2/doubleChampionDetailDummy  API Call 된 시간 : {} ", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) );
        return responseEntity;
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
