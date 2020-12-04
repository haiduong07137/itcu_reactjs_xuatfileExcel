package com.globits.ictuedu.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.ictuedu.domain.UploadActivityLog;

@Repository
public interface UploadActivityLogRepository extends JpaRepository<UploadActivityLog, UUID> {

}
