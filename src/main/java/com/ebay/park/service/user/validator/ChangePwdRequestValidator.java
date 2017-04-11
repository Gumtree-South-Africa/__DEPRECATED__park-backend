package com.ebay.park.service.user.validator;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.PasswdUtil;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author federico.jaite
 * 
 */
@Component
public class ChangePwdRequestValidator implements
		ServiceValidator<ChangePwdRequest> {

	@Autowired
	private PasswdUtil passwdUtil;
	
	@Autowired
	private UserDao userDao;
	

	@Override
	public void validate(ChangePwdRequest toValidate) {
		String newPwd = toValidate.getNewPassword();

		try {
			Validate.notEmpty(toValidate.getCurrentPassword(),
					"toValidate.currentPassword should not be empty");
			Validate.notEmpty(newPwd,
					"toValidate.newPassword should not be empty");
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.EMPTY_DATA_CHANGE_PWD);
		}
		
		User user = userDao.findByToken(toValidate.getToken());
		
		if (!passwdUtil.equalsToHashedPassword(toValidate.getCurrentPassword(), user.getPassword())) {
			throw createServiceException(ServiceExceptionCode.INVALID_PWD);
		}
		
		DataCommonUtil.validatePassword(newPwd);

		if (!verifyOldPasswordSimilarity(toValidate.getCurrentPassword(),
				newPwd)) {
			throw createServiceException(ServiceExceptionCode.PWD_TOO_SIMILAR);
		}

	}
	
	private boolean verifyOldPasswordSimilarity(String oldPassword,
			String newPassword) {
		// can't change to a password that contains any 3 character substring of
		// old password
		if (oldPassword != null) {
			int length = oldPassword.length();
			for (int i = 0; i < length - 2; i++) {
				String sub = oldPassword.substring(i, i + 3);
				if (newPassword.indexOf(sub) > -1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
