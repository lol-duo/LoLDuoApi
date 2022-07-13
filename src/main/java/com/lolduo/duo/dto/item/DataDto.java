package com.lolduo.duo.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class DataDto {
    private String name;
    private String key;
    private Object into;
    private GoldDto gold;
}
