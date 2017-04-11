/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service;

import com.ebay.park.exception.Context;
import com.ebay.park.util.KeyAndValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author juan.pizarro
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 3205885639656146081L;

	public static ServiceException createServiceException(ServiceExceptionCode code) {
		return new ServiceException(code, code.getMessage());
	}

	public static ServiceException createServiceException(ServiceExceptionCode code,
			String[] localizableMsgArg) {
		return new ServiceException(code, code.getMessage(), localizableMsgArg);
	}
	
	public static ServiceException createServiceException(ServiceExceptionCode code, Throwable cause) {
		return new ServiceException(code, cause);
	}

	private final int code;
	private Object[] msgArgs; // FIXME This is never set
	private String[] localizableMsgArgs;
	private Context exceptionContext = new Context();;
	private final ServiceExceptionCode serviceExceptionCode;
	
	protected ServiceException(ServiceExceptionCode code, String message) {
		super(message);
		this.code = code.getCode();
		this.serviceExceptionCode = code;
	}

	protected ServiceException(ServiceExceptionCode code, String message, String[] localizableMsgArg) {
		this(code, message);
		this.localizableMsgArgs = localizableMsgArg;
	}
	
	protected ServiceException(ServiceExceptionCode code, Throwable cause) {
		super(code.getMessage(), cause);
		this.code = code.getCode();
		this.serviceExceptionCode = code;
	}

	public int getCode() {
		return code;
	}

	public Object[] getMessageArgs() {
		return this.msgArgs;
	}

	public String[] getLocalizableMsgArgs() {
		return this.localizableMsgArgs;
	}

	public Context getContext() {
		return exceptionContext;
	}

	public void unsetContext() {
		exceptionContext = new Context();
	}

	public void setRequestToContext(Object data) {
		exceptionContext.addData(data);
	}

	public void setKeyValueListToContext(KeyAndValue... keyAndValueList) {

		Map<String, String> map = new HashMap<String, String>();
		for (KeyAndValue keyAndValue : keyAndValueList) {
			map.put(keyAndValue.getKey(), keyAndValue.getValue());
		}

		exceptionContext.addData(map);
	}
	
	/*By error we mean unexpected conditions, not input validation*/
	public boolean isError() {
		return this.serviceExceptionCode.isError();
	}

}
