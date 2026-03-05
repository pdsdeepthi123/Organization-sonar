package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SignatureTemplates;

@Repository
public interface SignatureTemplatesRepository extends JpaRepository<SignatureTemplates, Integer> {

	@Query("SELECT st FROM SignatureTemplates st")
	List<SignatureTemplates> getAllTemplates();

	@Query("SELECT st FROM SignatureTemplates st WHERE st.id = :id")
	SignatureTemplates getTemplateImageById(int id);
}
