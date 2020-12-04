package com.globits.ictuedu.dto;

import javax.persistence.Column;

import com.globits.core.dto.BaseObjectDto;
import com.globits.ictuedu.domain.Signature;

public class SignatureDto extends BaseObjectDto{

	  
	private String lineOne; 
	 
	private String lineTwo;  
	 
	private String lineThree;  
 
	private String signName;

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

	public SignatureDto(Signature entity) { 
		this.id=entity.getId();
		this.lineOne = entity.getLineOne();
		this.lineTwo = entity.getLineTwo();
		this.lineThree = entity.getLineThree();
		this.signName = entity.getSignName();
	}
	public SignatureDto() {
		// TODO Auto-generated constructor stub
	}
	 
	
}
