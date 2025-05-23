package com.quizz.AccountService.Repository.MySql;

import com.quizz.AccountService.Entity.MySql.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
}
