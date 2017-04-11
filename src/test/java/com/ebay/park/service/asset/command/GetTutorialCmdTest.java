package com.ebay.park.service.asset.command;

import com.ebay.park.db.dao.TutorialDao;
import com.ebay.park.db.entity.Tutorial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.asset.dto.GetTutorialRequest;
import com.ebay.park.service.asset.dto.GetTutorialResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetTutorialCmdTest {

	private static final String LANG = "en";
	private static final Integer TUTORIAL_STEP = 1;

	@Spy
	@InjectMocks
	private GetTutorialCmd cmd = new GetTutorialCmd();

	@Mock
	private SessionService sessionService;

	@Mock
	private TutorialDao tutorialDao;

	@Mock
	private GetTutorialRequest request;

	@Mock
	private UserSessionCache userSession;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getToken()).thenReturn("token");
		when(request.getStep()).thenReturn(TUTORIAL_STEP);
		when(userSession.getLang()).thenReturn(LANG);

		when(sessionService.getUserSession("token")).thenReturn(userSession);
	}

	@Test
	public void testExecuteShouldSucceed() {

		Tutorial tutorial = mock(Tutorial.class);
		when(tutorial.getStep()).thenReturn(TUTORIAL_STEP);

		when(tutorialDao.findByStepAndLang(TUTORIAL_STEP, LANG)).thenReturn(
				tutorial);

		GetTutorialResponse result = cmd.execute(request);

		assertEquals(TUTORIAL_STEP, result.getStep());

		verify(request).getToken();
		verify(request).getStep();
		verify(sessionService).getUserSession("token");
		verify(userSession).getLang();
		verify(tutorialDao).findByStepAndLang(TUTORIAL_STEP, LANG);

	}

	@Test
	public void testShouldFailOnInvalidStep() {
		when(tutorialDao.findByStepAndLang(TUTORIAL_STEP, LANG)).thenReturn(
				null);
		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.TUTORIAL_STEP_NOT_FOUND.getCode(), e.getCode());
		}
	}

}
