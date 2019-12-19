package com.sweagle.messageserver.component;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sweagle.messageserver.entity.CachedData;
import com.sweagle.messageserver.entity.Message;
import com.sweagle.messageserver.repository.MessageRepository;
import com.sweagle.messageserver.service.MessageService;

@Component
public class StartUpInit {

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Resource(name = "applicationScopedBean")
    private CachedData cachedData;
	
	@PostConstruct
	public void init() {
		initialiseCache();
		setUpDataRefresh();
	}
	
	private void setUpDataRefresh() {
		Runnable runnable = () -> initialiseCache();
		//long period = Duration.ofMinutes(1).toMillis();
		long period = Duration.ofMinutes(24).toMillis();
		ZonedDateTime rightNow = ZonedDateTime.now();
		ZonedDateTime temp = rightNow.plusDays(1);
		ZonedDateTime startOfNextDay = temp.toLocalDate().atStartOfDay().atZone(temp.getZone());
		long delay = Duration.between(rightNow, startOfNextDay).toMillis();
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(runnable,
				delay,
			    period,
			    TimeUnit.MILLISECONDS);
		
	}
	
	private void initialiseCache() {
		System.out.println("Initialization code happens here");
		cachedData.setMessage("Message set"+ZonedDateTime.now().getMinute());
		ZonedDateTime rightNow = ZonedDateTime.now();
		final Date sevenDaysBefore = messageService.findLastWeeksDateFromNow(rightNow);
		final Date beginningOfTheDay = messageService.findAndConvertIntoBeginningOfTheDay(rightNow);
		final List<Message> messages = messageRepository.listOfmessagesFromDateTimeAndBeforeEndDate(sevenDaysBefore, beginningOfTheDay);
		Map<Integer, Long> bucketMap = messages.isEmpty() 
				? new HashMap<>() 
						: messageService.createHourAndMessageCountBucketForTheLastWeek(rightNow, messages);
		cachedData.setBucketMap(bucketMap);
		
	}
	
	
}
