package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByRoleName(String userRoleName);
}
