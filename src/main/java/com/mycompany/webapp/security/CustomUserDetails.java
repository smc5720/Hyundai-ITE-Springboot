package com.mycompany.webapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
	public String useremail;

	public CustomUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities,
			String useremail) {
		super(username, password, enabled, true, true, true, authorities);
		this.useremail = useremail;
	}
	
	public String getUserEmail() {
		return this.useremail;
	}
}
