package com.globits.ictuedu.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.Constants;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.DownloadStatusDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.service.FileUploadService;
import com.globits.ictuedu.service.StudentService;
import com.globits.ictuedu.service.impl.ExportExcelServiceImpl;
import com.globits.ictuedu.service.impl.StudentServiceImpl;
import com.globits.ictuedu.utils.ImportExportExcelUtil;

@RestController
@RequestMapping("/api/fileDownload")
public class RestFileDownloadController {

	@Autowired
	ImportExportExcelUtil importExportExcelUtil;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	StudentService studentService;
	@Autowired
	StudentServiceImpl studentServiceImpl;
	@Autowired
	ExportExcelServiceImpl exportExcelServiceImpl;

	@RequestMapping(value = "/excel", method = RequestMethod.POST)
	public void getExcelStudent(HttpSession session, HttpServletResponse response, @RequestBody StudentSearchDto dto) {
		ByteArrayResource BTRExcel = null;
		try { 
			BTRExcel = exportExcelServiceImpl.studentToExcel(dto);
			InputStream ins = new ByteArrayInputStream(BTRExcel.getByteArray());
			org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream()); 
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.addHeader("Content-Disposition", "attachment; filename=Asset.xlsx");  
			response.flushBuffer();
		} catch (Exception e) {
			System.out.println(e + "");
		} 
	}
	
	@RequestMapping(value = "/excelStatus", method = RequestMethod.POST)
	public DownloadStatusDto getStatusDownloadExcel(@RequestBody StudentSearchDto dto ) {
		DownloadStatusDto dtoStatus = new DownloadStatusDto();  
			try {
				dtoStatus = exportExcelServiceImpl.checkStatus(dto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  return dtoStatus;
	}
	
	@RequestMapping(value = "/excelStatusAll", method = RequestMethod.POST)
	public DownloadStatusDto getStatusDownloadAllExcel(@RequestBody StudentSearchDto dto ) {
		DownloadStatusDto dtoStatus = new DownloadStatusDto();  
			try {
				dtoStatus = exportExcelServiceImpl.checkStatusAll(dto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  return dtoStatus;
	}
	
	

	@RequestMapping(value = "/excelAll", method = RequestMethod.POST)
	public void getExcelAll(HttpSession session, HttpServletResponse response,@RequestBody StudentSearchDto dto) {
 
		ByteArrayResource BTRExcel = null;
		try { 
			
			BTRExcel = exportExcelServiceImpl.allStudentToExcel(dto);
			InputStream ins = new ByteArrayInputStream(BTRExcel.getByteArray());
			org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.addHeader("Content-Disposition", "attachment; filename=Asset.xlsx");
			response.flushBuffer();
		} catch (Exception e) {
			System.out.println(e + "");
		}  
	}
	
	@RequestMapping(value = "/excelAppendix", method = RequestMethod.POST)
	public void getExcelAppendix(HttpSession session, HttpServletResponse response, @RequestBody AppendixSearchDto dto) {
		DownloadStatusDto dtoStatus = new DownloadStatusDto();
		try { 
			ByteArrayResource excelFile =null;
			excelFile = exportExcelServiceImpl.phuLucToExcel(dto);
			InputStream ins = new ByteArrayInputStream(excelFile.getByteArray());
			org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.addHeader("Content-Disposition", "attachment; filename=Asset.xlsx");
			response.flushBuffer();
		} catch (Exception e) {
			System.out.println(e + "");
		}
		 
	}
	
	@RequestMapping(value = "/excelAppendixStatus", method = RequestMethod.POST)
	public DownloadStatusDto getStatusDownloadAppendix(@RequestBody AppendixSearchDto dto ) {
		DownloadStatusDto dtoStatus = new DownloadStatusDto();  
			try {
				dtoStatus = exportExcelServiceImpl.checkStatusPhuLuc(dto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  return dtoStatus;
	}

}
