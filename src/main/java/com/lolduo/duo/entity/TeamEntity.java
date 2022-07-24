package com.lolduo.duo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "team")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "win")
    private Boolean win;

    @Type(type = "json")
    @Column(name = "position")
    private Map<Long,String> positionMap;

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private Map<Long,List<Long>> itemListMap;

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private Map<Long,TreeSet<Long>> spellListMap;

    @Type(type = "json")
    @Column(name = "champion")
    private TreeSet<Long> championList;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private Map<Long,List<Long>> perkListMap;

    public TeamEntity( Boolean win, Map<Long, String> positionMap, Map<Long, List<Long>> itemListMap, Map<Long, TreeSet<Long>> spellListMap, TreeSet<Long> championList, Map<Long, List<Long>> perkListMap) {
        this.win = win;
        this.positionMap = positionMap;
        this.itemListMap = itemListMap;
        this.spellListMap = spellListMap;
        this.championList = championList;
        this.perkListMap = perkListMap;
    }
}