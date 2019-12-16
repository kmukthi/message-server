package com.sweagle.messageserver.service.entity;

public class MessageInformation {
	
	private long numberOfMessageForTheRestOfTheday;
	private long totalNumberOfMessagesInaDay;
	
	private MessageInformation() {}
	
	public MessageInformation(long numberOfMessageForTheRestOfTheday, long totalNumberOfMessagesInaDay) {
		this.numberOfMessageForTheRestOfTheday = numberOfMessageForTheRestOfTheday;
		this.totalNumberOfMessagesInaDay = totalNumberOfMessagesInaDay;
	}

	public long getNumberOfMessageForTheRestOfTheday() {
		return numberOfMessageForTheRestOfTheday;
	}

	public long getTotalNumberOfMessagesInaDay() {
		return totalNumberOfMessagesInaDay;
	}


}
