package com.globits.ictuedu.dto;

import java.sql.Timestamp;
import java.util.Date;

import com.globits.core.dto.BaseObjectDto;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.UploadActivityLog;

public class UploadActivityLogDto extends BaseObjectDto {
	
	private Timestamp uploadTime;
	
	private String fileName;
	
	private String status; // SUCCESSFUL, FAILED
	
	private String logDetail;

	public Timestamp getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}
	
	public UploadActivityLogDto() {
		// TODO Auto-generated constructor stub
	}

	public UploadActivityLogDto(UploadActivityLog entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.uploadTime = entity.getUploadTime();
			this.fileName = entity.getFileName();
			this.status = entity.getStatus();
			this.logDetail = entity.getLogDetail();
		}
	}
}
