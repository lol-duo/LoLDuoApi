package com.lolduo.duo.controller;

import com.lolduo.duo.response.getChampionList.Champion;
import com.lolduo.duo.service.DetailService;
import com.lolduo.duo.response.championDetail.DetailDoubleResponse;
import com.lolduo.duo.response.championDetail.DetailSoloResponse;
import com.lolduo.duo.response.mainPage.DoubleResponseV2;
import com.lolduo.duo.response.mainPage.SoloResponseV2;
import com.lolduo.duo.service.ClientServiceV2;
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
    private final HealthCheckService healthCheckService;
    private final ClientServiceV2 clientServiceV2;
    private final DetailService detailService;
    @GetMapping("/championList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.",response = Champion[].class)
    public ResponseEntity<?> ChampionList() {
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getChampionList();
        log.info("/v2/ChampionList() API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/health")
    @ApiOperation(value ="서버 정상 작동 여부 반환", notes = "서버의 HealthCheck 결과를 반환한다.",response = Champion[].class)
    public ResponseEntity<?> getHealth() {
        return healthCheckService.getHealthCheckResult();
    }

    @GetMapping("/v2/soloInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = SoloResponseV2[].class)
    public ResponseEntity<?> soloInfo(@RequestParam String position,@RequestParam Long championId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getSoloChampionInfoList(championId,position);
        log.info("/v2/SoloInfo() API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }

    @GetMapping("/v2/soloInfoFront")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = SoloResponseV2[].class)
    public ResponseEntity<?> soloInfoFront(@RequestParam String position,@RequestParam Long championId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getSoloChampionInfoListToFront(championId,position);
        log.info("/v2/SoloInfo() API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/v2/doubleInfo")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = DoubleResponseV2[].class)
    public ResponseEntity<?> doubleInfo(@RequestParam String position,@RequestParam String position2,@RequestParam Long championId,@RequestParam Long championId2){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = clientServiceV2.getDoubleChampionInfoList(championId,position,championId2,position2);
        log.info("/v2/DoubleInfo()  API Call 된 시간 : {}, 소요 시간  : {} ms", LocalDateTime.ofInstant(Instant.ofEpochMilli(start),ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }

    @GetMapping("/checkServer")
    @ApiOperation(value ="요청한 챔피언 목록에 대한 승률 및 판수 반환", notes = "요청한 조합에 대한 챔피언들의 승률 및 전체 판수 리스트 정보를 제공한다.",response = DoubleResponseV2[].class)
    public ResponseEntity<?> doubleInfoDummy(@RequestParam String name) {
        long start = System.currentTimeMillis();
        log.info("/checkServer  API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return new ResponseEntity<>("호출한 사람 : " + name,HttpStatus.OK);
    }

    @GetMapping("/v2/soloChampionDetailDummy")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = DetailSoloResponse.class)
    public ResponseEntity<?> getSoloChampionDetailDummy(@RequestParam Long dbId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = detailService.getSoloChampionDetailDummy(dbId);
        log.info("/v2/soloChampionDetailDummy  API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/v2/doubleChampionDetailDummy")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = DetailDoubleResponse.class)
    public ResponseEntity<?> getDoubleChampionDetailDummy(@RequestParam Long dbId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = detailService.getDoubleChampionDetailDummy(dbId);
        log.info("/v2/doubleChampionDetailDummy  API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }

    @GetMapping("/v2/soloChampionDetail")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = DetailSoloResponse.class)
    public ResponseEntity<?> getSoloChampionDetail(@RequestParam Long dbId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = detailService.getSoloChampionDetail(dbId);
        log.info("/v2/soloChampionDetailDummy API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
    @GetMapping("/v2/doubleChampionDetail")
    @ApiOperation(value ="요청한 챔피언들에 대하여 종합 정보를 보여준다.",notes = "룬,아이템,스펠,승률을 포함한 상위 3개의 정보를 간략화해서 보여준다.  ",response = DetailDoubleResponse.class)
    public ResponseEntity<?> getDoubleChampionDetail(@RequestParam Long dbId){
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = detailService.getDoubleChampionDetail(dbId);
        log.info("/v2/doubleChampionDetail API Call 된 시간 : {}, 소요 시간  : {} ms",LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")) ,System.currentTimeMillis()-start);
        return responseEntity;
    }
}
