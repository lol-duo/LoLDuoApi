package com.lolduo.duo.response.championDetail.sub.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class DetailRune {
    String mainRuneImgUrl;
    String subRuneImgUrl;

    List<String> mainRuneList1;
    List<String> mainRuneList2;
    List<String> mainRuneList3;
    List<String> mainRuneList4;



    List<String> subRuneList1;
    List<String> subRuneList2;
    List<String> subRuneList3;

    public DetailRune(String mainRuneImgUrl, String subRuneImgUrl, List<String> mainRuneList1, List<String> mainRuneList2, List<String> mainRuneList3, List<String> mainRuneList4, List<String> subRuneList1, List<String> subRuneList2, List<String> subRuneList3) {
        this.mainRuneImgUrl = mainRuneImgUrl;
        this.subRuneImgUrl = subRuneImgUrl;
        this.mainRuneList1 = mainRuneList1;
        this.mainRuneList2 = mainRuneList2;
        this.mainRuneList3 = mainRuneList3;
        this.mainRuneList4 = mainRuneList4;
        this.subRuneList1 = subRuneList1;
        this.subRuneList2 = subRuneList2;
        this.subRuneList3 = subRuneList3;
    }
}
