package com.lolduo.duo.entity.clientInfo;

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
@Table(name = "duo_info")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DuoInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "json")
    @Column(name = "champion_id",columnDefinition = "json")
    private TreeSet<Long> championId;

    @Type(type = "json")
    @Column(name = "position", columnDefinition = "json")
    private Map<Long, String> position;

    @Column(name = "all_count")
    private Long allCount;

    @Column(name = "win_count")
    private Long winCount;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private Map<Map<Long, List<Long>>, Long> perkList;

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private Map<Map<Long,TreeSet<Long>>,Long> spellList;

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private Map<Map<Long,List<Long>>,Long> itemList;

    public DuoInfoEntity(TreeSet<Long> championId, Map<Long, String> position, Long allCount, Long winCount, Map<Map<Long, List<Long>>, Long> perkList, Map<Map<Long, TreeSet<Long>>, Long> spellList, Map<Map<Long, List<Long>>, Long> itemList) {
        this.championId = championId;
        this.position = position;
        this.allCount = allCount;
        this.winCount = winCount;
        this.perkList = perkList;
        this.spellList = spellList;
        this.itemList = itemList;
    }
    public DuoInfoEntity(Long id, TreeSet<Long> championId, Map<Long, String> position, Long allCount, Long winCount, Map<Map<Long, List<Long>>, Long> perkList, Map<Map<Long, TreeSet<Long>>, Long> spellList, Map<Map<Long, List<Long>>, Long> itemList) {
        this.id = id;
        this.championId = championId;
        this.position = position;
        this.allCount = allCount;
        this.winCount = winCount;
        this.perkList = perkList;
        this.spellList = spellList;
        this.itemList = itemList;
    }
}
