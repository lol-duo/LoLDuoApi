package com.lolduo.duo.object.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "champion")
public class ChampionEntity implements Comparable<ChampionEntity> {
    @Id
    @ApiModelProperty(example = "86")
    private Long id;
    @Column
    @ApiModelProperty(example = "가렌")
    private String name;
    @Column
    @ApiModelProperty(example = "https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/champion/Garen.png")
    private String imgUrl;
    public ChampionEntity(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int compareTo(ChampionEntity championEntity) {
        return this.name.compareTo(championEntity.name);
    }
}
