package com.quizz.AccountService.Repository;

import com.quizz.AccountService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByName(String name);
    boolean existsByname(String name);
}
