package com.medoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.medoc.security.MedocUserDetails;
import com.medoc.setting.SettingService;
import com.medoc.user.UserRepository;
import com.medoc.ordo.OrdoRepository;

@Controller
public class MainController {
	
	@Autowired private SettingService service;
	@Autowired private UserRepository userRepository;
	@Autowired private OrdoRepository ordoRepository;

	@GetMapping("")
	public String viewHomePage(Model model) {
		// Get total counts from database
		long totalUsers = userRepository.count();
		long totalOrders = ordoRepository.count();
		
		// Pass real data to the view
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalOrders", totalOrders);
		
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}
	
}
