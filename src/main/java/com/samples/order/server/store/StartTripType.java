package com.softwareag.mediator.server.store;

public class StartTripType {
	private String sessionID;
	private WelcomeMessageType welcomeMessage;
	private LocaleType locale;

	public String getSessionID() {
		return this.sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public WelcomeMessageType getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(WelcomeMessageType welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public LocaleType getLocale() {
		return this.locale;
	}

	public void setLocale(LocaleType locale) {
		this.locale = locale;
	}
}
