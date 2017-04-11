package com.ebay.park.service.session;

import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.transaction.annotation.Transactional;


/**
 * Access to the information regarding the session.
 * 
 * @author lucia.masola
 * 
 */
public interface SessionService {


  /**
   * Returns the user session for the given session token. If the user session is store
   * in the cache for the given <code>sessionToken</code>, it will return the
   * stored value, if it is not, it will throw an exception. 
   * @param sessionToken 
   *            a single String representing the session token, and used as key in
   *            the cache. 
   * @return a user session object
   * @throws ServiceException
   *            when the token is not stored in the cache.
   */
	public UserSessionCache getUserSession(String sessionToken) throws ServiceException;

  /**
   * It stores in the cache the returned value by the method as follows:
   * {@code sessionToken} -> {@code userSession}
   * 
   * @param sessionToken
   *            a single String representing the session token, and used as key in
   *            the cache.
   * @param userSessionCache
   *            a single UserSessionCache to be stored in the cache.
   * @return the userSessionCache (since @CachePut stores the returned value by
   *         the method in the cache, we return the value being passes as
   *         parameter in order to store it.)
   */
	public UserSessionCache saveUserSession(String sessionToken, UserSessionCache userSessionCache);
	
  /**
   * It creates and stores in the cache the returned value by the method as follows:
   * <code>user.sessionToken</code> -> <code>userSessionCache</code>
   *
   * @param userSession
   *            a UserSession to be stored in the cache.
   * @return the userSessionCache (since @CachePut stores the returned value by
   *         the method in the cache, we return the value being passes as
   *         parameter in order to store it.)
   */
  	@Transactional
	public UserSessionCache createUserSessionCache(UserSession userSession);


	public UserSessionCache createUserSessionCache(UserAdmin user, String token);
	
	/**
	 * Removes from the cache the object stored with key <code>sessionToken</code>
	 * @param sessionToken 
	 *  			a single String representing the session token, and used as key in
     *            	the cache.
	 */
	public void removeUserSession(String sessionToken);

}
