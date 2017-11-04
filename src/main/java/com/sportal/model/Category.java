package com.sportal.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	private long categoryId;
	@NotNull
	@Size(min = 3, max = 45)
	private String name;

	public Category(long category_id, String name) {
		this(name);
		this.categoryId = category_id;
	}

	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long category_id) {
		this.categoryId = category_id;
	}

}
