package com.globits.ictuedu.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.TranscriptDto; 

@Repository
@Transactional
public interface AppendixRepository extends JpaRepository<Appendix, UUID> {
//	@Transactional
//	@Modifying
//	@Query("DELETE FROM Transcript as transcript WHERE transcript.student.id = ?1")
//	public void deleteAllTranscripts(UUID studentID);
//
	@Query("select new com.globits.ictuedu.dto.AppendixDto(entity) from Appendix as entity where (1=1) AND entity.code = ?1")
	AppendixDto getStudentByCode(String code);

	@Query("from Appendix as entity where (1=1) and entity.majors = ?1 ")
	public List<Appendix> getAllStudentByMajors(String majors);
	 
	@Query("from Appendix as entity where (1=1) and entity.studentClass = ?1 ")
	public List<Appendix> getAllStudentByClass(String majors);
}
