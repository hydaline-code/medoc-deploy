package com.medoc.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.medoc.ControllerHelper;
import com.medoc.entity.Ordo;
import com.medoc.entity.Question;
import com.medoc.entity.User;
import com.medoc.ordo.OrdoNotFoundException;
import com.medoc.ordo.OrdoService;
import com.medoc.user.UserNotFoundException;
import com.medoc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {

	@Autowired
	private ControllerHelper controllerHelper;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private OrdoService ordoService;

	@GetMapping("/ask_question/{ordoId}")
	public String askQuestion(@PathVariable(name = "ordoId") Integer ordoId) {
		return "redirect:/p/" + ordoId + "#qa";
	}

	@GetMapping("/questions/{ordoId}/{userId}")
	public String listQuestionsOfProduct(@PathVariable(name = "ordoId") Integer ordoId,@PathVariable(name = "userId") Integer userId, Model model,
			HttpServletRequest request) throws OrdoNotFoundException, UserNotFoundException {
		
		// Ensure order-detail CSS is included for the questions page
		model.addAttribute("includeOrderDetailCss", true);
		return listQuestionsOfOrdoByPage(model, request, ordoId,userId, 1, "askTime", "desc");
	}

	@GetMapping("/questions/{ordoId}/{userId}/page/{pageNum}")
	public String listQuestionsOfOrdoByPage(Model model, HttpServletRequest request,
			@PathVariable(name = "ordoId") Integer ordoId, @PathVariable(name = "userId") Integer userId,
			@PathVariable(name = "pageNum") int pageNum, String sortField, String sortDir)
			throws OrdoNotFoundException, UserNotFoundException {

		Page<Question> page = questionService.listQuestionsOfOrdo(ordoId, userId, pageNum, sortField, sortDir);
		List<Question> listQuestions = page.getContent();
		Ordo ordo = ordoService.get(ordoId);
		
		User user = controllerHelper.getAuthenticatedUser(request);

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listQuestions", listQuestions);
		model.addAttribute("ordo", ordo);
		model.addAttribute("user", user);

		// Include order-detail CSS so the questions list matches the detail page styling
		model.addAttribute("includeOrderDetailCss", true);

		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
		model.addAttribute("startCount", startCount);

		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("endCount", endCount);

		if (page.getTotalPages() > 1) {
			model.addAttribute("pageTitle", "Page " + pageNum + " | Questions for product: " + ordo.getShortName());
		} else {
			model.addAttribute("pageTitle", "Questions for product: " + ordo.getShortName());
		}

		return "ordonnances/ordo_questions";
	}
	

}
