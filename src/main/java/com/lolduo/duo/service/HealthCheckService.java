package com.lolduo.duo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    public ResponseEntity<?> getHealthCheckResult() {
        Boolean isHealthy = true;
        // 서버 상태를 점검해 isHealthy에 결과를 저장하는 로직 추가 가능
        if (isHealthy)
            return new ResponseEntity<>(isHealthy, HttpStatus.OK);
        else
            return new ResponseEntity<>(isHealthy, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
