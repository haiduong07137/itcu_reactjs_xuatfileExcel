package com.globits.ictuedu.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;

@Entity
@Table(name = "tbl_activity_log_upload")
public class UploadActivityLog extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "upload_time")
	private Timestamp uploadTime;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "status")
	private String status; // SUCCESSFUL, FAILED
	
	@Column(name = "log_detail", columnDefinition="TEXT")
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
