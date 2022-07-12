package com.lolduo.duo.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class ItemDto {
    private Map<String, DataDto> data;

    public ItemDto(Map<String, DataDto> data) {
        this.data = data;
    }
}
