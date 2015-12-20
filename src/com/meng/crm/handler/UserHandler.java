package com.meng.crm.handler;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.meng.crm.entity.User;
import com.meng.crm.service.UserService;

@RequestMapping("/user")
@Controller
public class UserHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceBundleMessageSource message;
	
	
	@RequestMapping("/login")
	public String login(@RequestParam(value="username",required=false) String name,
						@RequestParam(value="password",required=false) String password,
						HttpSession session,RedirectAttributes attributes,
						Locale locale) {
		
		User user = userService.getByName(name, password);
		
		if(user == null) {
			
			attributes.addFlashAttribute("message", message.getMessage("error.login.message", null, locale));
			
			return "redirect:/index";
		}
		
		session.setAttribute("user", user);
		
		return "home/success";
	}
	
}
