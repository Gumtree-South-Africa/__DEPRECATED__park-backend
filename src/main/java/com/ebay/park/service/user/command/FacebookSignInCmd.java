/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;

/**
 * Functional interface to facebook sign in.
 * 
 * @author jpizarro
 * 
 */
public interface FacebookSignInCmd extends ServiceCommand<SignInRequest, SignInResponse> {

}
