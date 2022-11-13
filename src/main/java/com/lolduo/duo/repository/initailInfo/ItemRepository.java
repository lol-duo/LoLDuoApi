package com.lolduo.duo.repository.initailInfo;

import com.lolduo.duo.entity.initialInfo.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
}
