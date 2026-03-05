package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberStatusModel;

@Repository
public interface SubscriberStatusRepository extends JpaRepository<SubscriberStatusModel, String>{

	SubscriberStatusModel findBysubscriberUid(String subscriberUniqueId);

}
