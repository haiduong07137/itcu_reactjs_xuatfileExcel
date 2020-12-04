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
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.service.ExportExcelService;
import com.globits.ictuedu.service.impl.AppendixServiceImpl;
import com.globits.ictuedu.service.impl.ExportExcelServiceImpl;
import com.globits.ictuedu.service.impl.StudentServiceImpl; 

@RestController  
@RequestMapping("/api/appendix")
public class RestAppendixController { 

	@Autowired
	AppendixServiceImpl appendixServiceImpl;
	@Autowired
	ExportExcelServiceImpl exportExcelServiceImpl;
	
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT })
	@RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<AppendixDto>> searchByPage(@RequestBody AppendixSearchDto dto) {
		Page<AppendixDto> result = appendixServiceImpl.searchByPage(dto);
		return new ResponseEntity<Page<AppendixDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<AppendixDto> getOne(@PathVariable("id") UUID id) {
		AppendixDto dto = appendixServiceImpl.getOne(id); 
		return new ResponseEntity<AppendixDto>(dto, HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<AppendixDto> updateOne(@RequestBody AppendixDto dto, @PathVariable("id") UUID id) {
		AppendixDto result = appendixServiceImpl.saveOneOrUpdateExcel(dto, id); 
		return new ResponseEntity<AppendixDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT})
	@RequestMapping(value = "/getListMajorClass", method = RequestMethod.POST)
	public ResponseEntity<ClassMajorsDto> getListMajorAndClass() {
		ClassMajorsDto result = appendixServiceImpl.getMajorsAndClass();
		return new ResponseEntity<ClassMajorsDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT})
	@RequestMapping(value = "/deleteMultiple", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteMultiple(@RequestBody List<UUID> idList) {
		appendixServiceImpl.deleteMultiple(idList);
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT})
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteAll() {
		appendixServiceImpl.deleteAll();
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
 
	
	
	
}
