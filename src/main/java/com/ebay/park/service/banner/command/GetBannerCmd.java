package com.ebay.park.service.banner.command;

import com.ebay.park.db.dao.BannerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.banner.dto.BannerPriority;
import com.ebay.park.service.banner.dto.BannerRequest;
import com.ebay.park.service.banner.dto.SmallBanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class GetBannerCmd implements ServiceCommand<BannerRequest, SmallBanner>{

	@Autowired
	private BannerDao bannerDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public SmallBanner execute(BannerRequest param) throws ServiceException {
		
		User user = userDao.findByToken(param.getToken());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
		
		com.ebay.park.db.entity.Banner banner =  getSystemBanner(user);
				
		if (banner == null){
			banner = getUserBasedBanner(user);
		}
		
		if(banner == null){
			banner = getMessageBanner(user);
		}
		
		if (banner == null){
			throw createServiceException(ServiceExceptionCode.NO_BANNER_TO_SHOW);
		} 
		
		return new SmallBanner(banner);		
		
	}

	private com.ebay.park.db.entity.Banner getSystemBanner(User user) {
		List<com.ebay.park.db.entity.Banner> banners = 
				bannerDao.findByPriorityAndIdiom(BannerPriority.SYSTEM, user.getIdiom());
		if (banners != null && banners.size() > 0){
			return banners.get(0);
		}
		return null;
	}
	
	private com.ebay.park.db.entity.Banner getUserBasedBanner(User user) {
		if ( !user.isEmailVerified() ){
			List<com.ebay.park.db.entity.Banner> banners = 
					bannerDao.findByPriorityAndIdiom(BannerPriority.USER_BASED, user.getIdiom());
			if (banners != null && banners.size() > 0){
				return banners.get(0);
			}
		}
		return null;
	}
	
	private com.ebay.park.db.entity.Banner getMessageBanner(User user) {

		List<com.ebay.park.db.entity.Banner> banners = 
				bannerDao.findByPriorityAndIdiom(BannerPriority.GENERAL_MESSAGES, user.getIdiom());
		if (banners != null && banners.size() > 0){
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(banners.size());
			return banners.get(index);
		}
		return null;
	}

}
