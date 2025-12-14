package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("SELECT r FROM UserRole r WHERE r.roleName = :name")
    Optional<UserRole> findByRoleName(@Param("name") String name);
}