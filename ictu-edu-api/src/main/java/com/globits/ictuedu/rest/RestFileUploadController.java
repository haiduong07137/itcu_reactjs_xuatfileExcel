package com.globits.ictuedu.rest;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.Constants;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.UploadActivityLogDto; 
import com.globits.ictuedu.service.FileUploadService;
import com.globits.ictuedu.service.StudentService;
import com.globits.ictuedu.service.impl.UploadActivityLogServiceImpl;
import com.globits.ictuedu.utils.ImportExportExcelUtil;

@RestController
@RequestMapping("/api/fileUpload")
public class RestFileUploadController {

	@Autowired
	ImportExportExcelUtil importExportExcelUtil;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	StudentService studentService;
	
	@Autowired
	UploadActivityLogServiceImpl  uploadActivityLogServiceImplService;


	@RequestMapping(value = "/importStudentTranscript", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<UploadActivityLogDto> importStudentTranscript(
			@RequestParam("uploadfiles") MultipartFile[] uploadfiles) {
		UploadActivityLogDto status = new UploadActivityLogDto();
		try {
			status = fileUploadService.importStudentTranscriptFromInputStream(uploadfiles);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<UploadActivityLogDto>(status, (status != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/importThongTinPhuLuc", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<UploadActivityLogDto> importThongTinPhuLuc(
			@RequestParam("uploadfiles") MultipartFile[] uploadfiles) {
		UploadActivityLogDto status = new UploadActivityLogDto();
		try { 
			status = fileUploadService.importThongTinPhuLucInputStream(uploadfiles);   
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<UploadActivityLogDto>(status, (status != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
