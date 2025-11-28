// package com.medoc.user.controller;

// import java.io.IOException;
// import java.io.UnsupportedEncodingException;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.repository.query.Param;
// import org.springframework.mail.javamail.JavaMailSenderImpl;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.medoc.FileUploadUtil;
// import com.medoc.Utility;
// import com.medoc.entity.Role;
// import com.medoc.entity.User;
// import com.medoc.setting.EmailSettingBag;
// import com.medoc.setting.SettingService;
// import com.medoc.user.UserNotFoundException;
// import com.medoc.user.UserService;
// import com.medoc.user.export.UserCsvExporter;
// import com.medoc.user.export.UserExcelExporter;
// import com.medoc.user.export.UserPdfExporter;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Controller
// public class UserController {

// 	@Autowired
// 	private UserService service;

// 	@Autowired
// 	private SettingService settingService;

// 	@GetMapping("/users")
// 	public String listFirstPage(Model model) {
// 		return listByPage(1, model, "firstName", "asc", null,0);
// 	}

// 	@GetMapping("/users/page/{pageNum}")
// 	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
// 			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,
// 			@Param("roleId") Integer roleId) {
// 		System.out.println("Sort Field: " + sortField);
// 		System.out.println("Sort Order: " + sortDir);

// 		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword,roleId);

// 		List<User> listUsers = page.getContent();
// 		List<Role> listRoles = service.listRolesInForm();

// 		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
// 		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
// 		if (endCount > page.getTotalElements()) {
// 			endCount = page.getTotalElements();
// 		}

// 		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

// 		model.addAttribute("currentPage", pageNum);
// 		model.addAttribute("totalPages", page.getTotalPages());
// 		model.addAttribute("startCount", startCount);
// 		model.addAttribute("endCount", endCount);
// 		model.addAttribute("totalItems", page.getTotalElements());
// 		model.addAttribute("listUsers", listUsers);
// 		model.addAttribute("sortField", sortField);
// 		model.addAttribute("sortDir", sortDir);
// 		model.addAttribute("reverseSortDir", reverseSortDir);
// 		model.addAttribute("keyword", keyword);
// 		model.addAttribute("listRoles", listRoles);

// 		return "users/users";
// 	}

// 	@GetMapping("/users/new")
// 	public String newUser(Model model) {
// 		List<Role> listRoles = service.listRoles();

// 		User user = new User();
// 		user.setEnabled(true);

// 		model.addAttribute("user", user);
// 		model.addAttribute("listRoles", listRoles);
// 		model.addAttribute("pageTitle", "Create New User");

// 		return "users/user_form";
// 	}

// 	@GetMapping("/register")
// 	public String showRegisterForm(Model model) {

// 		model.addAttribute("pageTitle", "User Registration");
// 		model.addAttribute("user", new User());

// 		return "register/register_form";
// 	}

// 	@PostMapping("/users/save")
// 	public String saveUser(User user, RedirectAttributes redirectAttributes,
// 			@RequestParam("image") MultipartFile multipartFile) throws IOException {

// 		if (!multipartFile.isEmpty()) {
// 			// Get file extension from original filename
// 			String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
// 			String fileExtension = "";
// 			if (originalFileName.contains(".")) {
// 				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
// 			}
			
// 			User savedUser = service.save(user);
			
// 			// Use user ID as filename
// 			String fileName = savedUser.getId() + fileExtension;
// 			user.setPhotos(fileName);
// 			service.save(user);

// 			// Save directly in user-photos directory
// 			String uploadDir = "user-photos";
// 			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

// 		} else {
// 			if (user.getPhotos() != null && user.getPhotos().isEmpty())
// 				user.setPhotos(null);
// 			service.save(user);
// 		}

// 		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

// 		return getRedirectURLtoAffectedUser(user);
// 	}

// 	private String getRedirectURLtoAffectedUser(User user) {
// 		String firstPartOfEmail = user.getEmail().split("@")[0];
// 		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
// 	}

// 	@PostMapping("/create_user")
// 	public String createCustomer(User user, Model model, HttpServletRequest request,
// 			@RequestParam("image") MultipartFile multipartFile) throws MessagingException, IOException {

// 		if (!multipartFile.isEmpty()) {
// 			// Get file extension from original filename
// 			String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
// 			String fileExtension = "";
// 			if (originalFileName.contains(".")) {
// 				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
// 			}
			
// 			User savedUser = service.registerCustomer(user);
			
// 			// Use user ID as filename
// 			String fileName = savedUser.getId() + fileExtension;
// 			user.setPhotos(fileName);
// 			service.registerCustomer(user);

// 			// Save directly in user-photos directory
// 			String uploadDir = "user-photos";
// 			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

// 		} else {
// 			if (user.getPhotos() != null && user.getPhotos().isEmpty())
// 				user.setPhotos(null);
// 			service.registerCustomer(user);
// 		}

// 		sendVerificationEmail(request, user);

// 		model.addAttribute("pageTitle", "Registration Succeeded!");

// 		return "/register/register_success";
// 	}

// 	private void sendVerificationEmail(HttpServletRequest request, User user)
// 			throws UnsupportedEncodingException, MessagingException {
// 		EmailSettingBag emailSettings = settingService.getEmailSettings();
// 		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

// 		String toAddress = user.getEmail();
// 		String subject = emailSettings.getCustomerVerifySubject();
// 		String content = emailSettings.getCustomerVerifyContent();

// 		MimeMessage message = mailSender.createMimeMessage();
// 		MimeMessageHelper helper = new MimeMessageHelper(message);

// 		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
// 		helper.setTo(toAddress);
// 		helper.setSubject(subject);

// 		content = content.replace("[[name]]", user.getFullName());

// 		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + user.getVerificationCode();

// 		content = content.replace("[[URL]]", verifyURL);

// 		helper.setText(content, true);

// 		mailSender.send(message);

// 		System.out.println("to Address: " + toAddress);
// 		System.out.println("Verify URL: " + verifyURL);
// 	}

// 	@GetMapping("/users/edit/{id}")
// 	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
// 		try {
// 			User user = service.get(id);
// 			List<Role> listRoles = service.listRoles();

// 			model.addAttribute("user", user);
// 			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
// 			model.addAttribute("listRoles", listRoles);

// 			return "users/user_form";
// 		} catch (UserNotFoundException ex) {
// 			redirectAttributes.addFlashAttribute("message", ex.getMessage());
// 			return "redirect:/users";
// 		}
// 	}

// 	@GetMapping("/users/delete/{id}")
// 	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
// 			RedirectAttributes redirectAttributes) {
// 		try {
// 			service.delete(id);
// 			;
// 			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
// 		} catch (UserNotFoundException ex) {
// 			redirectAttributes.addFlashAttribute("message", ex.getMessage());
// 		}

// 		return "redirect:/users";
// 	}

// 	@GetMapping("/users/{id}/enabled/{status}")
// 	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
// 			RedirectAttributes redirectAttributes) {
// 		service.updateUserEnabledStatus(id, enabled);
// 		String status = enabled ? "enabled" : "disabled";
// 		String message = "The user ID " + id + " has been " + status;
// 		redirectAttributes.addFlashAttribute("message", message);

// 		return "redirect:/users";
// 	}

// 	@GetMapping("/users/export/csv")
// 	public void exportToCSV(HttpServletResponse response) throws IOException {
// 		List<User> listUsers = service.listAll();
// 		UserCsvExporter exporter = new UserCsvExporter();
// 		exporter.export(listUsers, response);
// 	}

// 	@GetMapping("/users/export/excel")
// 	public void exportToExcel(HttpServletResponse response) throws IOException {
// 		List<User> listUsers = service.listAll();

// 		UserExcelExporter exporter = new UserExcelExporter();
// 		exporter.export(listUsers, response);
// 	}

// 	@GetMapping("/users/export/pdf")
// 	public void exportToPDF(HttpServletResponse response) throws IOException {
// 		List<User> listUsers = service.listAll();

// 		UserPdfExporter exporter = new UserPdfExporter();
// 		exporter.export(listUsers, response);
// 	}

// 	@GetMapping("/verify")
// 	public String verifyAccount(@Param("code") String code, Model model) {
// 		boolean verified = service.verify(code);

// 		return "register/" + (verified ? "verify_success" : "verify_fail");
// 	}
// }


package com.medoc.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medoc.FileUploadUtil;
import com.medoc.Utility;
import com.medoc.email.EmailService;
import com.medoc.entity.Role;
import com.medoc.entity.User;
import com.medoc.user.UserNotFoundException;
import com.medoc.user.UserService;
import com.medoc.user.export.UserCsvExporter;
import com.medoc.user.export.UserExcelExporter;
import com.medoc.user.export.UserPdfExporter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private EmailService emailService;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null, 0);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,
			@Param("roleId") Integer roleId) {

		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword, roleId);

		List<User> listUsers = page.getContent();
		List<Role> listRoles = service.listRolesInForm();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listRoles", listRoles);

		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();

		User user = new User();
		user.setEnabled(true);

		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");

		return "users/user_form";
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {

		model.addAttribute("pageTitle", "User Registration");
		model.addAttribute("user", new User());

		return "register/register_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileExtension = "";

			if (originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			User savedUser = service.save(user);
			String fileName = savedUser.getId() + fileExtension;
			user.setPhotos(fileName);
			service.save(user);

			String uploadDir = "user-photos";
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos() != null && user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@PostMapping("/create_user")
	public String createCustomer(User user, Model model, HttpServletRequest request,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileExtension = "";

			if (originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			User savedUser = service.registerCustomer(user);

			String fileName = savedUser.getId() + fileExtension;
			user.setPhotos(fileName);
			service.registerCustomer(user);

			String uploadDir = "user-photos";
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos() != null && user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.registerCustomer(user);
		}

		sendVerificationEmail(request, user);

		model.addAttribute("pageTitle", "Registration Succeeded!");

		return "/register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, User user) {

		String toAddress = user.getEmail();
		String subject = "Veuillez vérifier votre compte";

		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + user.getVerificationCode();

		String content = "<p>Bonjour <b>" + user.getFullName() + "</b>,</p>"
				+ "<p>Merci de vous être inscrit. Veuillez cliquer sur le lien ci-dessous pour vérifier votre compte :</p>"
				+ "<p><a href=\"" + verifyURL + "\">Vérifier mon compte</a></p>"
				+ "<br>"
				+ "<p>Si vous n'avez pas créé de compte, ignorez cet email.</p>";

		emailService.sendEmail(toAddress, subject, content);
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);

			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/users";
	}

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verified = service.verify(code);
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
}
