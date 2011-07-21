/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.springframework.security;

import java.util.Arrays;
import java.util.Collection;

import org.opennms.test.ThrowableAnticipator;
import org.springframework.security.core.Authentication;
//import org.springframework.security.BadCredentialsException;
//import org.springframework.security.GrantedAuthority;
//import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
//import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;


public class AuthenticationIntegrationTest extends AbstractDependencyInjectionSpringContextTests {
	private DaoAuthenticationProvider m_provider; 

	@Override
	protected String[] getConfigLocations() {
		return new String[] {
                "org/opennms/web/springframework/security/applicationContext-authenticationIntegrationTest.xml"
        		};
	}
	
	public void setDaoAuthenticationProvider(DaoAuthenticationProvider provider) {
		m_provider = provider;
	}
	public DaoAuthenticationProvider getDaoAuthenticationProvider() {
		return m_provider;
	}
	
	public void testAuthenticateAdmin() {
	    Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "admin");
		Authentication authenticated = m_provider.authenticate(authentication);
		assertNotNull("authenticated Authentication object not null", authenticated);
		Collection<GrantedAuthority> authorities = authenticated.getAuthorities();
		assertNotNull("GrantedAuthorities should not be null", authorities);
		assertEquals("GrantedAuthorities size", 2, authorities.size());
		
		boolean foundUser = false;
		boolean foundAdmin = false;
		
		for (GrantedAuthority authority : authorities) {
			if ("ROLE_USER".equals(authority.getAuthority())) {
				foundUser = true;
			} else if ("ROLE_ADMIN".equals(authority.getAuthority())) {
				foundAdmin = true;
			} else { 
				fail("Found an authority other than ROLE_USER or ROLE_ADMIN: '" + authority.getAuthority() + "'");
			}
		}
		
		if (!foundUser) {
			fail("Did not find ROLE_USER authority");
		}
		
		if (!foundAdmin) {
			fail("Did not find ROLE_ADMIN authority");
		}
	}
	
	public void testAuthenticateRtc() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("rtc", "rtc");
		Authentication authenticated = m_provider.authenticate(authentication);
		assertNotNull("authenticated Authentication object not null", authenticated);
		Collection<GrantedAuthority> authorities = authenticated.getAuthorities();
		assertNotNull("GrantedAuthorities should not be null", authorities);
		assertEquals("GrantedAuthorities size", 1, authorities.size());
		assertEquals("GrantedAuthorities one name", "ROLE_RTC", authorities.iterator().next().getAuthority());
	}
	
	public void testAuthenticateTempUser() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("tempuser", "mike");
		Authentication authenticated = m_provider.authenticate(authentication);
		assertNotNull("authenticated Authentication object not null", authenticated);
		Collection<GrantedAuthority> authorities = authenticated.getAuthorities();
		assertNotNull("GrantedAuthorities should not be null", authorities);
		assertEquals("GrantedAuthorities size", 1, authorities.size());
		assertEquals("GrantedAuthorities zero role", "ROLE_USER", authorities.iterator().next().getAuthority());
	}
	
	public void testAuthenticateBadUsername() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("badUsername", "admin");
		
		ThrowableAnticipator ta = new ThrowableAnticipator();
		ta.anticipate(new BadCredentialsException("Bad credentials"));
		try {
			m_provider.authenticate(authentication);
		} catch (Throwable t) {
			ta.throwableReceived(t);
		}
		ta.verifyAnticipated();
	}
	
	public void testAuthenticateBadPassword() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "badPassword");

		ThrowableAnticipator ta = new ThrowableAnticipator();
		ta.anticipate(new BadCredentialsException("Bad credentials"));
		try {
			m_provider.authenticate(authentication);
		} catch (Throwable t) {
			ta.throwableReceived(t);
		}
		ta.verifyAnticipated();
	}
}
