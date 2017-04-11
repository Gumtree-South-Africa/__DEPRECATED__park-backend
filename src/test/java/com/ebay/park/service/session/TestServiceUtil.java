package com.ebay.park.service.session;

import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import org.joda.time.DateTime;

import static org.mockito.Mockito.mock;

public class TestServiceUtil {

	public static User createUserMock(Long id, String username, String email,
			byte[] encryptedPassword, Double latitude, Double longitud, String token) {

		User user = mock(User.class);
		user.setId(id);

		user.setUsername(username);
		user.setEmail(email);
		// encrypt password
		user.setPassword(encryptedPassword);
		user.setLatitude(latitude);
		user.setLongitude(longitud);
//		user.signIn(token);

		user.setCreation(new java.sql.Date(DateTime.now().toDate().getTime()));

		return user;
	}

	public static Social createSocial(long id, String description) {
		Social social = new Social();
		social.setSocialId(id);
		social.setDescription(description);
		return social;
	}

}
