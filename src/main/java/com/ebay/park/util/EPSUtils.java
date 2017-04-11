package com.ebay.park.util;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ebay.park.service.ServiceException.createServiceException;


/**
 * Utility class for CDN code, mostly used to create urls for the images uploaded.
 * @author marcos.lambolay | Julieta Salvadó
 */
@Component
public class EPSUtils {

	/**
	 * Url pattern for the assets that are going to be uploaded to the CDN
	 */
	private static final String URL_PATTERN = "(https?://[^/]+/)(.*)";
	
	@Value("${eps.user_files_prefix}")
	private String prefix = "";

	/**
	 * Default resolution for EPS pictures.
	 */
	@Value("${picture.default_size}")
	private String defaultResolution;
	
	/**
	 * Normalizes the prefix taken from the configuration files
	 */
	@PostConstruct
	private void initialize() {
		prefix = prefix.trim();
		if (!"".equals(prefix) && !prefix.endsWith("/")) {
			prefix = prefix + "/";
		}
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Given a CDN url, extracts the name of the resource that url access
	 * @param url
	 * @return a String depicting the resources name
	 *
	 * @throws ServiceException with code INVALID_URL if the url does not match URL_pattern
	 *
	 * FIXME get rid of it! if needed, use URLBuilder
	 */
	public String getResourceNameFromUrl(String url){
		Pattern pattern = Pattern.compile(URL_PATTERN);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group(2);
		}
		throw createServiceException(ServiceExceptionCode.INVALID_URL);
	}

	/**
	 * Changes the picture url in order to get the ulr in the required resolution.
	 * @param pictureUrl url to adapt
	 * @param resolution string that represents the desired resolution
	 * @return changed url
	 */
	public String getPictureResolution(String pictureUrl, String resolution) {
		return pictureUrl.replace(defaultResolution, resolution);
	}
}
