package com.app.NotificationService.Repository;

import com.app.NotificationService.Entity.Personal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepository extends MongoRepository<Personal,String> {
}
