package com.medoc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.medoc.entity.User;
import com.medoc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class ControllerHelper {
	@Autowired private UserService userService;
	
	public User getAuthenticatedUser(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedUser(request);				
		return userService.getUserByEmail(email);
	}
}
