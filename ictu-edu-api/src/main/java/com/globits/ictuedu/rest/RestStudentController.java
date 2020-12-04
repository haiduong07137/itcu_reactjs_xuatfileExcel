package com.globits.ictuedu.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.globits.core.domain.Department;
import com.globits.core.domain.Person;
import com.globits.core.dto.PersonDto;
import com.globits.core.repository.DepartmentRepository;
import com.globits.ictuedu.Constants;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.service.ExportExcelService;
import com.globits.ictuedu.service.impl.ExportExcelServiceImpl;
import com.globits.ictuedu.service.impl.StudentServiceImpl; 

@RestController  
@RequestMapping("/api/student")
public class RestStudentController { 

	@Autowired
	StudentServiceImpl studentServiceImpl;
	@Autowired
	ExportExcelServiceImpl exportExcelServiceImpl;
	
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<StudentDto>> searchByPage(@RequestBody StudentSearchDto dto) {
		Page<StudentDto> result = studentServiceImpl.searchByPage(dto);
		return new ResponseEntity<Page<StudentDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentDto> getOne(@PathVariable("id") UUID id) {
		StudentDto dto = studentServiceImpl.getOne(id); 
		return new ResponseEntity<StudentDto>(dto, HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudentDto> updateOne(@RequestBody StudentDto dto, @PathVariable("id") UUID id) {
		StudentDto result = studentServiceImpl.saveOneOrUpdateExcel(dto, id); 
		return new ResponseEntity<StudentDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/getListMajorClass", method = RequestMethod.POST)
	public ResponseEntity<ClassMajorsDto> getListMajorAndClass() {
		ClassMajorsDto result = studentServiceImpl.getMajorsAndClass();
		return new ResponseEntity<ClassMajorsDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/searchByPageDuplicateStudentCode", method = RequestMethod.POST)
	public  ResponseEntity<Page<StudentDto>>  searchByPageDuplicateStudentCode(@RequestBody StudentSearchDto dto) {
		Page<StudentDto> result = studentServiceImpl.searchByPageDuplicateStudentCode(dto);
		return new ResponseEntity<Page<StudentDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/deleteMultiple", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteMultiple(@RequestBody List<UUID> idList) {
		studentServiceImpl.deleteMultiple(idList);
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV})
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteAll() {
		studentServiceImpl.deleteAll();
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
	
}
