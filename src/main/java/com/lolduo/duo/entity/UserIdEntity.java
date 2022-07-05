package com.lolduo.duo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_id")
public class UserIdEntity {
    @Id
    private String puuid;
    @Column
    private String summonerId;

    @ManyToOne
    @JoinColumn(name = "tier")
    private TierEntity tier;

    public UserIdEntity(String puuid, String summonerId, TierEntity tier) {
        this.puuid = puuid;
        this.summonerId = summonerId;
        this.tier = tier;
    }
}
