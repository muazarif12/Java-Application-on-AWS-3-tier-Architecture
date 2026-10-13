package com.dpt.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

@Controller
public class register {


	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String DBusername;

	@Value("${spring.datasource.password}")
	private String DBpassword;


	@RequestMapping(value = "register", method = RequestMethod.GET)
	public ModelAndView registerform()
	{
		ModelAndView mv=new ModelAndView("register");

		return mv;
	}


	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView register(String firstName,String lastName,String email,String userName,String password) throws ClassNotFoundException
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		String message;
		//Add employee here
		String sql = "insert into Employee (first_name,last_name,email,username,password,regdate) values(?,?,?,?,?,CURDATE())";
		try (Connection con = DriverManager.getConnection(url, DBusername, DBpassword);
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, firstName);
			st.setString(2, lastName);
			st.setString(3, email);
			st.setString(4, userName);
			st.setString(5, password);
			st.executeUpdate();
			message = "user account has been added for " + HtmlUtils.htmlEscape(userName);

		} catch (SQLIntegrityConstraintViolationException ex) {

			message = "username " + HtmlUtils.htmlEscape(userName) + " is already taken";

		} catch (SQLException ex) {

			ex.printStackTrace();
			message = "registration failed, please try again later";

		}


		ModelAndView mv=new ModelAndView("register");
		mv.addObject("message", message);
		return mv;
	}


}
