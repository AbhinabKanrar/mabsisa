package com.mabsisa.portal.web.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticRouter {

	@GetMapping("/login")
	public String route() {
		return "index";
	}
	
}
