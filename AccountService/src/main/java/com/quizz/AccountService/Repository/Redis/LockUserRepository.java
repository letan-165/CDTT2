package com.quizz.AccountService.Repository.Redis;

import com.quizz.AccountService.Entity.Redis.LockUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockUserRepository extends CrudRepository<LockUser,String> {
}
