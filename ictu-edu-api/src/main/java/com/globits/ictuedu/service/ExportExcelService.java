package com.globits.ictuedu.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.dto.DownloadStatusDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;

public interface ExportExcelService {
	public ByteArrayResource studentToExcel(StudentSearchDto dto ) throws IOException;
	
	public ByteArrayResource allStudentToExcel(StudentSearchDto dto ) throws IOException;
}
