package com.sweagle.messageserver.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweagle.messageserver.entity.Message;
import com.sweagle.messageserver.repository.MessageRepository;
import com.sweagle.messageserver.service.entity.MessageInformation;

import org.slf4j.Logger;

@Service
public class MessageServiceImpl implements MessageService {
	
	private static int[] hourArr = {0, 6, 12, 18};
	private static String SENDER = "Sender";
	private static String RECEIVER = "Receiver";
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Override
	public Message saveMessage(Message message) throws IllegalArgumentException {
		validateMessage(message);
		Date savedDate = message.getDate() == null ? new Date() : message.getDate();
		message.setDate(savedDate);
		return messageRepository.save(message);
	}

	@Override
	public List<Message> getMessagesReceivedByEmail(String email) {
		return messageRepository.findMessagesByReceiver(email);
	}

	@Override
	public List<Message> getMessagesSentByEmail(String email) {
		return messageRepository.findMessagesBySender(email);
	}

	@Override
	public Message getMessageById(String id) {
		return messageRepository.findById(id).get();
	}

	@Override
	public long getProbableCountOfMessagesToSendForTheRestOfTheDay(ZonedDateTime rightNow) {
		MessageInformation messageInformation = getMessageInformation(rightNow);
		return messageInformation.getNumberOfMessageForTheRestOfTheday();
	}

	@Override
	public long getProbableCountOfMessagesToSendForTheRestOfTheWeek(ZonedDateTime rightNow) {
		MessageInformation messageInformation = getMessageInformation(rightNow);
		int remainingNumberOfDaysInCurrentWeek = calculateRemainingNumberOfDaysInCurrentWeek(rightNow);
		return messageInformation.getNumberOfMessageForTheRestOfTheday() + messageInformation.getTotalNumberOfMessagesInaDay() * remainingNumberOfDaysInCurrentWeek;
	}
	
	@Override
	public void deleteMessageBySender(String sender) {
		messageRepository.deleteBySender(sender);
		
	}
	
	private int calculateRemainingNumberOfDaysInCurrentWeek(ZonedDateTime rightNow) {
		int dayDiff = DayOfWeek.SUNDAY.getValue() - rightNow.getDayOfWeek().getValue();
		return dayDiff;
	}
	
	private long calculateNumberOfMessagesInaDay(long numberOfMsgsTillNow, ZonedDateTime rightNow) {
		final double dayInMillis = Duration.ofHours(24).toMillis();
		final ZonedDateTime beginningOfTheDay = findBeginingOfTheDay(rightNow);
		final double durationFromStart = findDurationBetweenTimeInMillis(beginningOfTheDay, rightNow);
		final long totalNumberOfmessagesInAday = Math.round(numberOfMsgsTillNow * (dayInMillis/(double)durationFromStart));
		return totalNumberOfmessagesInAday;
	}
	
	private MessageInformation getMessageInformation(ZonedDateTime rightNow) {
		long numberOfMessageForTheRestOfTheday = 0;
		long totalNumberOfMessagesInaDay = 0;
		long numberOfMsgsTillNow = 0;
		final Date sevenDaysBefore = findLastWeeksDateFromNow(rightNow);
		final Date beginningOfTheDay = findAndConvertIntoBeginningOfTheDay(rightNow);
		final List<Message> messages = messageRepository.listOfmessagesFromDateTimeAndBeforeEndDate(sevenDaysBefore, beginningOfTheDay);
		if (messages.isEmpty()) {
			numberOfMsgsTillNow = messageRepository.countOfMessagesFromDateTime(beginningOfTheDay);
			totalNumberOfMessagesInaDay = calculateNumberOfMessagesInaDay(numberOfMsgsTillNow, rightNow);
		} else {
			Map<Integer, Long> bucketMap = createHourAndMessageCountBucketForTheLastWeek(rightNow, messages);
			final int pos = rightNow.getHour()/6;
			numberOfMsgsTillNow = calculateNumberOfMessagesFromBeginingOfTheDayTillNow(pos, rightNow, bucketMap);
			totalNumberOfMessagesInaDay = bucketMap.values().stream().reduce((long) 0, Long::sum);
		}
		numberOfMessageForTheRestOfTheday = totalNumberOfMessagesInaDay - numberOfMsgsTillNow;
		return new MessageInformation(numberOfMessageForTheRestOfTheday, totalNumberOfMessagesInaDay);
		
	}
	
	private long findDurationBetweenTimeInMillis(ZonedDateTime start, ZonedDateTime end) {
		return Duration.between(start, end).toMillis();
	}
	
	private void checkUser(String user, String role) {
		if(userService.findByEmailId(user) == null) {
			throw new IllegalArgumentException(role + " does not exist, please choose a valid one");
		}
	}
	
	private long calculateNumberOfMessagesFromBeginingOfTheDayTillNow(int pos, ZonedDateTime rightNow, Map<Integer, Long> bucketMap) {
		return calculateNumberOfMessagesFromBeginingOfSixthHourTillNow(pos, rightNow, bucketMap) + calculateNumberOfMessagesFromBeginingOfDayTillSixthHour(pos, bucketMap);
	}
	
	private long calculateNumberOfMessagesFromBeginingOfSixthHourTillNow(int pos, ZonedDateTime rightNow, Map<Integer, Long> bucketMap) {
		int beginingHour = hourArr[pos];
		long numberOfMessagesInSixHours = bucketMap.get(beginingHour);
		long sixHoursInMilli = Duration.ofHours(6).toMillis();
		double numberOfMessageInOneMilli = numberOfMessagesInSixHours/(double)sixHoursInMilli;
		ZonedDateTime beginingOfSixthHour = ZonedDateTime.of(rightNow.getYear(), rightNow.getMonthValue(), rightNow.getDayOfMonth(), beginingHour, 0, 0, 0, rightNow.getZone());
		long durationBetweenBeginingOfSixthHourAndNowInMillis = findDurationBetweenTimeInMillis(beginingOfSixthHour, rightNow);
		return Math.round(durationBetweenBeginingOfSixthHourAndNowInMillis * numberOfMessageInOneMilli);
	}
	
	private long calculateNumberOfMessagesFromBeginingOfDayTillSixthHour(int pos, Map<Integer, Long> bucketMap) {
		long numberOfMsgs = 0;
		for (int i = 0; i < pos; i++) {
			numberOfMsgs = numberOfMsgs + bucketMap.get(hourArr[i]);
		}
		return numberOfMsgs;
	}
	
	private Date findLastWeeksDateFromNow(ZonedDateTime rightNow) {
		ZonedDateTime aWeekBack = rightNow.minusDays(7);
		ZonedDateTime start = findBeginingOfTheDay(aWeekBack);
		return convertZonedDateTimeToJavaUtilDate(start);
	}
	
	private Date convertZonedDateTimeToJavaUtilDate(ZonedDateTime zonedDateTime) {
		return Date.from(zonedDateTime.toInstant());
	}
	
	private Map<Integer, Long> createHourAndMessageCountBucketForTheLastWeek(ZonedDateTime rightNow, List<Message> messages) {
		Map<Integer, Long> bucketMap = new HashMap<>();
		for(int i : hourArr) {
			bucketMap.put(i, (long) 0);
		}
		messages.forEach(message -> {
			final ZonedDateTime dateOfMessage = ZonedDateTime.ofInstant(message.getDate().toInstant(), ZoneId.systemDefault());
			int bucket = dateOfMessage.getHour()/6;
			int key = hourArr[bucket];
			bucketMap.put(key, bucketMap.get(key) + 1);
		});
		Message firstMessage = messages.stream().min(java.util.Comparator.comparing(Message::getDate)).get();
		ZonedDateTime dateTimeOfFirstMsg = ZonedDateTime.ofInstant(firstMessage.getDate().toInstant(), ZoneId.systemDefault());
		int numberOfDays = rightNow.getDayOfMonth() - dateTimeOfFirstMsg.getDayOfMonth();
		
		calculateAverageNumberOfMsgs(bucketMap, numberOfDays);
		return bucketMap;
	}
	
	private void calculateAverageNumberOfMsgs(Map<Integer, Long> bucketMap, int numberOfDays) {
		bucketMap.keySet().forEach(key -> {
			bucketMap.put(key, Math.round(bucketMap.get(key)/(double)numberOfDays));
		});
	}
	
	private Date findAndConvertIntoBeginningOfTheDay(ZonedDateTime rightNow) {
		ZonedDateTime startOfTheDay = findBeginingOfTheDay(rightNow);
		return convertZonedDateTimeToJavaUtilDate(startOfTheDay);
	}
	
	private ZonedDateTime findBeginingOfTheDay(ZonedDateTime zonedDate) {
		return zonedDate.toLocalDate().atStartOfDay().atZone(zonedDate.getZone());
	}
	
	private void checkId(String id) {
		if (id != null) {
			throw new IllegalArgumentException("Message id is auto generated and should not be provided");
		}
	}
	
	private void validateMessage(Message message) {
		try {
			checkUser(message.getSender(), SENDER);
			checkUser(message.getReceiver(), RECEIVER);
			checkId(message.getId());
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		
	}

	

}
