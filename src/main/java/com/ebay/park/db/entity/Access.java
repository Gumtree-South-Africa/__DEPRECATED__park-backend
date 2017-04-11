package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Access implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "use_last_forget")
	private Date lastForgetPwdRequest;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "use_last_signin_attempt")
	private Date lastSignInAttempt;

	@Column(name = "use_failed_signin_attempts")
	private int failedSignInAttempts;

	@Column(name = "use_temporary_token")
	private String temporaryToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "use_last_successful_login")
	private Date lastSuccessfulLogin;

	public Access() {
		this.failedSignInAttempts = 0;
	}

	public Date getLastForgetPwdRequest() {
		return lastForgetPwdRequest;
	}

	public void setLastForgetPwdRequest(Date lastForgetPwdRequest) {
		this.lastForgetPwdRequest = lastForgetPwdRequest;
	}

	public Date getLastSignInAttempt() {
		return lastSignInAttempt;
	}

	public void updateLastSignInAttempt() {
		this.lastSignInAttempt = new Date();
	}

	public int getFailedSignInAttempts() {
		return failedSignInAttempts;
	}

	public void incFailedSignInAttempts() {
		this.updateLastSignInAttempt();
		failedSignInAttempts++;
	}
	
	public void resetFailedSignInAttempts(){
		this.updateLastSignInAttempt();
		this.updateLastSuccessfulLogin();
		failedSignInAttempts = 0;
	}

	public String getTemporaryToken() {
		return temporaryToken;
	}

	public void setTemporaryToken(String temporaryToken) {
		this.temporaryToken = temporaryToken;
	}

	public Date getLastSuccessfulLogin() {
		return lastSuccessfulLogin;
	}

	public void updateLastSuccessfulLogin() {
		this.lastSuccessfulLogin = new Date();
	}
}
