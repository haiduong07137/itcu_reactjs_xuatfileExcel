package com.globits.ictuedu.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.TranscriptDto; 

@Repository
@Transactional
public interface TranscriptRepository extends JpaRepository<Transcript, UUID> {
	@Transactional
	@Modifying
	@Query("DELETE FROM Transcript as transcript WHERE transcript.student.id = ?1")
	public void deleteAllTranscripts(UUID studentID);

	@Query("select new com.globits.ictuedu.dto.TranscriptDto(entity) from Transcript as entity where (1=1) AND entity.student.id = ?1")
	List<TranscriptDto> getByUserId(UUID studentID);
}
