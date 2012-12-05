package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Required;


import org.dspace.EDMExport.bo.EDMExportBOUser;
import org.dspace.EDMExport.dao.EDMExportDAOEperson;

public class EDMExportAuthenticationManager implements AuthenticationManager
{
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	
	private EDMExportDAOEperson daoEperson;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException
	{
		logger.debug("Performing EDMExport authentication");
		
		EDMExportBOUser eperson = null;
		
		try {
			eperson = daoEperson.getEperson(auth.getName());
		} catch (Exception e) {
			logger.error("User "+ auth.getName() + " does not exists!");
			throw new BadCredentialsException("User does not exists!");
		}
		
		if (!passwordEncoder.isPasswordValid(eperson.getPassword(), (String) auth.getCredentials(), null)) {
			logger.error("Wrong password!" + eperson.getPassword() + " " + (String) auth.getCredentials());
			throw new BadCredentialsException("Wrong password!");
		}
		
		if (auth.getName().equals(auth.getCredentials())) {
			logger.debug("Entered username and password are the same!");
			throw new BadCredentialsException("Entered username and password are the same!");
		} else {
			logger.debug("User details are good and ready to go");
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), getAuthorities(eperson.getAccess()));
		}
	}
	
	
	public Collection<SimpleGrantedAuthority> getAuthorities(Integer access)
	{
		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(2);
		logger.debug("Grant ROLE_USER to this user");
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if ( access.compareTo(1) == 0) {
			logger.debug("Grant ROLE_ADMIN to this user");
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
	 
		return authList;
	}
	
	@Required
	public void setEdmExportDAOEperson(EDMExportDAOEperson daoEperson)
	{
    	this.daoEperson = daoEperson;
	}

}