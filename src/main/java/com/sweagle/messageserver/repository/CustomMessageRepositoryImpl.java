package com.sweagle.messageserver.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sweagle.messageserver.entity.Message;

public class CustomMessageRepositoryImpl implements CustomMessageRepository {
	
	@Autowired
    private MongoTemplate mongotemplate;

	@Override
	public List<Message> listOfmessagesFromDateTimeAndBeforeEndDate(Date startDate, Date endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("date").gte(startDate).lt(endDate));
		return mongotemplate.find(query, Message.class);
	}

	@Override
	public long countOfMessagesFromDateTime(Date startDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("date").gte(startDate));
		return mongotemplate.count(query, Message.class);
	}
	
	private Query createCriteriaQuery(Date startDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("date").gte(startDate));
		return query;
	}

}
