package com.globits.ictuedu.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globits.ictuedu.Constants;
import com.globits.ictuedu.dto.UploadActivityLogDto;
import com.globits.ictuedu.dto.searchdto.UploadActivityLogSearchDto;
import com.globits.ictuedu.service.UploadActivityLogService;

@RestController
@RequestMapping("/api/upload-activity-log")
public class RestUploadActivityLogController {
	
	@Autowired
	UploadActivityLogService uploadActivityLogService;
	
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV, Constants.ROLE_QLDT})
	@PostMapping("/searchByPage")
	public ResponseEntity<Page<UploadActivityLogDto>> searchByPage(@RequestBody UploadActivityLogSearchDto searchDto) {
		Page<UploadActivityLogDto> result = uploadActivityLogService.searchByPage(searchDto);
		return new ResponseEntity<Page<UploadActivityLogDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	/* QUICK TESTING */
	@Secured({ Constants.ROLE_ADMIN ,Constants.ROLE_USER, Constants.ROLE_QLSV, Constants.ROLE_QLDT})
	@PostMapping("/add")
	public ResponseEntity<UploadActivityLogDto> saveOneOrUpdate(@RequestBody UploadActivityLogDto dto) {
		UploadActivityLogDto result = uploadActivityLogService.saveOneOrUpdate(dto, null);
		return new ResponseEntity<UploadActivityLogDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
