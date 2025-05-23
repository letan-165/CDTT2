package com.quizz.AccountService.Repository.Redis;

import com.quizz.AccountService.Entity.Redis.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token,String> {
}
