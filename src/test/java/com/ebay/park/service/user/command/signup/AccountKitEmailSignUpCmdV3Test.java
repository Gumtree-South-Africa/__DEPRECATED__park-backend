package com.ebay.park.service.user.command.signup;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.session.UserSessionHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit Test for {@link AccountKitEmailSignUpCmdV3}
 */
public class AccountKitEmailSignUpCmdV3Test {
    private static final String EMAIL = "email";
    private static final String TOKEN = "12345678-1234-1234-1234-123456789ab";

    @InjectMocks
    @Spy
    private AccountKitEmailSignUpCmdV3 cmd;

    @Mock
    private SignUpCommand signUpCmd;

    @Mock
    private UserServiceHelper userServiceHelper;

    @Mock
    private UserSessionHelper userSessionHelper;

    @Mock
    private EmailVerificationUtil emailVerificationUtil;

    @Mock
    private AccountKitEmailSignUpRequest request;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestWhenExecutingThenException() {
        cmd.execute(null);
    }

//    @Test
//    public void givenNullUsernameWhenExecutingThenGenerateUsername() {
//        User user = new User();
//        when(request.getEmail()).thenReturn(EMAIL);
//        when(signUpCmd.execute(request)).thenReturn(user);
//        when(userSessionHelper.createSession(user, request)).thenReturn(TOKEN);
//
//        SignUpResponse response = cmd.execute(request);
//
//        verify(userServiceHelper).createUsernameByEmail(EMAIL);
//        assertThat(response.getUsername(), is(notNull()));
//    }
}