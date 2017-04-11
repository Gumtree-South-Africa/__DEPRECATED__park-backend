package com.ebay.park.service.user.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRequestValidator;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.util.FacebookUtil;
import org.junit.Ignore;
import org.springframework.test.util.ReflectionTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for {@link SignUpRequestValidator}.
 * @author lucia.masola
 */
public class SignUpRequestValidatorTest {

    private static final String VALID_USERNAME = "validUser";
    private static final String INVALID_USERNAME = "invalid.User";
    private static final String VALID_PASSWORD = "Pa$$w0rd";
    private static final String INVALID_PASSWORD = "Pa$$w0rdPa$$w0rdPa$$w0rdPa$$w0rdPa$$w0rdPa$$w0rd";
    private static final String VALID_EMAIL = "blabla@google.com";
    private static final String VALID_LOCATION_1 = "33.234232,-23.342342";
    private static final String INVALID_LOCATION_1 = "invalidLocation";
    private static final String INVALID_LOCATION_2 = "333.234232,-23.342342";
    private static final String INVALID_LOCATION_3 = "-33.234232a,-23.342342";
    private static final String INVALID_LOCATION_4 = "33.234232,-2333.342342";
    private static final String FAIL_MESSAGE = "An exception was expected. ";
    private static final String VALID_FACEBOOK_ID = "validFaceId";
    private static final String DUP_FACEBOOK_ID = "duplicatedFaceId";
    private static final String VALID_FACEBOOK_TOKEN = "validFaceToken";
    private static final String VALID_PHOTO_ID = "photoID";
    private static final String VALID_LOCATION_NAME = "CITY NAME, STATE";
    private static final String VALID_BIRTHDAY = "2001-11-11";
    private static final String VALID_GENDER = "M";
    private static final String VALID_ZIP_CODE = "90210";
    private static final String INVALID_USERNAME1 = "user_name";
    private static final String INVALID_USERNAME2 = "user-name";
    private static final String INVALID_USERNAME3 = "us";
    private static final String INVALID_USERNAME4 = "usernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusername";
    private static final String ANOTHER_FACEBOOK_ID = "anotherid";
    private static final String ANOTHER_EMAIL_ADDRESS = "blabla2@google.com";
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 15;

    @InjectMocks
    private ServiceValidator<SignUpRequest> validator;

    @Mock
    private FacebookUtil facebookService;

    @Mock
    private DeviceRequestValidator deviceReqValidator;

    @Mock
    private UserSocialDao userSocialDao;

    @Before
    public void setUp() {
        validator = new SignUpRequestValidator();
        initMocks(this);
        ReflectionTestUtils.setField(validator, "usernameMinLength", USERNAME_MIN_LENGTH);
        ReflectionTestUtils.setField(validator, "usernameMaxLength", USERNAME_MAX_LENGTH);
        Mockito.when(
                userSocialDao
                        .findBySocialUserIdAndNetwork(Social.FACEBOOK, VALID_FACEBOOK_ID))
                .thenReturn(null);
        UserSocial userSoc = new UserSocial();
        List<UserSocial> userSocialArray = new ArrayList<UserSocial>();
        userSocialArray.add(userSoc);
        Mockito.when(
                userSocialDao
                        .findBySocialUserIdAndNetwork(DUP_FACEBOOK_ID, Social.FACEBOOK))
                .thenReturn(userSocialArray);
    }

    @Test
    public void valideFieldWithFacebookInformation() {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME,
                VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1,
                VALID_FACEBOOK_ID, VALID_FACEBOOK_TOKEN, VALID_PHOTO_ID,
                VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY, null,
                VALID_ZIP_CODE);
        doNothing().when(deviceReqValidator).validate(request.getDevice());
        doNothing().when(facebookService).tokenIsValid(VALID_FACEBOOK_TOKEN, VALID_FACEBOOK_ID);
        when(facebookService.getUserIdAssociatedWithToken(VALID_FACEBOOK_TOKEN))
                .thenReturn(VALID_FACEBOOK_ID);

        when(facebookService.getEmail(VALID_FACEBOOK_TOKEN)).thenReturn(VALID_EMAIL);

        validator.validate(request);

        verify(deviceReqValidator, Mockito.times(1)).validate(request.getDevice());
        verify(facebookService, Mockito.times(1)).tokenIsValid(VALID_FACEBOOK_TOKEN, VALID_FACEBOOK_ID);
        verify(facebookService, Mockito.times(1)).getUserIdAssociatedWithToken(VALID_FACEBOOK_TOKEN);
    }

    @Test
    public void givenNullUsernameWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(null, VALID_PASSWORD,
                    VALID_EMAIL, VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_USERNAME.getCode());
        }
    }

    @Test
    public void givenEmptyUsernameWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest("", VALID_PASSWORD,
                    VALID_EMAIL, VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_USERNAME.getCode());
        }
    }

    @Test
    public void givenEmptyPasswordWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME, "",
                    VALID_EMAIL, VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_PASSWORD.getCode());
        }
    }

    @Test
    public void givenNullPasswordWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME, null,
                    VALID_EMAIL, VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_PASSWORD.getCode());
        }
    }

    @Test
    public void givenNullEmailWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, null, VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {

            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_EMAIL.getCode());
        }
    }

    @Test
    public void givenEmptyEmailWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, "", VALID_LOCATION_1, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_EMAIL.getCode());
        }
    }

    @Test
    public void nullOrEmptyLocationTest() {

        try {

            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, null, null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {

            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_LOCATION.getCode());

        }

        try {

            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, "", null, null, null,
                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {

            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_LOCATION.getCode());

        }
    }

    @Test
    public void givenInvalidLocationWhenValidatingThenException() {

        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, INVALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_LOCATION.getCode());
        }

        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, INVALID_LOCATION_2, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_LOCATION.getCode());
        }

        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, INVALID_LOCATION_3, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_LOCATION.getCode());
        }

        try {

            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, INVALID_LOCATION_4, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {

            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_LOCATION.getCode());
        }
    }

    @Test
    public void givenInvalidFacebookIdWhenValidatingThenException() {
        when(facebookService.getUserIdAssociatedWithToken(VALID_FACEBOOK_TOKEN))
                .thenReturn(ANOTHER_FACEBOOK_ID);
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1,
                    VALID_FACEBOOK_ID, VALID_FACEBOOK_TOKEN, null, VALID_LOCATION_NAME,
                    VALID_GENDER, VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_FACEBOOK_ID.getCode());
        }
    }

    @Test
    public void givenInvalidFacebookEmailWhenValidatingThenException() {
        when(facebookService.getUserIdAssociatedWithToken(VALID_FACEBOOK_TOKEN))
                .thenReturn(VALID_FACEBOOK_ID);
        when(facebookService.getEmail(VALID_FACEBOOK_TOKEN)).thenReturn(ANOTHER_EMAIL_ADDRESS);
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1,
                    VALID_FACEBOOK_ID, VALID_FACEBOOK_TOKEN, null, VALID_LOCATION_NAME,
                    VALID_GENDER, VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_FACEBOOK_EMAIL.getCode());
        }
    }


    @Test
    public void givenNullFacebookTokenWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1,
                    VALID_FACEBOOK_ID, null, null, VALID_LOCATION_NAME,
                    VALID_GENDER, VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_FACEBOOK_INFO.getCode());
        }
    }

    @Test
    public void givenNullFacebookIdWhenValidatingThenException() {
        try {

            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    VALID_FACEBOOK_TOKEN, null, VALID_LOCATION_NAME,
                    VALID_GENDER, VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);

        } catch (ServiceException re) {

            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_FACEBOOK_INFO.getCode());
        }
    }

    @Test
    public void givenDuplicateFacebookAccountWhenValidatingThenException() {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME,
                VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1,
                DUP_FACEBOOK_ID, VALID_FACEBOOK_TOKEN, VALID_PHOTO_ID,
                VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY, null,
                VALID_ZIP_CODE);
        Mockito.doNothing().when(deviceReqValidator)
                .validate(request.getDevice());
        Mockito.doNothing().when(facebookService)
                .tokenIsValid(VALID_FACEBOOK_TOKEN, DUP_FACEBOOK_ID);
        Mockito.when(facebookService
                .getUserIdAssociatedWithToken(VALID_FACEBOOK_TOKEN))
                .thenReturn(DUP_FACEBOOK_ID);

        try {
            validator.validate(request);
            fail(FAIL_MESSAGE);
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_FB.getCode());
            return;
        }
    }

    @Test
    public void givenNullZipCodeWhenValidationThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null, null, null,

                    VALID_LOCATION_NAME, VALID_GENDER, VALID_BIRTHDAY,
                    null, null));
            fail(FAIL_MESSAGE);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_ZIP_CODE.getCode(), e.getCode());
        }
    }

    @Test
    public void givenInvalidPasswordWhenValidationThenException() {

        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    INVALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.PWD_TOO_LONG.getCode());
        }
    }

    @Test
    public void givenUsernameWithUnderscoreWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(INVALID_USERNAME1,
                    INVALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USERNAME_PATTERN.getCode());
        }
    }

    @Test
    public void givenUsernameWithHyphenWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(INVALID_USERNAME2,
                    INVALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USERNAME_PATTERN.getCode());
        }
    }

    @Test
    public void givenTooShortUsernameWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(INVALID_USERNAME3,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USERNAME_LONG.getCode());
        }
    }

    @Test
    public void givenTooLongUsernameWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(INVALID_USERNAME4,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USERNAME_LONG.getCode());
        }
    }

    @Test
    public void givenEmptyLocationWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(VALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, null, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_LOCATION_NAME.getCode());
        }
    }

    @Test
    public void givenNullRequestWhenValidatingThenException() {
        try {
            validator.validate(null);
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_SIGNUP_REQ.getCode());
        }
    }

    @Test
    public void givenInvalidUsernameWhenValidatingThenException() {
        try {
            validator.validate(new SignUpRequest(INVALID_USERNAME,
                    VALID_PASSWORD, VALID_EMAIL, VALID_LOCATION_1, null,
                    null, null, VALID_LOCATION_NAME, VALID_GENDER,
                    VALID_BIRTHDAY, null, VALID_ZIP_CODE));
            fail(FAIL_MESSAGE);
        } catch (ServiceException re) {
            assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USERNAME_PATTERN.getCode());
        }
    }
}