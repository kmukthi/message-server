package com.sweagle.messageserver.entity;

import java.util.Map;

public class CachedData {

    private String message;
    
    Map<Integer, Long> bucketMap;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

	public Map<Integer, Long> getBucketMap() {
		return bucketMap;
	}

	public void setBucketMap(Map<Integer, Long> bucketMap) {
		this.bucketMap = bucketMap;
	}
    
    

}
