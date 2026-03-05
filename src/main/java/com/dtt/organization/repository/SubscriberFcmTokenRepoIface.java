package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberFcmToken;



@Repository
public interface SubscriberFcmTokenRepoIface extends JpaRepository<SubscriberFcmToken, Integer>{

	SubscriberFcmToken findBysubscriberUid(String suid);
	
}
