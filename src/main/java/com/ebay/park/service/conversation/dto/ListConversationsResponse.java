package com.ebay.park.service.conversation.dto;


import com.ebay.park.service.PaginatedResponse;

import java.util.List;

/**
 * 
 * @author marcos.lambolay
 *
 */
public class ListConversationsResponse extends PaginatedResponse{
	private List<SmallConversation> conversations;

	
	public ListConversationsResponse(List<SmallConversation> conversations, long totalResults){
		super(totalResults, conversations.size());
		this.conversations = conversations;
	}
	
	
	public List<SmallConversation> getConversations() {
		return conversations;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.service.ListedResponse#listIsEmpty()
	 */
	@Override
	public boolean listIsEmpty() {
		return conversations.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListConversationsResponse [conversations=").append(conversations).append(", totalResults=")
				.append(totalElements).append("]");
		return builder.toString();
	}

	/**
	 * FIXME coordinate with FE use totalElements instead
	 * @return the totalResults
	 */
	public long getTotalResults() {
		return totalElements;
	}	
}
