package com.lolduo.duo.repository;

import com.lolduo.duo.entity.PerkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerkRepository extends JpaRepository<PerkEntity, Long> {
    List<PerkEntity> findAllByType(Long type);
    List<PerkEntity> findAllByIdNotAndType(Long id, Long type);
    List<PerkEntity> findAllByParentAndType(Long parent, Long type);
}
