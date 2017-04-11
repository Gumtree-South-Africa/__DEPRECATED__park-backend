package com.ebay.park.service.banner;

import com.ebay.park.service.banner.dto.BannerRequest;
import com.ebay.park.service.banner.dto.SmallBanner;

public interface BannerService {

	SmallBanner getBanner(BannerRequest request);
	
}
