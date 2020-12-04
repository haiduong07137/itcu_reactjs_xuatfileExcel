package com.globits.ictuedu.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto; 

public interface AppendixService extends GenericService<Appendix, UUID>{

	Page<AppendixDto> searchByPage(AppendixSearchDto dto);
	
	AppendixDto getOne(UUID id);
	
	AppendixDto saveOneOrUpdateExcel(AppendixDto dto, UUID id);
	
	public void deleteById(UUID id);
	
	public List<StudentDto> checkDuplicateStudentCode(List<String> codes) ;
	public List<AppendixDto> checkDuplicateStudentCodeAppendix(List<String> codes);
	public ClassMajorsDto getMajorsAndClass();
}
