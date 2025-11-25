package com.medoc.question;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.medoc.entity.Ordo;
import com.medoc.entity.Question;
import com.medoc.entity.User;
import com.medoc.ordo.OrdoNotFoundException;
import com.medoc.ordo.OrdoRepository;
import com.medoc.user.UserNotFoundException;

@Service
public class QuestionService {
	
	public static final int QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING = 10;
	
	@Autowired
	private QuestionRepository questionRepo;
	
	@Autowired
	private OrdoRepository ordoRepo;
	
	public void saveNewQuestion(Question question, User asker, 
			Integer ordoId) throws UserNotFoundException, OrdoNotFoundException {
		Optional<Ordo> ordoById = ordoRepo.findById(ordoId);
		if (!ordoById.isPresent()) {
			throw new OrdoNotFoundException("Could not find ordo with ID " + ordoId);
		}
		question.setAskTime(new Date());
		question.setOrdo(ordoById.get());
		question.setAnswerer(asker);
		
		questionRepo.save(question);
	}
	
	// Méthode pour enregistrer la réponse à une question
	public void saveAnswerToQuestion(Question question,Integer questionId) throws QuestionNotFoundException {
        // Rechercher la question par son ID
        Optional<Question> questionById = questionRepo.findById(questionId);

        if (!questionById.isPresent()) {
            throw new QuestionNotFoundException("Question not found with ID " + questionId);
        }

        // Récupérer la question existante
        Question existingQuestion = questionById.get();

		// Mettre à jour la réponse de la question
		existingQuestion.setAnswer(question.getAnswer());

		existingQuestion.setAnswerTime(new Date());

		// If the incoming Question contains an answerer (the user who answered), save it
		if (question.getAnswerer() != null) {
			existingQuestion.setAnswerer(question.getAnswerer());
		}

		// Sauvegarder la question mise à jour dans la base de données
		questionRepo.save(existingQuestion);
    }
	
//	public void saveNewQuestionAnswer(Question questionInForm,
//			Integer questionId) throws QuestionNotFoundException {
//		Optional<Question> questionById = questionRepo.findById(questionId);
//		if (!questionById.isPresent()) {
//			throw new QuestionNotFoundException("Could not find question with ID " + questionId);
//		}
//		// Récupérer la question existante
//	    Question existingQuestion = questionById.get();
//	    
//	    // Mettre à jour les champs nécessaires
//	    existingQuestion.setAnswer(questionInForm.getAnswer()); // Mettre à jour uniquement la réponse
//	    existingQuestion.setAnswerTime(questionInForm.getAnswerTime()); // Mettre à jour l'heure de la réponse
//	    
//		
//		questionRepo.save(questionInForm);
//	}
	
	public int getNumberOfAnsweredQuestions(Integer ordoId, Integer userId) {
		return questionRepo.countAnsweredQuestions(ordoId,userId);
	}
	
	public int getNumberOfQuestions(Integer ordoId, Integer userId) {
		return questionRepo.countApprovedQuestions(ordoId,userId);
	}

	// Return counts across all users (for admin/pharmacie views)
	public int getNumberOfAnsweredAllQuestions(Integer ordoId) {
		return questionRepo.countAnsweredAllQuestions(ordoId);
	}

	public int getNumberOfAllQuestions(Integer ordoId) {
		return questionRepo.countApprovedAllQuestions(ordoId);
	}
	
//	public int getNumberOfAnsweredAllQuestions(Integer ordoId) {
//		return questionRepo.countAnsweredAllQuestions(ordoId);
//	}
//	
//	public int getNumberOfAllQuestions(Integer ordoId) {
//		return questionRepo.countApprovedAllQuestions(ordoId);
//	}
	
	public List<Question> getTop3OrdoAllQuestions(Integer ordoId) {
		Pageable pageable = PageRequest.of(0, 300, Sort.by("askTime").descending());
		Page<Question> result = questionRepo.findAll(ordoId, pageable);
		return result.getContent();
	}
	
	public List<Question> getTop3OrdoQuestions(Integer ordoId, Integer userId) {
	    Pageable pageable = PageRequest.of(0, 300, Sort.by("askTime").descending());
	    Page<Question> result = questionRepo.findAllByOrdoAndUser(ordoId, userId, pageable);
	    return result.getContent();
	}

	
	public Page<Question> listQuestionsOfOrdo(Integer ordoId,Integer userId, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending(); 
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING, sort);
		return questionRepo.findAllByOrdoAndUser(ordoId,userId, pageable);
	}
	
	public Page<Question> listAllQuestionsOfOrdo(Integer ordoId, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending(); 
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING, sort);
		return questionRepo.findByOrdoId(ordoId, pageable);
	}
	

	public List<Question> findByOrdo(Ordo ordo) {
	    return questionRepo.findByOrdo(ordo);
	}

	public Question get(Integer id) throws QuestionNotFoundException {
		try {
			return questionRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find any question with ID " + id);
		}
	}
	
	public List<Question> getQuestionsByOrdo(Ordo ordo) {
        return questionRepo.findByOrdo(ordo);
    }

}
