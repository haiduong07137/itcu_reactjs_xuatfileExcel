package com.globits.ictuedu.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.StudentDto;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID>{
	@Query(" from Student as entity where entity.code = ?1")
	public Student getStudentFromCode(String studentCode);
	
	@Query("SELECT new com.globits.ictuedu.dto.StudentDto(entity) from Student as entity where (1=1) ")
	public List<StudentDto> getAll();
	
	@Query("from Student as entity where (1=1) and entity.majors = ?1 ")
	public List<Student> getAllStudentByMajors(String majors);
	 
	@Query("from Student as entity where (1=1) and entity.studentClass = ?1 ")
	public List<Student> getAllStudentByClass(String majors);
	 
}
