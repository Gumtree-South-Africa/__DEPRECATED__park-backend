package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.context.ApplicationContext;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.rejection.RejectAnimalsItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectCommissionItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectDoNotSendItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectDuplicatedItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectForbiddenItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectMakeupItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectPicturesItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectPriceItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectServicesItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectStyleItemExecutor;

public class RejectItemCmdImplTest {
	
	private static final long ITEM_ID = 127l;
	private static final int REASON0 = 0;
	private static final int REASON1 = 1;
	private static final int REASON2 = 2;
	private static final int REASON3 = 3;
	private static final int REASON4 = 4;
	private static final int REASON5 = 5;
	private static final int REASON6 = 6;
	private static final int REASON7 = 7;
	private static final int REASON8 = 8;
	private static final int REASON9 = 9;

	@Spy
	@InjectMocks
	private RejectItemCmdImpl cmd;

	@Mock
	ApplicationContext ctx;

	@Mock
    RejectDoNotSendItemExecutor executor0;

	@Mock
	RejectDuplicatedItemExecutor executor1;

	@Mock
	RejectPicturesItemExecutor executor2;

	@Mock
	RejectServicesItemExecutor executor3;

	@Mock
	RejectMakeupItemExecutor executor4;

	@Mock
	RejectAnimalsItemExecutor executor5;

	@Mock
	RejectCommissionItemExecutor executor6;

	@Mock
	RejectStyleItemExecutor executor7;

	@Mock
	RejectPriceItemExecutor executor8;

	@Mock
    RejectForbiddenItemExecutor executor9;
	
	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
    public void testSucceededReason0() {
        RejectItemRequest request0 = new RejectItemRequest(ITEM_ID, REASON0);
        Mockito.when(ctx.getBean(RejectDoNotSendItemExecutor.class)).thenReturn(executor0);

        ServiceResponse response0 = cmd.execute(request0);

        verify(executor0).execute(request0);
        assertEquals(ServiceResponse.SUCCESS, response0);
    }

	@Test
	public void testSucceededReason1() {
		RejectItemRequest request1 = new RejectItemRequest(ITEM_ID, REASON1);
		Mockito.when(ctx.getBean(RejectDuplicatedItemExecutor.class)).thenReturn(executor1);

		ServiceResponse response1 = cmd.execute(request1);

		verify(executor1).execute(request1);
		assertEquals(ServiceResponse.SUCCESS, response1);
	}

	@Test
	public void testSucceededReason2() {
		RejectItemRequest request2 = new RejectItemRequest(ITEM_ID, REASON2);
		Mockito.when(ctx.getBean(RejectPicturesItemExecutor.class)).thenReturn(executor2);

		ServiceResponse response2 = cmd.execute(request2);
	
		verify(executor2).execute(request2);
		assertEquals(ServiceResponse.SUCCESS, response2);
	}

	@Test
	public void testSucceededReason3() {
		RejectItemRequest request3 = new RejectItemRequest(ITEM_ID, REASON3);
		Mockito.when(ctx.getBean(RejectServicesItemExecutor.class)).thenReturn(executor3);
			
		ServiceResponse response3 = cmd.execute(request3);
				
		verify(executor3).execute(request3);
		assertEquals(ServiceResponse.SUCCESS, response3);
	}

	@Test
	public void testSucceededReason4() {
		RejectItemRequest request4 = new RejectItemRequest(ITEM_ID, REASON4);
		Mockito.when(ctx.getBean(RejectMakeupItemExecutor.class)).thenReturn(executor4);
	
		ServiceResponse response4 = cmd.execute(request4);
				
		verify(executor4).execute(request4);
		assertEquals(ServiceResponse.SUCCESS, response4);
	}

	@Test
	public void testSucceededReason5() {
		RejectItemRequest request5 = new RejectItemRequest(ITEM_ID, REASON5);
		Mockito.when(ctx.getBean(RejectAnimalsItemExecutor.class)).thenReturn(executor5);

		ServiceResponse response5 = cmd.execute(request5);

		verify(executor5).execute(request5);
		assertEquals(ServiceResponse.SUCCESS, response5);
	}

	@Test
	public void testSucceededReason6() {
		RejectItemRequest request6 = new RejectItemRequest(ITEM_ID, REASON6);
		Mockito.when(ctx.getBean(RejectCommissionItemExecutor.class)).thenReturn(executor6);
		
		ServiceResponse response6 = cmd.execute(request6);
								
		verify(executor6).execute(request6);
		assertEquals(ServiceResponse.SUCCESS, response6);
	}

	@Test
	public void testSucceededReason7() {
		RejectItemRequest request7 = new RejectItemRequest(ITEM_ID, REASON7);
		Mockito.when(ctx.getBean(RejectStyleItemExecutor.class)).thenReturn(executor7);
	
		ServiceResponse response7 = cmd.execute(request7);
				
		verify(executor7).execute(request7);
		assertEquals(ServiceResponse.SUCCESS, response7);
	}

	@Test
	public void testSucceededReason8() {
		RejectItemRequest request8 = new RejectItemRequest(ITEM_ID, REASON8);
		Mockito.when(ctx.getBean(RejectPriceItemExecutor.class)).thenReturn(executor8);
						
		ServiceResponse response8 = cmd.execute(request8);
								
		verify(executor8).execute(request8);
		assertEquals(ServiceResponse.SUCCESS, response8);

	}

	@Test
	public void testSucceededReason9() {
		RejectItemRequest request9 = new RejectItemRequest(ITEM_ID, REASON9);
		Mockito.when(ctx.getBean(RejectForbiddenItemExecutor.class)).thenReturn(executor9);
						
		ServiceResponse response9 = cmd.execute(request9);
								
		verify(executor9).execute(request9);
		assertEquals(ServiceResponse.SUCCESS, response9);
	}
}
