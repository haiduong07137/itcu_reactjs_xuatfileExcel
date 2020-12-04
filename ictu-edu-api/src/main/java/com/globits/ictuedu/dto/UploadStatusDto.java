package com.globits.ictuedu.dto;
 
import java.util.List; 
 
public class UploadStatusDto  {
	 private List<StudentDto> listDto;  
	 private String status;
	 private String statusStudent;
	 private String statusAppendix;
	 
	 
	  
	public String getStatusAppendix() {
		return statusAppendix;
	}
	public void setStatusAppendix(String statusAppendix) {
		this.statusAppendix = statusAppendix;
	}
	public  String getStatusStudent() {
		return statusStudent;
	}
	public void setStatusStudent(String  statusStudent) {
		this.statusStudent = statusStudent;
	}
	public List<StudentDto> getListDto() {
		return listDto;
	}
	public void setListDto(List<StudentDto> listDto) {
		this.listDto = listDto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	 
	 
	 
	 
}
