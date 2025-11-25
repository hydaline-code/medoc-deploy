package com.medoc.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medoc.entity.Ordo;
import com.medoc.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.answer IS NOT NULL and (q.ordo.id = :ordoId AND q.answerer.id = :userId)")
	int countAnsweredQuestions(@Param("ordoId") Integer ordoId, @Param("userId") Integer userId);
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.ordo.id = :ordoId AND q.answerer.id = :userId")
	int countApprovedQuestions(@Param("ordoId") Integer ordoId, @Param("userId") Integer userId);
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.answer IS NOT NULL and q.ordo.id =?1")
	int countAnsweredAllQuestions(Integer ordoId);
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.ordo.id =?1")
	int countApprovedAllQuestions(Integer ordoId);
	
	@Query("SELECT q FROM Question q WHERE q.ordo.id = ?1")
	Page<Question> findAll(Integer ordoId, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.ordo.id = ?1")
	Page<Question> findByOrdoId(Integer ordoId, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.ordo.id = :ordoId AND q.answerer.id = :userId")
	Page<Question> findAllByOrdoAndUser(@Param("ordoId") Integer ordoId, @Param("userId") Integer userId, Pageable pageable);

	List<Question> findByOrdo(Ordo ordo);
	
	
}
