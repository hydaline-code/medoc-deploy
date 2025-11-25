package com.medoc.ordo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medoc.ControllerHelper;
import com.medoc.FileUploadUtil;
import com.medoc.Utility;
import com.medoc.entity.Ordo;
import com.medoc.entity.OrdoImage;
import com.medoc.entity.Question;
import com.medoc.entity.User;
import com.medoc.question.QuestionNotFoundException;
import com.medoc.question.QuestionService;
import com.medoc.security.MedocUserDetails;
import com.medoc.setting.EmailSettingBag;
import com.medoc.setting.SettingService;
import com.medoc.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrdoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdoController.class);
	@Autowired
	private OrdoService ordoService;
	
	@Autowired
	private OrdoRepository ordoRepository;

	@Autowired
	private SettingService settingService;

	@Autowired
	private UserService userService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ControllerHelper controllerHelper;

	@GetMapping("/medocs")
	public String listOrdonnances(Model model) {

		MedocUserDetails user = getAuthenticatedUser();
		Integer userId = user.getId(); // Récupérer l'ID de l'utilisateur authentifié

		if (userId != null) {
			// Récupérer les ordonnances via le service
			List<Ordo> listOrdonnances = ordoService.getOrdonnancesByUser(userId);

			// Ajouter les données au modèle
			// model.addAttribute("userId", userId);
			model.addAttribute("listOrdonnances", listOrdonnances);
		}

		return "ordonnances/ordonnances_list";
	}

	@GetMapping("/medocs/new")
	public String newOrdo(Model model) {
	    Ordo ordo = new Ordo();
	    ordo.setEnabled(true);

	    // Ajouter une liste vide de questions pour éviter l'erreur Thymeleaf
	    List<Question> questions = new ArrayList<>();
	    
	    model.addAttribute("ordo", ordo);
	    model.addAttribute("questions", questions); // ✅ Ajout ici
	    model.addAttribute("pageTitle", "Create New Ordonnance");
	    model.addAttribute("numberOfExistingExtraImages", 0);

	    return "ordonnances/ordonnance_form";
	}


	@PostMapping("/medocs/save")
	public String saveOrdo(Ordo ordo, RedirectAttributes ra, HttpServletRequest request,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames)
			throws IOException, MessagingException {
		// Récupérer l'utilisateur authentifié
		MedocUserDetails userDetails = getAuthenticatedUser();
		if (userDetails != null) {
			User user = userDetails.getUser(); // Extraire l'objet User
			ordo.setUser(user); // Assigner l'utilisateur à l'ordonnance
		}
		System.out.println("imageIDs: "+Arrays.toString(imageIDs));
		System.out.println("imageNames: "+Arrays.toString(imageNames));
		setMainImageName(mainImageMultipart, ordo);
		setExistingExtraImageNames(imageIDs, imageNames, ordo);
		setNewExtraImageNames(extraImageMultiparts, ordo);

		Ordo savedOrdo = ordoService.save(ordo);
		System.out.println(savedOrdo.getImages().toString());

		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedOrdo);

		deleteExtraImagesWeredRemovedOnForm(ordo);

		sendOrderConfirmationEmailClientPharma(request, ordo);

		ra.addFlashAttribute("message", "The Ordonnance has been saved successfully.");

		return "redirect:/medocs";
	}

	@GetMapping("/medocs/{id}/enabled/{status}")
	public String updateOrdoEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		ordoService.updateOrdoEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Ordonannce has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/medocs";
	}

	@GetMapping("/medocs/delete/{id}")
	public String deleteOrdo(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordoService.delete(id);

			String ordoExtraImagesDir = "../ordo-images/" + id + "/extras";
			String ordoImagesDir = "../ordo-images/" + id;

			FileUploadUtil.removeDir(ordoExtraImagesDir);
			FileUploadUtil.removeDir(ordoImagesDir);

			redirectAttributes.addFlashAttribute("message", "The Ordonannce has been deleted successfully");
		} catch (OrdoNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/medocs";
	}

	@GetMapping("/medocs/edit/{id}")
	public String editOrdo(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) throws QuestionNotFoundException {

		try {
			Ordo ordo = ordoService.get(id);
			System.out.println(ordo);
			List<Question> questions = questionService.getQuestionsByOrdo(ordo);
			System.out.println(questions);

			Integer numberOfExistingExtraImages = ordo.getImages().size();

			User user = controllerHelper.getAuthenticatedUser(request);

			List<Question> listQuestions = questionService.getTop3OrdoAllQuestions(ordo.getId());
System.out.println(ordo.getImages().toString());
			model.addAttribute("listQuestions", listQuestions);
	
			model.addAttribute("user", user);
			model.addAttribute("ordo", ordo);
			model.addAttribute("questions", questions);
			model.addAttribute("ordo", ordo);
			model.addAttribute("pageTitle", "Edit Ordo");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "ordonnances/ordonnance_form";

		} catch (OrdoNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/medocs";
		}
	}

	@GetMapping("/medocs/detail/{id}")
	public String viewMedocDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra, HttpServletRequest request) {
		try {
			Ordo ordo = ordoService.get(id);
			User user = controllerHelper.getAuthenticatedUser(request);
			Integer userId = user != null ? user.getId() : 0;
			
			// Fetch all questions for this order so answers are visible to viewers
			List<Question> listQuestions = questionService.findByOrdo(ordo);
			int numberOfQuestions = listQuestions != null ? listQuestions.size() : 0;
			int numberOfAnsweredQuestions = 0;
			if (listQuestions != null) {
				numberOfAnsweredQuestions = (int) listQuestions.stream()
					.filter(q -> q.getAnswer() != null && !q.getAnswer().isEmpty())
					.count();
			}
			
			model.addAttribute("ordo", ordo);
			model.addAttribute("user", user);
			// Ensure the order-detail stylesheet is included in the <head>
			model.addAttribute("includeOrderDetailCss", true);
			model.addAttribute("pageTitle", "Order #" + ordo.getId() + " - Details");
			model.addAttribute("listQuestions", listQuestions);
			model.addAttribute("numberOfQuestions", numberOfQuestions);
			model.addAttribute("numberOfAnsweredQuestions", numberOfAnsweredQuestions);

			return "ordonnances/ordo_detail";

		} catch (OrdoNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/medocs";
		}
	}

	@GetMapping("/orders")
	public String viewOrders(Model model, HttpServletRequest request) {
		try {
			User user = controllerHelper.getAuthenticatedUser(request);
			List<Ordo> listOrdos;
			String pageTitle;
			
			// Role-based filtering
			if (user.hasRole("Admin")) {
				// Admin sees all orders
				listOrdos = ordoRepository.findAll();
				pageTitle = "All Orders";
				model.addAttribute("isAdmin", true);
			} else if (user.hasRole("Pharmacie")) {
				// Pharmacy sees only enabled orders
				listOrdos = ordoService.listEnabledOrdo();
				pageTitle = "Available Prescriptions";
				model.addAttribute("isPharmacy", true);
			} else {
				// Client sees only their own orders
				listOrdos = ordoRepository.findByUserId(user.getId());
				pageTitle = "My Orders";
				model.addAttribute("isClient", true);
			}
			
			// Calculate statistics
			long totalOrders = listOrdos.size();
			long activeOrders = listOrdos.stream().filter(Ordo::isEnabled).count();
			long pendingOrders = totalOrders - activeOrders;
			
			model.addAttribute("listOrdos", listOrdos);
			model.addAttribute("pageTitle", pageTitle);
			model.addAttribute("totalOrders", totalOrders);
			model.addAttribute("activeOrders", activeOrders);
			model.addAttribute("pendingOrders", pendingOrders);
			model.addAttribute("currentUser", user);

			return "ordonnances/orders";
		} catch (Exception e) {
			return "error/500";
		}
	}

	@GetMapping("/ordo/{id}")
	public String viewOrdoDetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {

		try {

			User user = controllerHelper.getAuthenticatedUser(request);

			Ordo ordo = ordoService.get(id);
			List<Question> listQuestions;
			int numberOfQuestions;
			int numberOfAnsweredQuestions;

			if (user != null && (user.hasRole("Admin") || user.hasRole("Pharmacie"))) {
				// Admins and pharmacies see all questions and aggregate counts
				listQuestions = questionService.getTop3OrdoAllQuestions(ordo.getId());
				numberOfQuestions = questionService.getNumberOfAllQuestions(ordo.getId());
				numberOfAnsweredQuestions = questionService.getNumberOfAnsweredAllQuestions(ordo.getId());
			} else {
				listQuestions = questionService.getTop3OrdoQuestions(ordo.getId(), user != null ? user.getId() : 0);
				numberOfQuestions = questionService.getNumberOfQuestions(ordo.getId(), user != null ? user.getId() : 0);
				numberOfAnsweredQuestions = questionService.getNumberOfAnsweredQuestions(ordo.getId(), user != null ? user.getId() : 0);
			}

			model.addAttribute("listQuestions", listQuestions);
			model.addAttribute("numberOfQuestions", numberOfQuestions);
			model.addAttribute("numberOfAnsweredQuestions", numberOfAnsweredQuestions);
			// Include the order-detail CSS in the page head for this view
			model.addAttribute("includeOrderDetailCss", true);

			model.addAttribute("ordo", ordo);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", ordo.getShortName());

			return "ordonnances/ordo_detail";
		} catch (OrdoNotFoundException e) {
			return "error/404";
		}
	}

	private void sendOrderConfirmationEmailClientPharma(HttpServletRequest request, Ordo ordo)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String ordoTime = dateFormatter.format(ordo.getUpdatedTime());

		String toAddressClient = ordo.getUser().getEmail();

		if (toAddressClient != null) {
			String subject = emailSettings.getOrderConfirmationSubjectClient();
			String content = emailSettings.getOrderConfirmationContentClient();

			helper.setTo(toAddressClient);
			helper.setSubject(subject);

			content = content.replace("[[name]]", ordo.getUser().getFullName());
			// System.out.println(ordoTime);
			subject = subject.replace("[[ordoTime]]", ordoTime);

			helper.setText(content, true);
			mailSender.send(message);
		}

		List<User> pharmacies = userService.findUsersByRole("Pharmacie"); // Récupérer les utilisateurs ayant ce rôle

		if (pharmacies.isEmpty()) {
			System.out.println("Aucun utilisateur avec le rôle 'Pharmacie' trouvé.");
			return;
		}

		for (User pharmacy : pharmacies) {
			String subject = emailSettings.getOrderConfirmationSubjectPharma();
			String content = emailSettings.getOrderConfirmationContentPharma();

			content = content.replace("[[name]]", pharmacy.getFullName());
			subject = subject.replace("[[ordoTime]]", ordoTime);
			content = content.replace("[[userName]]", ordo.getUser().getFullName());
			String verifyURL = Utility.getSiteURL(request);

			content = content.replace("[[URL]]", verifyURL);

			helper.setSubject(subject);
			helper.setTo(pharmacy.getEmail());
			helper.setText(content, true);

			mailSender.send(message); // Envoi de l'email
		}
		System.out.println("Email envoyé à toutes les pharmacies.");

	}

	private void deleteExtraImagesWeredRemovedOnForm(Ordo ordo) {
		String extraImageDir = "../ordo-images/" + ordo.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!ordo.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}

			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Ordo ordo) {
		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<OrdoImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new OrdoImage(id, name, ordo));
		}

		ordo.setImages(images);

	}

	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Ordo savedOrdo) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../ordo-images/" + savedOrdo.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../ordo-images/" + savedOrdo.getId() + "/extras";

			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty())
					continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}

	}

	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Ordo ordo) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!ordo.containsImageName(fileName)) {
						ordo.addExtraImage(fileName);
					}
				}
			}
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Ordo ordo) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			ordo.setMainImage(fileName);
		}
	}

	// Méthode pour récupérer l'utilisateur authentifié
	public MedocUserDetails getAuthenticatedUser() {
		// Récupérer l'utilisateur authentifié via Spring Security
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MedocUserDetails user = (MedocUserDetails) authentication.getPrincipal();

		if (authentication != null && authentication.isAuthenticated()) {
			// Récupérer et retourner les détails de l'utilisateur authentifié
			return (MedocUserDetails) authentication.getPrincipal();
		}

		return null; // Retourne null si l'utilisateur n'est pas authentifié
	}
}
