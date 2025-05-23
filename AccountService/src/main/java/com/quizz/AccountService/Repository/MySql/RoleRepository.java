package com.quizz.AccountService.Repository.MySql;

import com.quizz.AccountService.Entity.MySql.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
}
