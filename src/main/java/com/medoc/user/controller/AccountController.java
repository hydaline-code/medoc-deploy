package com.medoc.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medoc.FileUploadUtil;
import com.medoc.entity.User;
import com.medoc.security.MedocUserDetails;
import com.medoc.user.UserService;


@Controller
public class AccountController {

	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal MedocUserDetails loggedUser,
			Model model) {
		String email = loggedUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		
		return "users/account_form";
		
	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal MedocUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		System.out.println("Updating account for user: " + user.getEmail());
		System.out.println("Photo file empty: " + multipartFile.isEmpty());
		
		if (!multipartFile.isEmpty()) {
			// Get file extension from original filename
			String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileExtension = "";
			if (originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}
			
			// Use user ID as filename
			String fileName = user.getId() + fileExtension;
			System.out.println("Uploading photo with name: " + fileName);
			
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			// Save directly in user-photos directory (no subdirectory)
			String uploadDir = "user-photos";
			System.out.println("Upload directory: " + uploadDir);
			
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			if (user.getPhotos() != null && user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
		
		return "redirect:/account";
	}	
}
