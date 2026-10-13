package com.dpt.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Creates the Employee table on startup if it is missing, so a fresh RDS
// database (which sits in a private subnet) needs no manual setup.
@Component
public class DatabaseInitializer implements ApplicationRunner {

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String DBusername;

	@Value("${spring.datasource.password}")
	private String DBpassword;

	@Override
	public void run(ApplicationArguments args) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String sql = "CREATE TABLE IF NOT EXISTS Employee ("
				+ " id int unsigned auto_increment not null,"
				+ " first_name varchar(250),"
				+ " last_name varchar(250),"
				+ " email varchar(250),"
				+ " username varchar(250),"
				+ " password varchar(250),"
				+ " regdate timestamp,"
				+ " primary key (id),"
				+ " unique key uk_employee_username (username)"
				+ ")";
		try (Connection con = DriverManager.getConnection(url, DBusername, DBpassword);
				Statement st = con.createStatement()) {
			st.execute(sql);
			System.out.println("Employee table is ready");
		} catch (SQLException ex) {
			// Keep the app running: pages that don't touch the database still work,
			// and the table is created on the next start once the database is reachable
			System.out.println("Could not create Employee table: " + ex.getMessage());
		}
	}

}
