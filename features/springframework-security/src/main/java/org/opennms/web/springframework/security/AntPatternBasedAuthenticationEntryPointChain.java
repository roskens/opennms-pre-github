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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opennms.core.utils.ThreadCategory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.util.Assert;

/**
 * PatternBasedAuthenticationEntryPointWrapper
 */
public class AntPatternBasedAuthenticationEntryPointChain implements AuthenticationEntryPoint, InitializingBean {

    private List<String> m_patterns;
    private AuthenticationEntryPoint m_matchingEntryPoint;
    private AuthenticationEntryPoint m_nonMatchingEntryPoint;
    
    private AntUrlPathMatcher m_urlPathMatcher = new AntUrlPathMatcher();
    
    
    /**
     * <p>setPatterns</p>
     *
     * @param patterns the patterns to set
     */
    public void setPatterns(List<String> patterns) {
        m_patterns = patterns;
    }

    /**
     * <p>setMatchingEntryPoint</p>
     *
     * @param matchedEntryPoint the matchedEntryPoint to set
     */
    public void setMatchingEntryPoint(AuthenticationEntryPoint matchedEntryPoint) {
        m_matchingEntryPoint = matchedEntryPoint;
    }

    /**
     * <p>setNonMatchingEntryPoint</p>
     *
     * @param unmatchedEntryPoint the unmatchedEntryPoint to set
     */
    public void setNonMatchingEntryPoint(AuthenticationEntryPoint unmatchedEntryPoint) {
        m_nonMatchingEntryPoint = unmatchedEntryPoint;
    }
    
    
    /**
     * <p>setRequiresLowerCaseUrl</p>
     *
     * @param requiresLowerCaseUrl a boolean.
     */
    public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
        m_urlPathMatcher.setRequiresLowerCaseUrl(requiresLowerCaseUrl);
    }

    /**
     * <p>afterPropertiesSet</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(m_nonMatchingEntryPoint, "nonMatchingEntryPoint may not be null");
        Assert.notNull(m_matchingEntryPoint, "matchingEntryPoint may not be null");
        Assert.notNull(m_patterns, "patterns may not be null");
    }

    /** {@inheritDoc} */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        
        String relativePath = getRelativePath(request);

        AuthenticationEntryPoint entryPoint = getAppropriateEntryPoint(relativePath);
        
        log().debug("Commencing entry to " + entryPoint + " for " + relativePath);
        
        entryPoint.commence(request, response, authException);
        
    }
    
	private AuthenticationEntryPoint getAppropriateEntryPoint(String url) {
        for (String pattern : m_patterns) {
            if (m_urlPathMatcher.pathMatchesUrl(m_urlPathMatcher.compile(pattern), url)) {
                return m_matchingEntryPoint;
            }
        }
        
        return m_nonMatchingEntryPoint;
        
    }
    
	/**
	 * Returns the relative path for the saved request that failed authorization.
	 * Strips off all but the path of the URL and then attempts to remove the context path
	 * from the beginning as well.  This assumes that the current context path is the same
	 * as the context path of the saved request.
	 * 
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
    private String getRelativePath(HttpServletRequest request) throws MalformedURLException {
    	String path = new URL(getSavedRequest(request).getRedirectUrl()).getPath();
    	if (path.startsWith(request.getContextPath())) {
        	return path.substring(request.getContextPath().length());
    	} else {
    		return path;
    	}
    }
    
    private SavedRequest getSavedRequest(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return new DefaultSavedRequest(request, new PortResolverImpl());
        } else {
            return (SavedRequest) httpSession.getAttribute(WebAttributes.SAVED_REQUEST);
        }
        
    }

    /**
     * <p>log</p>
     *
     * @return a {@link org.opennms.core.utils.ThreadCategory} object.
     */
    protected ThreadCategory log() {
        return ThreadCategory.getInstance(getClass());
    }
}
