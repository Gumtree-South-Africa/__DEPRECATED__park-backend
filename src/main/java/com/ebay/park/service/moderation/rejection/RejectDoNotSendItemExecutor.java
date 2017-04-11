package com.ebay.park.service.moderation.rejection;

import org.springframework.stereotype.Component;

import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.RejectItemRequest;

/**
 * It manages the item rejection when the reason of rejection is 'do not send'.
 * None feed is sent.
 * @author Julieta Salvadó
 *
 */
@Component
public class RejectDoNotSendItemExecutor extends RejectItemExecutor{

    @Override
    public ItemNotificationEvent execute(RejectItemRequest request)
            throws ServiceException {
        return new ItemNotificationEvent(super.rejectItem(request));
    }

}
