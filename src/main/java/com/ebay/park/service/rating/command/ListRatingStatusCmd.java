package com.ebay.park.service.rating.command;

import com.ebay.park.db.entity.RatingStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * List the several rating status a rating can have.
 * @author marcos.lambolay
 */
@Component
public class ListRatingStatusCmd {

	public List<String> execute() {
		RatingStatus[] values = RatingStatus.values();
		List<String> result = new ArrayList<String>(values.length); 
		for(RatingStatus status : values) {
			result.add(status.getDescription());
		}
		
		return result;
	}
}
