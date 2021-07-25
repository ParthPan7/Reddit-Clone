package com.example.redditclone.service;

import javax.mail.internet.MimeMessage;

import org.hibernate.pretty.MessageHelper;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.redditclone.execptions.RedditException;
import com.example.redditclone.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService 
{
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;
	
	@Async
	void sendMail(NotificationEmail notificationEmail) throws RedditException
	{
		MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("noreply@redditclone.com");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(mailContentBuilder.Build(notificationEmail.getBody()));
		};
		try
		{
			mailSender.send(mimeMessagePreparator);
			log.info("Activation email sent");
		}
		catch(MailException e)
		{
			throw new RedditException("Exception catched while sending mail to the client"+notificationEmail.getRecipient());
		}
	}
}
