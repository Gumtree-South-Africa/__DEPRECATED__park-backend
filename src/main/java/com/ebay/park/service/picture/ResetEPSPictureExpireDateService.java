package com.ebay.park.service.picture;

import com.ebay.park.eps.EPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gabriel.sideri
 */
@Service
public class ResetEPSPictureExpireDateService {

	@Autowired
	private EPSClient epsClient;
	
	/**
	 * Resets the expire date in the eBay Service Picture Service for a picture
	 * url.
	 *
	 * @param url to update
	 */
	@Async
	public void resetEPSExpireDate(String url) {
		epsClient.update(url);
	}
	
	/**
	 * Resets the expire date in the eBay Service Picture Service for a list of
	 * picture's urls.
	 * 
	 * @param urls to update
	 *
	 */
	@Async
	public void resetEPSExpireDate(List<String> urls) {
		for (String url : urls){
			epsClient.update(url);
		}
	}	
}