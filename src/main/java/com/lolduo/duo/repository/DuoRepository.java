package com.lolduo.duo.repository;

import com.lolduo.duo.entity.DuoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuoRepository extends JpaRepository<DuoEntity, Long> {
}
