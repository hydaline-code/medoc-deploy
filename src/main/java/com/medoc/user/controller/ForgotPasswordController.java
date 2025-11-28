// package com.medoc.user.controller;

// import java.io.UnsupportedEncodingException;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import jakarta.servlet.http.HttpServletRequest;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.repository.query.Param;
// import org.springframework.mail.javamail.JavaMailSenderImpl;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;

// import com.medoc.Utility;
// import com.medoc.entity.User;
// import com.medoc.setting.EmailSettingBag;
// import com.medoc.setting.SettingService;
// import com.medoc.user.UserNotFoundException;
// import com.medoc.user.UserService;


// @Controller
// public class ForgotPasswordController {
// 	@Autowired private UserService userService;
// 	@Autowired private SettingService settingService;
	
// 	@GetMapping("/forgot_password")
// 	public String showRequestForm() {
// 		return "users/forgot_password_form";
// 	}
	
// 	@PostMapping("/forgot_password")
// 	public String processRequestForm(HttpServletRequest request, Model model) {
// 		String email = request.getParameter("email");
// 		try {
// 			String token = userService.updateResetPasswordToken(email);
// 			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
// 			sendEmail(link, email);
			
// 			model.addAttribute("message", "We have sent a reset password link to your email."
// 					+ " Please check.");
// 		} catch (UserNotFoundException e) {
// 			model.addAttribute("error", e.getMessage());
// 		} catch (UnsupportedEncodingException | MessagingException e) {
// 			model.addAttribute("error", "Could not send email");
// 		}
		
// 		return "users/forgot_password_form";
// 	}
	
// 	private void sendEmail(String link, String email) 
// 			throws UnsupportedEncodingException, MessagingException {
// 		EmailSettingBag emailSettings = settingService.getEmailSettings();
// 		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
// 		String toAddress = email;
// 		String subject = "Here's the link to reset your password";
		
// 		String content = "<p>Hello,</p>"
// 				+ "<p>You have requested to reset your password.</p>"
// 				+ "Click the link below to change your password:</p>"
// 				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
// 				+ "<br>"
// 				+ "<p>Ignore this email if you do remember your password, "
// 				+ "or you have not made the request.</p>";
		
// 		MimeMessage message = mailSender.createMimeMessage();
// 		MimeMessageHelper helper = new MimeMessageHelper(message);
		
// 		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
// 		helper.setTo(toAddress);
// 		helper.setSubject(subject);		
		
// 		helper.setText(content, true);
// 		mailSender.send(message);
// 	}
	
// 	@GetMapping("/reset_password")
// 	public String showResetForm(@Param("token") String token, Model model) {
// 		User user = userService.getByResetPasswordToken(token);
// 		if (user != null) {
// 			model.addAttribute("token", token);
// 		} else {
// 			model.addAttribute("pageTitle", "Invalid Token");
// 			model.addAttribute("message", "Invalid Token");
// 			return "message";
// 		}
		
// 		return "users/reset_password_form";
// 	}
	
// 	@PostMapping("/reset_password")
// 	public String processResetForm(HttpServletRequest request, Model model) {
// 		String token = request.getParameter("token");
// 		String password = request.getParameter("password");
		
// 		try {
// 			userService.updatePassword(token, password);
			
// 			model.addAttribute("pageTitle", "Reset Your Password");
// 			model.addAttribute("title", "Reset Your Password");
// 			model.addAttribute("message", "You have successfully changed your password.");
			
// 		} catch (UserNotFoundException e) {
// 			model.addAttribute("pageTitle", "Invalid Token");
// 			model.addAttribute("message", e.getMessage());
// 		}	

// 		return "message";	
// 	}
// }



package com.medoc.user.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.medoc.Utility;
import com.medoc.entity.User;
import com.medoc.user.UserNotFoundException;
import com.medoc.user.UserService;
import com.medoc.setting.SettingService;
import com.medoc.email.EmailService;

@Controller
public class ForgotPasswordController {

    @Autowired private UserService userService;
    @Autowired private SettingService settingService;

    @Autowired 
    private EmailService emailService;

    @GetMapping("/forgot_password")
    public String showRequestForm() {
        return "users/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        try {
            String token = userService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;

            sendEmail(link, email);

            model.addAttribute("message", 
                "We have sent a reset password link to your email. Please check.");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Could not send email");
        }

        return "users/forgot_password_form";
    }

    private void sendEmail(String link, String email) {
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you remember your password "
                + "or you have not made the request.</p>";

        emailService.sendEmail(email, subject, content);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        if (user != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "users/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            userService.updatePassword(token, password);

            model.addAttribute("pageTitle", "Reset Your Password");
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", 
                "You have successfully changed your password.");

        } catch (UserNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", e.getMessage());
        }

        return "message";
    }
}
