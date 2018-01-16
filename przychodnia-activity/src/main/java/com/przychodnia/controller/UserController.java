package com.przychodnia.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.przychodnia.model.User;
import com.przychodnia.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "registration", method = RequestMethod.GET)
	public String registrationFormGET(Model model) {
		model.addAttribute("user", new User());
		return "index";
	}

	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public String registrationFormPOST(Model model, User user, BindingResult result, final HttpServletRequest request) {
		try {

			if (result.getErrorCount() == 0) {
				userService.save(user);
				model.addAttribute("success", "success.user.create");
			} else {
				model.addAttribute("error", "size.userForm.password");
			}
		} catch (DataIntegrityViolationException ex) {
			model.addAttribute("error", "errors.user.not.unique");
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", "errors.password.not.equals");
			return "index";
		}
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout, User user) {
		if (error != null) {
			model.addAttribute("error", "error.user.login");
		}
		if (logout != null)
			model.addAttribute("success", "success.user.logout");

		return "index";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

}
