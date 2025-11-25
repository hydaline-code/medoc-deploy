package com.medoc.question;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medoc.ControllerHelper;
import com.medoc.Utility;
import com.medoc.entity.Ordo;
import com.medoc.entity.Question;
import com.medoc.entity.User;
import com.medoc.ordo.OrdoNotFoundException;
import com.medoc.setting.EmailSettingBag;
import com.medoc.setting.SettingService;
import com.medoc.user.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class QuestionRestController {

	@Autowired
	private ControllerHelper controllerHelper;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private SettingService settingService;

	@PostMapping("/post_question/{productId}")
	public ResponseEntity<?> postQuestion(@RequestBody Question question,
			@PathVariable(name = "productId") Integer productId, HttpServletRequest request) {

		try {
			User user = controllerHelper.getAuthenticatedUser(request);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
			}

			// Save the question
			questionService.saveNewQuestion(question, user, productId);

			// Attempt to send email but don't fail the request if email sending fails
			try {
				sendOrderConfirmationEmailPharmaToClient(request, question);
			} catch (Exception ex) {
				// Log and continue - question is already saved
				ex.printStackTrace();
			}

			// Build minimal response to return to client for immediate UI insert
			java.util.Map<String, Object> resp = new java.util.HashMap<>();
			resp.put("id", question.getId());
			resp.put("questionContent", question.getQuestionContent());
			resp.put("askTime", question.getAskTime());
			resp.put("answer", question.getAnswer());
			resp.put("answerer", question.getAnswerer() != null ? question.getAnswerer().getFirstName() + " " + question.getAnswerer().getLastName() : null);
			resp.put("asker", user.getFirstName() + " " + user.getLastName());

			return ResponseEntity.ok(resp);
		} catch (OrdoNotFoundException | UserNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to post question");
		}
	}

	@PostMapping("/postQuestionAnswer/{productId}")
	public ResponseEntity<?> postAnswer(@RequestBody Question question,
			@PathVariable(name = "productId") Integer productId, HttpServletRequest request) {
		try {
			System.out.println("Received answer: " + question);
			// Ensure the caller is authenticated and set them as the answerer
			User user = controllerHelper.getAuthenticatedUser(request);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
			}

			question.setAnswerer(user);

			// Save the answer to the question (service will persist answerer if present)
			questionService.saveAnswerToQuestion(question, productId);

			// Attempt to send email but don't fail the response if email sending fails
			try {
				sendOrderConfirmationEmailClientToPharma(request, question);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// Fetch updated question to return to client
			Question updated = questionService.get(productId);
			java.util.Map<String, Object> resp = new java.util.HashMap<>();
			resp.put("id", updated.getId());
			resp.put("answer", updated.getAnswer());
			resp.put("answerTime", updated.getAnswerTime());
			resp.put("answerer", updated.getAnswerer() != null ? updated.getAnswerer().getFirstName() + " " + updated.getAnswerer().getLastName() : null);

			return ResponseEntity.ok(resp);
		} catch (QuestionNotFoundException e) {
			e.printStackTrace();
			// Handle question not found
			return ResponseEntity.status(403).body("Question not found with ID " + productId);
		} catch (Exception e) {
			e.printStackTrace();
			// Other errors
			return ResponseEntity.status(500).body("An error occurred while posting the answer.");
		}
	}
	
	private void sendOrderConfirmationEmailPharmaToClient(HttpServletRequest request, Question question)
			throws UnsupportedEncodingException, MessagingException, QuestionNotFoundException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		// Vérifie si la question existe en base
        Question existingQuestion = questionService.get(question.getId());
        if (existingQuestion == null) {
            System.out.println("Erreur : la question n'existe pas.");
        }

        // Récupérer l’answerer depuis la BD si absent dans la requête
        if (question.getAnswerer() == null) {
            question.setAnswerer(existingQuestion.getAnswerer());
        }

        if (question.getAnswerer() == null) {
        	System.out.println("Erreur : l'utilisateur answerer est null même après récupération.");
        }

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
System.out.println("question: "+question);
		String toAddressClient = question.getOrdo().getUser().getEmail().toString();
		System.out.println("toAddressClient "+toAddressClient);

		if (toAddressClient != null) {
			String subject = "Réponse Client " ;
			String content = "A votre réponse un pharmacien dit: " + question.getAnswer();

			helper.setTo(toAddressClient);
			helper.setSubject(subject);

			helper.setText(content, true);
			mailSender.send(message);
		}

	}

	private void sendOrderConfirmationEmailClientToPharma(HttpServletRequest request, Question question)
			throws UnsupportedEncodingException, MessagingException, QuestionNotFoundException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		// Vérifie si la question existe en base
        Question existingQuestion = questionService.get(question.getId());
        if (existingQuestion == null) {
            System.out.println("Erreur : la question n'existe pas.");
        }

        // Récupérer l’answerer depuis la BD si absent dans la requête
        if (question.getAnswerer() == null) {
            question.setAnswerer(existingQuestion.getAnswerer());
        }

        if (question.getAnswerer() == null) {
        	System.out.println("Erreur : l'utilisateur answerer est null même après récupération.");
        }

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
System.out.println("question: "+question);
		String toAddressClient = question.getAnswerer().getEmail().toString();
		System.out.println("toAddressClient "+toAddressClient);

		if (toAddressClient != null) {
			String subject = "Réponse Client " ;
			String content = "A votre réponse ce client dit: " + question.getAnswer();

			helper.setTo(toAddressClient);
			helper.setSubject(subject);

			helper.setText(content, true);
			mailSender.send(message);
		}

	}

}
