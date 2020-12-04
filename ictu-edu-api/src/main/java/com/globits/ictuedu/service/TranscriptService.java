package com.globits.ictuedu.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.dto.searchdto.TranscriptSearchDto;

public interface TranscriptService extends GenericService<Transcript, UUID>{

	Page<TranscriptDto> searchByPage(TranscriptSearchDto dto);
	
	Page<TranscriptDto> getScoreByStudentCode(StudentSearchDto dto);
	  
	TranscriptDto getOne(UUID id);
	
	TranscriptDto saveOneOrUpdate(TranscriptDto dto, UUID id,UUID idStudent);
	
	public void deleteById(UUID id);
	 
}
