/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Beans definition for email sender
 *
 * @author jpizarro
 * @author gervasio.amy
 * @see JavaMailSenderImpl
 */
@Configuration
public class EmailConfig {

	@Value("${mail.host}")
	private String host;
	@Value("${mail.user}")
	private String user;
	@Value("${mail.pwd}")
	private String pwd;
	@Value("${mail.port}")
	private int port;
	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.authenticated}")
	private String isAuthenticated;
	@Value("${mail.start_tls_enable}")
	private String isStartTLSEnable;
	
	@Value("${mail.smtp.localhost}")
	private String mailSmtpLocalhost;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();

		javaMailSenderImpl.setProtocol(this.protocol);
		javaMailSenderImpl.setHost(this.host);
		javaMailSenderImpl.setPort(this.port);
		javaMailSenderImpl.setDefaultEncoding("UTF-8");

		if (Boolean.valueOf(isAuthenticated)) {
			// Create the message to send
			Properties props = new Properties();
			
			props.put("mail.smtp.auth", this.isAuthenticated);
			props.put("mail.smtp.starttls.enable", this.isStartTLSEnable);
			props.put("mail.smtp.quitwait", false);
			
			props.put("mail.smtp.localhost", mailSmtpLocalhost);
			props.put("mail.debug", "true");
			
			javaMailSenderImpl.setUsername(this.user);
			javaMailSenderImpl.setPassword(this.pwd);
			javaMailSenderImpl.setJavaMailProperties(props);
			
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
                protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, pwd);
				}
			});
			
			javaMailSenderImpl.setSession(session);
			
		} else{
			Properties props = new Properties();
			props.put("mail.smtp.localhost", mailSmtpLocalhost);
			props.put("mail.debug", "true");
			javaMailSenderImpl.setJavaMailProperties(props);			
		}

		return javaMailSenderImpl;
	}

	@Bean(name = "velocityEngineFactoryBean")
	public VelocityEngineFactoryBean velocityEngine() {
		VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
		Properties velocityProperties = new Properties();
		velocityProperties.setProperty("resource.loader", "class");
		velocityProperties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngineFactoryBean.setVelocityProperties(velocityProperties);
		return velocityEngineFactoryBean;
	}

}
