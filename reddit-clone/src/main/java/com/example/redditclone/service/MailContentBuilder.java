package com.example.redditclone.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MailContentBuilder 
{
	//private final TemplateEngine templateEngine;
	private TemplateEngine templateEngine;
	String Build(String message)
	{
		Context context = new Context();
		context.setVariable("message", message);
		return templateEngine.process("mailTemplate", context);
	}
}
