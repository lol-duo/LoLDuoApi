package com.lolduo.duo.repository;

import com.lolduo.duo.entity.UserIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdRepository extends JpaRepository<UserIdEntity, String> {
}
