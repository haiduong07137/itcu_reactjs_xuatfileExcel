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
import com.globits.ictuedu.dto.SignatureDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.SignatureSearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.service.ExportExcelService;
import com.globits.ictuedu.service.SignatureService;
import com.globits.ictuedu.service.impl.AppendixServiceImpl;
import com.globits.ictuedu.service.impl.ExportExcelServiceImpl;
import com.globits.ictuedu.service.impl.SignatureServiceImpl;
import com.globits.ictuedu.service.impl.StudentServiceImpl; 

@RestController  
@RequestMapping("/api/signature")
public class RestSignatureController { 
 
	@Autowired
	SignatureServiceImpl signatureServiceImpl;
	
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT,Constants.ROLE_QLSV})
	@RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<SignatureDto>> searchByPage(@RequestBody SignatureSearchDto dto) {
		Page<SignatureDto> result = signatureServiceImpl.searchByPage(dto);
		return new ResponseEntity<Page<SignatureDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT,Constants.ROLE_QLSV})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SignatureDto> getOne(@PathVariable("id") UUID id) {
		SignatureDto dto = signatureServiceImpl.getOne(id); 
		return new ResponseEntity<SignatureDto>(dto, HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT,Constants.ROLE_QLSV})
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<SignatureDto> saveOne(@RequestBody SignatureDto dto) {
		SignatureDto result = signatureServiceImpl.saveOneOrUpdateExcel(dto, null); 
		return new ResponseEntity<SignatureDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,Constants.ROLE_QLDT,Constants.ROLE_QLSV})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<SignatureDto> updateOne(@RequestBody SignatureDto dto,@PathVariable("id") UUID id) {
		SignatureDto result = signatureServiceImpl.saveOneOrUpdateExcel(dto, id); 
		return new ResponseEntity<SignatureDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER})
	@RequestMapping(value = "/deleteMultiple", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteMultiple(@RequestBody List<UUID> idList) {
		signatureServiceImpl.deleteMultiple(idList);
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER})
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteAll() {
		signatureServiceImpl.deleteAll();
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
 
	
	
	
}
