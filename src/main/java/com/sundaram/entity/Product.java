/**
 * 
 */
package com.sundaram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sundaram
 *
 */
public class Product {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("current_price")
	private CurrentPrice currentPrice;

	public Integer getId() {
		return id;
	}

	public Product setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Product setName(String name) {
		this.name = name;
		return this;
	}

	public CurrentPrice getCurrentPrice() {
		return currentPrice;
	}

	public Product setCurrentPrice(CurrentPrice currentPrice) {
		this.currentPrice = currentPrice;
		return this;
	}

	public class CurrentPrice {

		@JsonIgnore
		private Integer productId;

		@JsonProperty("value")
		private Double value;

		@JsonProperty("currency_code")
		private CurrencyCode currencyCode;

		public Double getValue() {
			return value;
		}

		public CurrentPrice setValue(Double value) {
			this.value = value;
			return this;
		}

		public CurrencyCode getCurrencyCode() {
			return currencyCode;
		}

		public CurrentPrice setCurrencyCode(CurrencyCode currencyCode) {
			this.currencyCode = currencyCode;
			return this;
		}

	}
	
	public enum CurrencyCode {
		USD, EU, INR, POUND;
	}
}