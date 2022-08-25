package com.lolduo.duo.object.response.championDetail2;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ResponsePerk2 {
    String winRate;
    String AllCount;

    String mainPerkUrl;
    List<String> keyPerkUrlList;
    List<String> main1UrlList;
    List<String> main2UrlList;
    List<String> main3UrlList;

    String subPerkUrl;
    List<String> sub1UrlList;
    List<String> sub2UrlList;
    List<String> sub3UrlList;

    List<String> subsub1UrlList; // 이후에 subsub -> statMod 이름 변경
    List<String> subsub2UrlList;
    List<String> subsub3UrlList;
}