package com.ebay.park.service.banner.command;

import com.ebay.park.db.dao.BannerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Banner;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.banner.dto.BannerPriority;
import com.ebay.park.service.banner.dto.BannerRequest;
import com.ebay.park.service.banner.dto.SmallBanner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetBannerCmdTest {

	private static final String TOKEN = "token";
	private static final String MESSAGE = "messageToDisplay";

	@InjectMocks
	private GetBannerCmd getBannerCmd;
	
	@Mock
	private BannerRequest request;
	@Mock
	private BannerDao bannerDao;
	@Mock
	private UserDao userDao;
	@Mock
	private User user;
	
	@Before
	public void setUp(){
		getBannerCmd = new GetBannerCmd();
		initMocks(this);
	}
	
	@Test
	public void GetGeneralBannertest(){
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		Idiom idiom = Mockito.mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom)).thenReturn(null);
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.USER_BASED, idiom)).thenReturn(null);
		@SuppressWarnings("unchecked")
		List<Banner> banners = Mockito.mock(List.class);
		Banner expectedBanner = Mockito.mock(Banner.class);
		when(expectedBanner.getMessage()).thenReturn(MESSAGE);
		when(banners.size()).thenReturn(2);
		when(banners.get(Mockito.isA(Integer.class))).thenReturn(expectedBanner);
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.GENERAL_MESSAGES, idiom)).thenReturn(banners);

		
		SmallBanner smallBanner = getBannerCmd.execute(request);
		
		assertNotNull(smallBanner);
		assertEquals(MESSAGE, smallBanner.getMessage());
		
		verify(request).getToken();
		verify(userDao).findByToken(TOKEN);
		verify(user, Mockito.times(3)).getIdiom();
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom);
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.USER_BASED, idiom);
		verify(banners, Mockito.times(2)).size();
		verify(banners).get(Mockito.isA(Integer.class));
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.GENERAL_MESSAGES, idiom);
		verify(expectedBanner).getMessage();
		
	}
	
	@Test
	public void GetSystemBannertest(){
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		Idiom idiom = Mockito.mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);

		@SuppressWarnings("unchecked")
		List<Banner> banners = Mockito.mock(List.class);
		Banner expectedBanner = Mockito.mock(Banner.class);
		when(expectedBanner.getMessage()).thenReturn(MESSAGE);
		when(banners.size()).thenReturn(2);
		when(banners.get(Mockito.isA(Integer.class))).thenReturn(expectedBanner);
		
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom)).thenReturn(banners);
		
		SmallBanner smallBanner = getBannerCmd.execute(request);
		
		assertNotNull(smallBanner);
		assertEquals(MESSAGE, smallBanner.getMessage());
		
		verify(request).getToken();
		verify(userDao).findByToken(TOKEN);
		verify(user).getIdiom();
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom);
		verify(banners).size();
		verify(banners).get(Mockito.isA(Integer.class));

		verify(expectedBanner).getMessage();
		
	}
	
	@Test
	public void GetUserBasedBannertest(){
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		Idiom idiom = Mockito.mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom)).thenReturn(null);
		
		@SuppressWarnings("unchecked")
		List<Banner> banners = Mockito.mock(List.class);
		Banner expectedBanner = Mockito.mock(Banner.class);
		when(expectedBanner.getMessage()).thenReturn(MESSAGE);
		when(banners.size()).thenReturn(2);
		when(banners.get(Mockito.isA(Integer.class))).thenReturn(expectedBanner);
		
		when(bannerDao.findByPriorityAndIdiom(BannerPriority.USER_BASED, idiom)).thenReturn(banners);
		
		SmallBanner smallBanner = getBannerCmd.execute(request);
		
		assertNotNull(smallBanner);
		assertEquals(MESSAGE, smallBanner.getMessage());
		
		verify(request).getToken();
		verify(userDao).findByToken(TOKEN);
		verify(user, Mockito.times(2)).getIdiom();
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.SYSTEM, idiom);
		verify(bannerDao).findByPriorityAndIdiom(BannerPriority.USER_BASED, idiom);
		verify(banners).size();
		verify(banners).get(Mockito.isA(Integer.class));
		verify(expectedBanner).getMessage();
		
	}
	
}
