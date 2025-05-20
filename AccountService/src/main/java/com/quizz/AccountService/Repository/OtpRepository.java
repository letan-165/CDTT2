package com.quizz.AccountService.Repository;

import com.quizz.AccountService.Entity.Otp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends CrudRepository<Otp,String> {
}
