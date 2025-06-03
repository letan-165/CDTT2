package com.app.QuizService.Repository.MongoDB;

import com.app.QuizService.Entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz,String> {
    List<Quiz> findAllByTeacherID(String teacherID);
}
