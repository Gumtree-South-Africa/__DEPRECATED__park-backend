package com.ebay.park.util;

import com.ebay.park.service.ServiceExceptionCode;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Utility to access to Account Kit Graph API in order to retrieve 
 * and verify user access tokens, etc.
 * @author scalderon
 * @since 2.0.2
 * @see <a href="https://developers.facebook.com/docs/accountkit/graphapi"> https://developers.facebook.com/docs/accountkit/graphapi</a>
</a>
 *
 */

@Component
public class AccountKitUtil {
	
	/** The account kit scheme. */
	@Value("${account.kit.scheme}")
	private String accountKitScheme;
	
	/** The account kit host. */
	@Value("${account.kit.host}")
	private String accountKitHost;
	
	/** The account kit version path. */
	@Value("${account.kit.api.version.path}")
	private String accountKitVersionPath;
	
	private static Logger logger = LoggerFactory.getLogger(AccountKitUtil.class);
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Validates the user's identity by verifying their access token
	 * by accessing the Account Kit API.
	 * @param accessToken
	 * 			the user access token
	 */
	public void validateAccessTokenByPhoneNumber(String accessToken, String phoneNumber) {
		if (StringUtils.isEmpty(accessToken)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_ACCOUNT_KIT_TOKEN);
		}
		
		if (StringUtils.isEmpty(phoneNumber)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_PHONE_NUMBER);
		}
		
		URI uri = getURIValidateAccessToken(accessToken);
		if (uri != null) {
			try {
				//This returns the Account ID associated with the access token pair if it's valid
				AccountKitPhone phone = restTemplate.getForObject(uri, AccountKitPhone.class);

				if (!phoneNumber.equals(phone.getPhone().getNumber())) {
                    logger.error("Invalid account kit token: {} for phone number: {}", accessToken, phoneNumber);
					throw createServiceException(ServiceExceptionCode.INVALID_ACCOUNT_KIT_TOKEN);
				}
			
			} catch (RestClientException restClientException) {
				logger.error("Error trying to access to Account Kit API", restClientException);
				throw createServiceException(ServiceExceptionCode.ERROR_ACCOUNT_KIT_COMMUNICATION);
			}
		}
	}

	/**
     * call
     * 
     * https://graph.accountkit.com/v1.0/me/?access_token=EMAWeJw9Ah5lhYFcy3KcblQavqZCCZBclVun7V8m5CIfaR33pdFcQuvHtg56isDasCUBslzFtP88hwVZBnzbwgAoIZBz1dUn01h4K6IillNAZDZD
     * 
     * 
     * response
     * {
                   "email": {
                      "address": "nicolas.porpiglia\u0040gmail.com"
                   },
                   "id": "254164021619025"
                }
     */

    public void validateAccessTokenByEmail(String accessToken, String email) {
    	if (StringUtils.isEmpty(accessToken)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_ACCOUNT_KIT_TOKEN);
		}
		
		if (StringUtils.isEmpty(email)) {
			throw createServiceException(ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL);
		}
		
		URI uri = getURIValidateAccessToken(accessToken);
		if (uri != null) {			
			try {
				//This returns the Account ID associated with the access token pair if it's valid
				AccountKitEmail emailAccountKit = restTemplate.getForObject(uri, AccountKitEmail.class);
				if (!email.equals(emailAccountKit.getEmail().getAddress())) {
                    logger.error("Invalid account kit token: {} for email: {}", accessToken, email);
					throw createServiceException(ServiceExceptionCode.INVALID_ACCOUNT_KIT_TOKEN);
				}
			} catch (RestClientException e) {
				logger.error("Error trying to access to Account Kit API", e);
				throw createServiceException(ServiceExceptionCode.ERROR_ACCOUNT_KIT_COMMUNICATION);
			}
		}
    }
    
    private URI getURIValidateAccessToken(String accessToken) {
    	URI uri;
		try {
			uri = new URIBuilder()
					.setScheme(accountKitScheme)
					.setHost(accountKitHost)
					.setPath("/me/")
					.addParameter("access_token", accessToken)
					.build();
		} catch (URISyntaxException e) {
			logger.error("Error trying to build the Account Kit URI", e);
			throw createServiceException(ServiceExceptionCode.INVALID_ACCOUNT_KIT_URI);
		}
		return uri;
    }

}
