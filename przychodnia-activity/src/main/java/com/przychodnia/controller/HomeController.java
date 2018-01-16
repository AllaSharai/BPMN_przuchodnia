package com.przychodnia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.przychodnia.model.User;

@Controller
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, User user) {
		return "index";
	}


	@RequestMapping(value = "/visit", method = RequestMethod.GET)
	public String visit() {
		return "visit";
	}
}
