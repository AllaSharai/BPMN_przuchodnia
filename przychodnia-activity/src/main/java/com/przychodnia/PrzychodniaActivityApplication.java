package com.przychodnia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.przychodnia.configuration.DBConfig;
import com.przychodnia.configuration.HibernateConfig;


@SpringBootApplication
@Import({ DBConfig.class, HibernateConfig.class })
@EnableJpaRepositories("com.przychodnia.respository")
@ComponentScan("com.przychodnia.*")
@Configuration
@EnableAutoConfiguration
public class PrzychodniaActivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrzychodniaActivityApplication.class, args);
	}
	

	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
