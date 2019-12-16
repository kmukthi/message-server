package com.sweagle.messageserver.repository;

import java.util.Date;
import java.util.List;

import com.sweagle.messageserver.entity.Message;

public interface CustomMessageRepository {
	
	public List<Message> listOfmessagesFromDateTimeAndBeforeEndDate(Date startDate, Date endDate);
	
	public long countOfMessagesFromDateTime(Date startDate);

}
