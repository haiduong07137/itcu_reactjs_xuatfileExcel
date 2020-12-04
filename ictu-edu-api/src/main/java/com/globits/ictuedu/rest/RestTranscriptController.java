package com.globits.ictuedu.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.dto.searchdto.TranscriptSearchDto;
import com.globits.ictuedu.service.TranscriptService;
import com.globits.ictuedu.service.impl.StudentServiceImpl;
import com.globits.ictuedu.service.impl.TranscriptServiceImpl; 

@RestController  
@RequestMapping("/api/transcript")
public class RestTranscriptController { 

	@Autowired
	TranscriptServiceImpl transcriptServiceImpl;
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER,  Constants.ROLE_QLSV, Constants.ROLE_QLDT})
	@RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<TranscriptDto>> createAgency(@RequestBody TranscriptSearchDto dto) { 
		Page<TranscriptDto> result = transcriptServiceImpl.searchByPage(dto);
		return new ResponseEntity<Page<TranscriptDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV, Constants.ROLE_QLDT})
	@RequestMapping(value = "/getScoreByStudentCode", method = RequestMethod.POST)
	public ResponseEntity<Page<TranscriptDto>> createAgency(@RequestBody StudentSearchDto dto) { 
		Page<TranscriptDto> result = transcriptServiceImpl.getScoreByStudentCode(dto);
		return new ResponseEntity<Page<TranscriptDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
