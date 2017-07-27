/**
 * 
 */
package com.sundaram.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sundaram
 *
 */
public class FBEntity {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	public FBEntity() {
		
	}
	
	public FBEntity(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public FBEntity setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public FBEntity setName(String name) {
		this.name = name;
		return this;
	}

}