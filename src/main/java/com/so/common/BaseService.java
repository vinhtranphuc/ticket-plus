package com.so.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.so.security.UserPrincipal;

public class BaseService {
	
	@Value("${server.port}")
	protected String severPost;
	
	public static UserPrincipal getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if (principal instanceof UserDetails)
	    	return ((UserPrincipal) principal);
	    return null;
	}
}
