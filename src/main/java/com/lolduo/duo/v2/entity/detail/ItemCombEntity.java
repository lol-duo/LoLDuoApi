package com.lolduo.duo.v2.entity.detail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "item_comb",indexes = {
        @Index(name="item_search_index",columnList ="item1, item2, item3",unique = true)})
public class ItemCombEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item1")
    private Long item1;

    @Column(name = "item2")
    private Long item2;

    @Column(name = "item3")
    private Long item3;

}
