package com.example.demo.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.vo.Greeting;

@RestController
public class HomeController {
	public static Logger logger = Logger.getLogger(HomeController.class.getName());
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/")
	public String home() {
		logger.info("home() Info...");
		logger.debug("home() Debug...");
		return "Hello Spring Boot!!";
	}
	
	
	 

	    @RequestMapping("/greeting")
	    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
	        return new Greeting(counter.incrementAndGet(),
	                            String.format(template, name));
	    }
}
