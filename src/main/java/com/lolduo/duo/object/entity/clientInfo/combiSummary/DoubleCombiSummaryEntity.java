package com.lolduo.duo.object.entity.clientInfo.combiSummary;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Table(name = "double_combi_summary", indexes = @Index(name = "idx_pos_champ_win_all", columnList = "position, champion_id, win_rate"))
@Getter
@TypeDef(name = "json", typeClass = JsonType.class,defaultForType = JsonNode.class)
public class DoubleCombiSummaryEntity implements ICombiSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "json")
    @Column(name = "champion_id",columnDefinition = "varchar(50)")
    private TreeSet<Long> championId = new TreeSet<>();

    @Type(type = "json")
    @Column(name = "position", columnDefinition = "varchar(100)")
    private Map<Long, String> position = new HashMap<>();

    @Column(name = "all_count_sum")
    private Long allCountSum;

    @Column(name = "win_count_sum")
    private Long winCountSum;

    @Column(name = "win_rate")
    private Double winRate;
}
