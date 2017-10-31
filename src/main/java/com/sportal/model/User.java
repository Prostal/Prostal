package com.sportal.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class User{

	
	public static final String AVATAR_URL = "C:\\uploads\\";
	private long id;
	
	@NotNull
    @Size(min=2, max=45)
	private String username;
	@NotNull
	@Size(min=2, max=200)
	private String password;
	@NotNull
	@Size(min=2, max=45)
	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
	private String email;
	private int age;
	@AssertFalse
	private boolean isAdmin;
	@AssertFalse
	private boolean isBanned;
	private LocalDateTime registered;
	private String avatarUrl;

	public User(long id, String username, String password, String email, String avatarUrl, int age, LocalDateTime registered, boolean isBanned, boolean isAdmin) {
		this(username, password, email);
		this.id = id;
		this.avatarUrl = avatarUrl;
		this.age = age;
		this.registered = registered;
		this.isAdmin = isAdmin;
		this.isBanned = isBanned;
	}
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.registered = LocalDateTime.now();
		this.isAdmin = false;
		this.isBanned = false;
	}
	

	public User() {
		this.registered = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isBanned() {
		return isBanned;
	}

	public LocalDateTime getRegistered() {
		return registered;
	}

	

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		return true;
	}


	
	
}
