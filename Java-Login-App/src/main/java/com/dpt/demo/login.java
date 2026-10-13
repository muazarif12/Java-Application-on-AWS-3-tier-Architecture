package com.dpt.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

@Controller
public class login {

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String DBusername;

	@Value("${spring.datasource.password}")
	private String DBpassword;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(String userName, String password) throws ClassNotFoundException {

		// Local to each request: the controller is a single instance shared by all users
		String userId = "";
		String errorMessage = "Invalid username or password";

		Class.forName("com.mysql.cj.jdbc.Driver");
		// validate user credentials
		String query = "select username from Employee where username = ? and password = ?";
		try (Connection con = DriverManager.getConnection(url, DBusername, DBpassword);
				PreparedStatement st = con.prepareStatement(query)) {
			st.setString(1, userName);
			st.setString(2, password);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					userId = rs.getString("username");
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			errorMessage = "Login is unavailable right now, please try again later";
		}

		ModelAndView mv;
		if (!userId.isEmpty())
		{
			mv = new ModelAndView("user");
			mv.addObject("username", HtmlUtils.htmlEscape(userId));
		}
		else
		{

			mv = new ModelAndView("login");
			mv.addObject("errorMessage", errorMessage);
		}

		return mv;
	}



	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView registerform()
	{
		ModelAndView mv=new ModelAndView("login");

		return mv;
	}

}
