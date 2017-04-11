package com.ebay.park.service.social.dto;

import com.ebay.park.db.entity.Rating;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to filter a given collection of {@link Rating} with a <code>ratingStatus</code>
 * @author lucia.masola
 *
 */
public class RatingsPredicate implements Predicate<Rating> {
	
	/**
     * The rating status
     */
    private final List<String> ratingStatus;

    /**
     * Creates a new rating predicate
     *
     * @param ratingStatus The rating status list
     */
    public RatingsPredicate(final List<String> ratingStatus) {
        super();
        if (ratingStatus != null) {
            this.ratingStatus = ratingStatus;
        } else {
            this.ratingStatus = new ArrayList<String>();
        }
    }

    @Override
    public boolean apply(final Rating rating) {
    	if(rating.getStatus() == null){
    		return false;
    	}
    	return ratingStatus.contains(rating.getStatus().getDescription());
    }

}
