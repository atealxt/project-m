package projectm.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import projectm.ApplicationConfig;

@RestController
@RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
public class HomeController {

	@Resource
	private ApplicationConfig applicationConfig;

	@RequestMapping
	public String home() {
		return "Project M is online!";
	}
}
