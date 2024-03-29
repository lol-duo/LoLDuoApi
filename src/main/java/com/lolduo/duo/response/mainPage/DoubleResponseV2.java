package com.lolduo.duo.response.mainPage;

import com.lolduo.duo.response.ChampionResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoubleResponseV2 {
    @ApiModelProperty(example = "15")
    private Long id;
    @ApiModelProperty(example = "0")
    private Long rankChangeNumber;
    @ApiModelProperty(example = "1")
    private Long rankNumber ;
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/logo/Group.svg")
    private String rankNumberIcon ;

    @ApiModelProperty
    private ChampionResponse champion1;

    @ApiModelProperty
    private ChampionResponse champion2;

    @ApiModelProperty(example = "67.2%")
    private String winRate;

    public DoubleResponseV2(Long id, Long rankChangeNumber, Long rankNumber, String rankNumberIcon, ChampionResponse champion1, ChampionResponse champion2, String winRate) {
        this.id = id;
        this.rankChangeNumber = rankChangeNumber;
        this.rankNumber = rankNumber;
        this.rankNumberIcon = rankNumberIcon;
        this.champion1 = champion1;
        this.champion2 = champion2;
        this.winRate = winRate;
    }
}
