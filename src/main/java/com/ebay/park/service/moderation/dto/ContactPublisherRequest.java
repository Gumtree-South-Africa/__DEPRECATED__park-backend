package com.ebay.park.service.moderation.dto;

public class ContactPublisherRequest extends ItemRequest {

	private String subject;
	private String body;

	public ContactPublisherRequest() {
		super();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContactPublisherRequest [itemId= ")
			.append(this.getItemId()).append("]");
			
	return builder.toString();
	}
}
