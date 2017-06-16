package com.sundaram.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sundaram.entity.User;

@Component
public class UserDao {

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);

	public User findUserById(String userId) {
		User user = new User();
		try {
			Statement stmt = PostgressConnection.getStatement();
			String query = "SELECT * FROM \"user\" where id = '" + userId + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				user.setId(rs.getString(1));
				user.setName(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setCreateDate(rs.getDate(5));
				user.setUpdatedDate(rs.getDate(6));
			}

		} catch (Exception e) {
			LOGGER.error("Error occured in fetching product details by Id.", e);
		}
		return user;
	}

	public List<String> getTicks() {

	    try (Connection connection = new PostgressConnection().getConnection()) {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
	      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
	      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

	      ArrayList<String> output = new ArrayList<String>();
	      while (rs.next()) {
	        output.add("Read from DB: " + rs.getTimestamp("tick"));
	      }

	      return output;
	    } catch (Exception e) {
	    	return new ArrayList<String>();
	    }
	  
	}
}
