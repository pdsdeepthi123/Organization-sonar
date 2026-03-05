package com.dtt.organization.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberOnboardingData;


@Repository
public interface SubscriberOnboardingDataRepoIface extends JpaRepository<SubscriberOnboardingData, Integer> {



	SubscriberOnboardingData findTop1BySubscriberUidOrderByCreatedDateDesc(String uid);

	default SubscriberOnboardingData getBySubUid(String uid) {
		return findTop1BySubscriberUidOrderByCreatedDateDesc(uid);
	}

}
