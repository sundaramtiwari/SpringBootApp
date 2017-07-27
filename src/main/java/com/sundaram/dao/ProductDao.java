/**
 * 
 */
package com.sundaram.dao;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sundaram.entity.Product;

/**
 * @author sundaram
 *
 */
@Component
public class ProductDao {

	private static final Logger LOGGER = Logger.getLogger(ProductDao.class);

	public Product findProductById(Integer productId) {
		Product product = new Product();

		try {
			Statement stmt = PostgressConnection.getStatement();
			String query = "SELECT * FROM product where id = '" + productId + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
			}

		} catch (Exception e) {
			LOGGER.error("Error occured in fetching product details by Id.", e);
		}
		return product;
	}

	public String save(Product product) {
		try {
			Statement stmt = PostgressConnection.getStatement();
			StringBuffer sbr = new StringBuffer("INSERT INTO PRODUCT VALUES (").append(product.getId()).append(", '")
					.append(product.getName()).append("')");
			String query = sbr.toString();
			LOGGER.info(query);
			stmt.execute(query);

			return "Saved product with id: " + product.getId();

		} catch (Exception e) {
			LOGGER.error("Error occured in saving product details by Id.", e);
			return e.getMessage();
		}
	}

	public String update(Product product) {
		try {
			Statement stmt = PostgressConnection.getStatement();
			StringBuffer sbr = new StringBuffer("UPDATE PRODUCT SET name = '").append(product.getName())
					.append("' WHERE id = '").append(product.getId()).append("'");
			String query = sbr.toString();
			LOGGER.info(query);
			stmt.execute(query);

			return "Updated product with id: " + product.getId();

		} catch (Exception e) {
			LOGGER.error("Error occured in updating product details by Id.", e);
			return e.getMessage();
		}
	}
}
