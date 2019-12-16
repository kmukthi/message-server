package com.sweagle.messageserver.util;

import java.time.ZonedDateTime;
import java.util.Date;

public class Util {
	
	public static Date findLastWeeksDateFromNow(ZonedDateTime rightNow) {
		ZonedDateTime aWeekBack = rightNow.minusDays(7);
		ZonedDateTime start = findBeginingOfTheDay(aWeekBack);
		return convertZonedDateTimeToJavaUtilDate(start);
	}
	
	public static ZonedDateTime findBeginingOfTheDay(ZonedDateTime zonedDate) {
		return zonedDate.toLocalDate().atStartOfDay().atZone(zonedDate.getZone());
	}
	
	public static Date convertZonedDateTimeToJavaUtilDate(ZonedDateTime zonedDateTime) {
		return Date.from(zonedDateTime.toInstant());
	}

}
