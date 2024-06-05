package com.jobportal.Config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessagePropertiesConfig {
	
	@Bean
	public LocaleResolver localeResolver() {
		final SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US); // Set the default locale to US
		return slr;
	}

	/**
	 * Responsible for loading message resources from properties files.
	 * @return ReloadableResourceBundleMessageSource
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message"); // Path to the messages properties file
		messageSource.setCacheSeconds(3600); // Cache messages for 3600 seconds
		messageSource.setDefaultEncoding("UTF-8"); // Use UTF-8 encoding
		messageSource.setUseCodeAsDefaultMessage(true); // Use message code as default message if no message found
		return messageSource;
	}

	/**
	 * Configures a bean to use custom message source for validation messages.
	 * @return LocalValidatorFactoryBean
	 */
//	@Bean
//	public LocalValidatorFactoryBean validator() {
//		final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//		bean.setValidationMessageSource(messageSource()); // Set the custom message source
//		return bean;
//	}

}
