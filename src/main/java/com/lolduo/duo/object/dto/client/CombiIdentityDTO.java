package com.lolduo.duo.object.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class CombiIdentityDTO {
    private Map<Long, String> positionMap;
    private String perkMythItem;

    public CombiIdentityDTO(Map<Long, String> positionMap, String perkMythItem) {
        this.positionMap = positionMap;
        this.perkMythItem = perkMythItem;
    }
}
