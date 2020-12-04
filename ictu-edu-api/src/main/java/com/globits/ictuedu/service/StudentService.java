package com.globits.ictuedu.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto; 

public interface StudentService extends GenericService<Student, UUID>{

	Page<StudentDto> searchByPage(StudentSearchDto dto);
	
	StudentDto getOne(UUID id);
	
	StudentDto saveOneOrUpdateExcel(StudentDto dto, UUID id);
	
	public void deleteById(UUID id);
	
	UUID getStudentIdFromCode(String studentCode);
	
	ClassMajorsDto getMajorsAndClass();
	
	Page<StudentDto> searchByPageDuplicateStudentCode(StudentSearchDto dto);
	
	List<StudentDto> checkDuplicateStudentCode(List<String> codes);
	
	void deleteMultiple(List<UUID> idList);
	
	void deleteAll();
}
