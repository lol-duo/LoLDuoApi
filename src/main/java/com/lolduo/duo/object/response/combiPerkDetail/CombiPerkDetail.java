package com.lolduo.duo.object.response.combiPerkDetail;

import com.lolduo.duo.object.response.championDetail2.ResponsePerk2;
import lombok.Getter;

import java.util.List;

@Getter
public class CombiPerkDetail {
    private List<ResponseChampionInfo> championInfoList;
    private List<CombiPerkInfo> combiPerkInfoList;

    public CombiPerkDetail(List<ResponseChampionInfo> championInfoList, List<CombiPerkInfo> combiPerkInfoList) {
        this.championInfoList = championInfoList;
        this.combiPerkInfoList = combiPerkInfoList;
    }

    @Getter
    public static class ResponseChampionInfo {
        private Long championId;
        private String championPosition;
        private String championImgUrl;
        private String championPositionImgUrl;
        private List<String> perkMythItemImgUrlList;

        public ResponseChampionInfo(Long championId, String championPosition, String championImgUrl, String championPositionImgUrl, List<String> perkMythItemImgUrlList) {
            this.championId = championId;
            this.championPosition = championPosition;
            this.championImgUrl = championImgUrl;
            this.championPositionImgUrl = championPositionImgUrl;
            this.perkMythItemImgUrlList = perkMythItemImgUrlList;
        }
    }

    @Getter
    public static class CombiPerkInfo {
        private Long allCount;
        private String winRate;
        List<ResponsePerk2> perkUrlList;

        public CombiPerkInfo(Long allCount, String winRate, List<ResponsePerk2> perkUrlList) {
            this.allCount = allCount;
            this.winRate = winRate;
            this.perkUrlList = perkUrlList;
        }
    }
}
