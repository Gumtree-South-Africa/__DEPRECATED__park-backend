package com.ebay.park.service.conversation.dto;


public class SendOfferRequest extends SendChatRequest{

	private String offeredPrice;
	
	public String getOfferedPrice() {
		return offeredPrice;
	}
	public void setOfferedPrice(String offeredPrice) {
		this.offeredPrice = offeredPrice;
	}
	
	/* (non-Javadoc)
	 * @see com.ebay.park.service.conversation.dto.SendChatRequest#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendOfferRequest [itemId= ")
			.append(this.getItemId()).append(", comment= ")
			.append(this.getComment()).append(", conversationId= ")
			.append(this.getConversationId()).append(", brandPublish= ")
			.append(this.getBrandPublish()).append(", versionPublish= ")
			.append(this.getVersionPublish()).append(", offeredPrice= ")
			.append(this.offeredPrice).append("]");
		
		return builder.toString();
	}
}
