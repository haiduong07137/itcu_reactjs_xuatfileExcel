package com.globits.ictuedu.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.UploadActivityLogDto; 
public interface FileUploadService {
	UploadActivityLogDto importStudentTranscriptFromInputStream(MultipartFile[] uploadfiles);
	
	UploadActivityLogDto importThongTinPhuLucInputStream(MultipartFile[] uploadfiles); 
	 
}
