package com.sundaram.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenericDao<T> {

	private Class<T> clazz;
	
	private static final int maxCount = 40;
	
	public GenericDao( Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<T> getEntityClass(){
		return this.clazz;
	}

	public T findById( String id){
		try {
			Statement stmt = PostgressConnection.getStatement();
			String className = clazz.getName().toLowerCase();
			StringBuffer sbf = new StringBuffer("SELECT * FROM ")
					.append(className)
					.append(" WHERE id=")
					.append(id);
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

			T t = clazz.newInstance();

			Field[] fields = clazz.getDeclaredFields();
			Field field = fields[0];


		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
