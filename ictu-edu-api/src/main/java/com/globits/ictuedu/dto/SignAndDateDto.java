package com.globits.ictuedu.dto;

import javax.persistence.Column;

import com.globits.core.dto.BaseObjectDto;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;

public class SignAndDateDto  {
	private String dateExport;

	private String lineOne;

	private String lineTwo;

	private String lineThree;

	private String signName;
	private Boolean isFormal;

	public Boolean getIsFormal() {
		return isFormal;
	}

	public void setIsFormal(Boolean isFormal) {
		this.isFormal = isFormal;
	}

	public String getDateExport() {
		return dateExport;
	}

	public void setDateExport(String dateExport) {
		this.dateExport = dateExport;
	}

	public String getLineOne() {
		return lineOne;
	}

	public void setLineOne(String lineOne) {
		this.lineOne = lineOne;
	}

	public String getLineTwo() {
		return lineTwo;
	}

	public void setLineTwo(String lineTwo) {
		this.lineTwo = lineTwo;
	}

	public String getLineThree() {
		return lineThree;
	}

	public void setLineThree(String lineThree) {
		this.lineThree = lineThree;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
	
	
}
