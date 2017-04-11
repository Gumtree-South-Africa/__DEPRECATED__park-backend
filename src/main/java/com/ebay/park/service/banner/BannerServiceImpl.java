package com.ebay.park.service.banner;

import com.ebay.park.service.banner.command.GetBannerCmd;
import com.ebay.park.service.banner.dto.BannerRequest;
import com.ebay.park.service.banner.dto.SmallBanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	private GetBannerCmd getBannerCmd;
	
	@Override
	public SmallBanner getBanner(BannerRequest request) {
		return getBannerCmd.execute(request);
	}

}
