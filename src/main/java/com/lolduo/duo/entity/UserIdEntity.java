package com.lolduo.duo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_id")
public class UserIdEntity {
    @Id
    private String puuid;
    @Column
    private String summonerId;

    public UserIdEntity(String puuid, String summonerId) {
        this.puuid = puuid;
        this.summonerId = summonerId;
    }
}
