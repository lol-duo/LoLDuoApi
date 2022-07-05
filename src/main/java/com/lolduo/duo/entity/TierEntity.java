package com.lolduo.duo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "tier")
public class TierEntity {

    @Id
    private Long tierId;

    @Column
    private String tierName;

    @Column
    private String tierImg;

    public TierEntity(Long tierId, String tierName, String tierImg) {
        this.tierId = tierId;
        this.tierName = tierName;
        this.tierImg = tierImg;
    }
}
