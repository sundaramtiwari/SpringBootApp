package com.sundaram.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@ComponentScan
public class PostgressConnection {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	private static DataSource dataSource1;

	@Autowired
	private DataSource dataSource;

	@Bean
	public DataSource dataSource() throws SQLException {
		if (dataSource != null) {
			return dataSource;
		} else if (dbUrl == null || dbUrl.isEmpty()) {
			dataSource = new HikariDataSource();
			dataSource1 = dataSource;
			return dataSource;
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			dataSource = new HikariDataSource(config);
			dataSource1 = dataSource;
			return dataSource;
		}
	}

	public static Connection getConnection() throws SQLException {
		return dataSource1.getConnection();
	}

	public static Statement getStatement() throws SQLException {
		return getConnection().createStatement();
	}
}
