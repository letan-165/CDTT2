package com.quizz.AccountService.Repository.Redis;

import com.quizz.AccountService.Entity.Redis.Otp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends CrudRepository<Otp,String> {
}
