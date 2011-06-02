package org.opennms.web.svclayer;

public interface SecurityContextService {

	/**
	 * Get the user name about the currently logged in user
	 * 
	 * @return user name from security context otherwise null
	 */
	public String getUsername();

	/**
	 * Get the user password about the currently logged in user
	 * 
	 * @return user password from security context otherwise null
	 */
	public String getPassword();

	/**
	 * Check if the currently logged in user has the required role.
	 * 
	 * @param role
	 *            - required role
	 * @return true if role is assigned, otherwise false
	 */
	public boolean hasRole(String role);

	/**
	 * Check if the currently logged in user is authenticated.
	 * 
	 * @return true is authenticated, otherwise false
	 */
	public boolean isAuthenticated();
}
