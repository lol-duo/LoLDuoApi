package com.lolduo.duo.v2.response.championDetail.sub.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailItem {
    String firstItem;
    String secondItem;
    String thirdItem;

    public DetailItem(String firstItem, String secondItem, String thirdItem) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.thirdItem = thirdItem;
    }
}
