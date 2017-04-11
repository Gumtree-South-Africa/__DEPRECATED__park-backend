package com.ebay.park.service.rating.command;

import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.rating.dto.RateUserRequest;

public interface RateUserCmd extends ServiceCommand<RateUserRequest, UsersEvent>{

}
