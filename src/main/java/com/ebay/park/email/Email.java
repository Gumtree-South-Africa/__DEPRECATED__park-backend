/*
 * Copyright eBay, 2014
 */
package com.ebay.park.email;

import java.util.Map;

/**
 * This is just a simple pojo to represent an email. It holds:
 * <ul>
 *     <li>to: email's  receptor </li>
 *     <li>from: email's sender</li>
 *     <li>fromName: email's sender name</li>
 *     <li>subject: email's subject </li>
 *     <li>template (to be filled with params</li>
 *     <li>params (to fill the template)</li>
 *     <li>rawBody: email's raw body</li>
 *     <li>htmlFormat: email's html </li>
 * </ul>
 *
 * @author jpizarro
 */
public class Email {

	private String to;
	private String from;
	private String fromName;
	private String subject;
	private String template;
	private Map<String, String> params;
	private String rawBody;
	private Boolean htmlFormat;

    public Email() {

    }

    public Email(String to, String from, String fromName, String subject) {
        this();
        this.to = to;
        this.from = from;
        this.fromName = fromName;
        this.subject = subject;
    }

    public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getRawBody() {
		return rawBody;
	}

	public void setRawBody(String rawBody) {
		this.rawBody = rawBody;
	}

	public Boolean isHtmlFormat() {
		return htmlFormat;
	}

	public void setHtmlFormat(Boolean isHtml) {
		this.htmlFormat = isHtml;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		//@formatter:off
		     builder.append("Email [")
			.append("email.from=" + this.getFrom())
			.append(" email.fromName=" + this.getFromName())
			.append("; email.subject=" + this.getSubject())
			.append("; email.to=" + this.getTo())
			.append("; email.template=" + this.getTemplate())
			.append("; email.params=" + this.getParams())
			.append("; email.rawBody=" + this.getRawBody())
			.append("; email.isHtmlFormat=" + ((this.isHtmlFormat() != null)?this.isHtmlFormat():"true"))
			.append("]");
			//@formatter:on		
		return builder.toString();

	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
}
