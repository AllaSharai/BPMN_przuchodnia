package com.przychodnia.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DBConfig {

	
	@Value("${jdbc.driver:org.postgresql.Driver}")
	private String DRIVER;
	
	@Value("${jdbc.url:jdbc:postgresql://localhost/klinika}")
	private String URL;
	
	@Value("${jdbc.username:postgres}")
	private String USERNAME;
	
	@Value("${jdbc.password:kotkot}")
	private String PASSWORD;

	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(DRIVER);
	    dataSource.setUrl(URL);
	    dataSource.setUsername(USERNAME);
	    dataSource.setPassword(PASSWORD);
	    return dataSource;
	}
	

}
