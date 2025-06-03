package com.app.QuizService.Repository.ElasticSearch;

import com.app.QuizService.Entity.Elastic.SearchQuiz;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchQuizRepository extends ElasticsearchRepository<SearchQuiz,String> {
    List<SearchQuiz> findByTopicsContaining(String topic);
    List<SearchQuiz> findByTitleContainingOrTopicsContaining(String title, String topic);

}
