package com.rand.ishowApi.contactUs.admin.domain.repository;


import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComplaintRepository extends JpaRepository<Complaint, Long>, JpaSpecificationExecutor<Complaint> {}
