/**
 * 
 */
package com.sundaram.dao;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sundaram.entity.FBEntity;

/**
 * @author sundaram
 *
 */
@Component
public class WebhookDao {

	private static final Logger LOGGER = Logger.getLogger(WebhookDao.class);

	public String save(FBEntity fbEntity) {
		try {
			Statement stmt = PostgressConnection.getStatement();
			StringBuffer sbr = new StringBuffer("INSERT INTO FB_ENTITY VALUES ('").append(fbEntity.getId()).append("', '")
					.append(fbEntity.getName()).append("')");
			String query = sbr.toString();
			LOGGER.info(query);
			stmt.execute(query);

			return fbEntity.getId();

		} catch (Exception e) {
			LOGGER.error("Error occured in saving fb entity details by Id.", e);
			return e.getMessage();
		}
	}
	

	public FBEntity getFBEntityById(String fbEntityId) {
		FBEntity fbEntity = new FBEntity();

		try {
			Statement stmt = PostgressConnection.getStatement();
			String query = "SELECT * FROM FB_ENTITY where id = '" + fbEntityId + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				fbEntity.setId(rs.getString(1));
				fbEntity.setName(rs.getString(2));
			}

			return fbEntity;
		} catch (Exception e) {
			LOGGER.error("Error occured in fetching fb entity details by Id.", e);
			return null;
		}
	}
	
	public void createFBEntityTable () {
		try {
			Statement stmt = PostgressConnection.getStatement();
			String query ="CREATE TABLE IF NOT EXISTS FB_ENTITY(id TEXT PRIMARY KEY NOT NULL, name TEXT)";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println(rs);
		} catch (Exception e) {
			LOGGER.error("Error occured in creating fb entity table.", e);
		}
	}
}
