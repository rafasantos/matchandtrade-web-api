package com.matchandtrade.authentication;

/**
 * Immutable POJO to hold authentication values.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class AuthenticationResponsePojo {

	private Integer userId;
	private Boolean isNewUser;
	private String email;
	private String name;
	private String authorizationToken;

	public AuthenticationResponsePojo(
			Integer userId,
			Boolean isNewUser,
			String email,
			String name,
			String authorizationToken) {
		this.userId = userId;
		this.isNewUser = isNewUser;
		this.email = email;
		this.name = name;
		this.authorizationToken = authorizationToken;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public Integer getUserId() {
		return userId;
	}

	public Boolean isNewUser() {
		return isNewUser;
	}

}
