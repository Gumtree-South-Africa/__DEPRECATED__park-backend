package com.ebay.park.util;

import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/***
 * Unit test for {@link LanguageUtil}.
 * @author Julieta Salvadó
 */
public class LanguageUtilTest {

	private static final String VALID_LANG = "en";
	private static final String INVALID_LANG = "pe";
	private static final String NULL_LANG = null;
	private static final String DEFAULT_LANG = "es";

	@Test
	public void givenNullLanguageWhenValidatingThenReturnTheSameValidLanguage(){
		String lang = LanguageUtil.getValidLanguage(NULL_LANG);
		assertThat("The language must be validated", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenAValidLanguageWhenValidatingThenReturnTheSameValidLanguage(){
		String lang = LanguageUtil.getValidLanguage(VALID_LANG);
		assertThat("The language must be validated", lang, is(VALID_LANG));
	}

	@Test
	public void givenNullLanguageWhenValidatingThenReturnDefaultLanguage(){
		String lang = LanguageUtil.getValidLanguage(NULL_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenAInvalidLanguageWhenValidatingThenReturnDefaultLanguage(){
		String lang = LanguageUtil.getValidLanguage(INVALID_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenNullUserAndValidRequestLanguageWhenAskingForUserLanguageThenReturnRequestLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, VALID_LANG);
		assertThat("The request language is retrieved", lang, is(VALID_LANG));
	}

	@Test
	public void givenNullUserAndInvalidRequestLanguageWhenAskingForUserLanguageThenReturnDefaultLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, INVALID_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenNullUserAndNullRequestLanguageWhenAskingForUserLanguageThenReturnDefaultLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, null);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenValidUserAndNullRequestLanguageWhenAskingForUserLanguageThenReturnUserLanguage() {
		User user = mock(User.class);
		Idiom idiom = mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		when(idiom.getCode()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(user, null);
		assertThat("The user language is retrieved when request langueage is not set", lang, is(VALID_LANG));
	}

	@Test
	public void givenValidUserAndValidRequestLanguageWhenAskingForUserLanguageThenReturnRequestLanguage() {
		User user = mock(User.class);
		Idiom idiom = mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		when(idiom.getCode()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(user, DEFAULT_LANG);
		assertThat("The request language is retrieved when it is defined", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenValidUserAndInvalidRequestLanguageWhenAskingForUserLanguageThenReturnDefaultLanguage() {
		User user = mock(User.class);
		Idiom idiom = mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		when(idiom.getCode()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(user, INVALID_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenNullUserAndValidRequestLanguageWhenAskingForSessionLanguageThenReturnRequestLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, VALID_LANG);
		assertThat("The request language is retrieved", lang, is(VALID_LANG));
	}

	@Test
	public void givenNullUserAndInvalidRequestLanguageWhenAskingForSessionLanguageThenReturnDefaultLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, INVALID_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenNullUserAndNullRequestLanguageWhenAskingForSessionLanguageThenReturnDefaultLanguage() {
		String lang = LanguageUtil.getLanguageForUserRequest((User) null, null);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenValidUserAndNullRequestLanguageWhenAskingForSessionLanguageThenReturnUserLanguage() {
		UserSessionCache sessionCache = mock(UserSessionCache.class);
		when(sessionCache.getLang()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(sessionCache, null);
		assertThat("The user language is retrieved when request langueage is not set", lang, is(VALID_LANG));
	}

	@Test
	public void givenValidUserAndValidRequestLanguageWhenAskingForSessionLanguageThenReturnRequestLanguage() {
		UserSessionCache sessionCache = mock(UserSessionCache.class);
		when(sessionCache.getLang()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(sessionCache, DEFAULT_LANG);
		assertThat("The request language is retrieved when it is defined", lang, is(DEFAULT_LANG));
	}

	@Test
	public void givenValidUserAndInvalidRequestLanguageWhenAskingForSessionLanguageThenReturnDefaultLanguage() {
		UserSessionCache sessionCache = mock(UserSessionCache.class);
		when(sessionCache.getLang()).thenReturn(VALID_LANG);

		String lang = LanguageUtil.getLanguageForUserRequest(sessionCache, INVALID_LANG);
		assertThat("The default language is retrieved", lang, is(DEFAULT_LANG));
	}
}
