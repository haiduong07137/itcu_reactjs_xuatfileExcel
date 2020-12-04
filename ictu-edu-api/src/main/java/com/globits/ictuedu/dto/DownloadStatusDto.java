package com.globits.ictuedu.dto;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;

public class DownloadStatusDto {
	private String logDetail;
	private String status;
	private HSSFWorkbook hssfWb;
	private ByteArrayResource byteArrayRes;
	
	
	
	public ByteArrayResource getByteArrayRes() {
		return byteArrayRes;
	}
	public void setByteArrayRes(ByteArrayResource byteArrayRes) {
		this.byteArrayRes = byteArrayRes;
	}
	public String getLogDetail() {
		return logDetail;
	}
	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HSSFWorkbook getHssfWb() {
		return hssfWb;
	}
	public void setHssfWb(HSSFWorkbook hssfWb) {
		this.hssfWb = hssfWb;
	}

	
	
}
