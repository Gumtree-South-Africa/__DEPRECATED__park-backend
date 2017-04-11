/**
 * 
 */
package com.ebay.park.service;


/**
 * @author jppizarro
 * 
 */
public interface ServiceCommand<Z, T> {

	T execute(Z param) throws ServiceException;

}
