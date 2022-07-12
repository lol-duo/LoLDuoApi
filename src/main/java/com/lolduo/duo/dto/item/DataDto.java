package com.lolduo.duo.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DataDto {
    private String name;
    private String key;

    public DataDto(String name, String key) {
        this.name = name;
        this.key = key;
    }
}
